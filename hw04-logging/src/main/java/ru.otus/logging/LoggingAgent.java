package ru.otus.logging;

import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class LoggingAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {

                List<String> methodsForLogging = new ArrayList<>();
                Class<?> aClass = new MyClassLoader().getClass(className, classfileBuffer, protectionDomain);
                Method[] methods = aClass.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation logAnnotation = method.getDeclaredAnnotation(Log.class);
                    if (logAnnotation != null) {
                        methodsForLogging.add(method.getName());
                    }
                }

                if (!methodsForLogging.isEmpty()) {
                    System.out.println(methodsForLogging.get(0));
                    return addLogMethod(classfileBuffer, methodsForLogging.get(0), className);
                }
                return classfileBuffer;
            }
        });

    }


    private static class MyClassLoader extends ClassLoader {
        private Class getClass(String className, byte[] classfileBuffer, ProtectionDomain protectionDomain) {
            String correctedClassName = className.replace("/", ".");
            ByteBuffer byteBuffer = ByteBuffer.wrap(classfileBuffer);
            return super.defineClass(correctedClassName, byteBuffer, protectionDomain);
        }
    }

    private static byte[] addLogMethod(byte[] originalClass, String methodName, String className) {
        //className = className.replace("/", ".");
        ClassReader cr = new ClassReader(originalClass);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (name.equals(methodName.toString())) {
                    return super.visitMethod(access, methodName + "Logging", descriptor, signature, exceptions);
                } else {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }
        };
        cr.accept(cv, Opcodes.ASM5);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, "(Ljava/lang/String;)V", null, null);

        Handle handle = new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", handle, "executed method: " + methodName + ", param: \u0001");

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        System.out.println(className);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, methodName + "Logging", "(Ljava/lang/String;)V", false);

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();


        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("LoggingASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }



}
