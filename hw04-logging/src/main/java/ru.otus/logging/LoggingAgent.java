package ru.otus.logging;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class LoggingAgent {

    public static List<String> methodsForLogging = new ArrayList<>();
    public static List<String> parameterTypes = new ArrayList<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {



                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
                ClassVisitor cv = new AnnotationScanner(Opcodes.ASM5, cw);
                cr.accept(cv,0);

                if (!methodsForLogging.isEmpty()) {
                    return addLogMethod(classfileBuffer, methodsForLogging, className);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] addLogMethod(byte[] originalClass, List methodNames, String className) {

        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (methodNames.contains(name)) {
                    return super.visitMethod(access, name + "Logging", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        Handle handle = new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        for (int i = 0; i < methodNames.size(); i++) {
            String methodName = methodNames.get(i).toString();
            String parameterType = parameterTypes.get(i).toString();

            Type parameterAsmType = Type.getType(parameterType);
            int opcodesType = parameterAsmType.getOpcode(Opcodes.ILOAD);

            MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, "(" + parameterType + ")V", null, null);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn(opcodesType, 1);
            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + parameterType + ")Ljava/lang/String;", handle, "executed method: " + methodName + ", param: \u0001");

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(opcodesType, 1);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methodName + "Logging", "(" + parameterType + ")V", false);

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }




        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("LoggingASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

    public static class AnnotationScanner extends ClassVisitor{

        private String lastCheckedMethod;
        private String innerDesc;

        public AnnotationScanner(int api){
            super(api);
        }

        public AnnotationScanner(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            lastCheckedMethod = name;
            innerDesc = desc;
            return new AnnotationScanner.MethodAnnotationScanner();
        }

        class MethodAnnotationScanner extends MethodVisitor{

            MethodAnnotationScanner(){ super(Opcodes.ASM5); }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible){
                if(desc.contains("Lru/otus/logging/Log;")) {
                    methodsForLogging.add(lastCheckedMethod);
                    parameterTypes.add(innerDesc.replaceAll("\\(", "").replaceAll("\\)V", ""));
                }
                return super.visitAnnotation(desc, visible);
            }
        }

    }



}
