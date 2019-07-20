package ru.otus.runner;

import ru.otus.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Runner {

    private List<Method> methodList;
    private List<Method> methodBeforAllList;
    private List<Method> methodBeforeList;
    private List<Method> methodTestList;
    private List<Method> methodAfterList;
    private List<Method> methodAfterAllList;


    public Runner() {
        this.methodList = new ArrayList<>();
        this.methodBeforAllList = new ArrayList<>();
        this.methodBeforeList = new ArrayList<>();
        this.methodTestList = new ArrayList<>();
        this.methodAfterList = new ArrayList<>();
        this.methodAfterAllList = new ArrayList<>();
    }

    //мы тестируем AnnotationClass, но не факт, что в следующий раз не потребуется тестировать другой класс, поэтому везде мы используем Class<...> в методах

    public void run(Class<?> annotationsClass) {

        Method[] methods = annotationsClass.getDeclaredMethods();

        for (Method method : methods) {

            Annotation beforeAllAnnotation = method.getDeclaredAnnotation(BeforeAll.class);

            if (beforeAllAnnotation != null) {
                this.methodBeforAllList.add(method);
            }
        }

        for (Method method : methods) {

            Annotation beforeAnnotation = method.getDeclaredAnnotation(Before.class);

            if (beforeAnnotation != null) {
                this.methodBeforeList.add(method);
            }
        }

        for (Method method : methods) {

            Annotation testAnnotation = method.getDeclaredAnnotation(Test.class);

            if (testAnnotation != null) {
                this.methodTestList.add(method);
            }
        }

        for (Method method : methods) {

            Annotation afterAnnotation = method.getDeclaredAnnotation(After.class);

            if (afterAnnotation != null) {
                this.methodAfterList.add(method);
            }
        }

        for (Method method : methods) {

            Annotation afterAllAnnotation = method.getDeclaredAnnotation(AfterAll.class);

            if (afterAllAnnotation != null) {
                this.methodAfterAllList.add(method);
            }
        }



        this.methodList.addAll(this.methodBeforAllList);
        this.methodTestList.forEach((value) -> {
            this.methodList.add(value);
            this.methodList.addAll(this.methodBeforeList);
            this.methodList.add(value);
            this.methodList.addAll(this.methodAfterList);
            this.methodList.add(value);
        });
        this.methodList.addAll(this.methodAfterAllList);

        int testAnnotationCounter = 0;
        Object instance = null;
        for (Method value : this.methodList) {
            if (value.getAnnotation(BeforeAll.class) != null || value.getAnnotation(AfterAll.class) != null) {

                try {
                    value.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            if(value.getAnnotation(Test.class) != null) {
                if (testAnnotationCounter == 0) {

                    instance = newInstance(value.getDeclaringClass());
                    testAnnotationCounter++;

                } else if (testAnnotationCounter == 1) {
                    try {
                        value.invoke(instance);
                        testAnnotationCounter++;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    instance = null;
                    testAnnotationCounter = 0;
                }
            }

            if(value.getAnnotation(Before.class) != null || value.getAnnotation(After.class) != null) {
                try {
                    value.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private <T> T newInstance(Class<T> aClass) {

        Constructor<T> declaredConstructor = null;
        T instance = null;

        try {
            declaredConstructor = aClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (declaredConstructor != null) {
            try {
                declaredConstructor.setAccessible(true);
                instance = declaredConstructor.newInstance();


            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            } finally {
                declaredConstructor.setAccessible(false);
            }
        }
        return instance;
    }
}
