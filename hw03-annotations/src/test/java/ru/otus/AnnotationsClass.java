package ru.otus;

import ru.otus.annotations.*;

public class AnnotationsClass {

    @BeforeAll
    public static void testBeforeAll(){
        System.out.println("Test method before all");
    }

    @BeforeAll
    public static void testBeforeAll2(){
        System.out.println("Test method before all 2");
    }

    @Before
    public void testBefore(){
        System.out.println("Test method before each");
    }

    @Test
    public void test(){
        System.out.println("Test method");
    }

    @Before
    public void testBefore2(){
        System.out.println("Test method before each 2");
    }

    @After
    public void testAfter(){
        System.out.println("Test method after each");
    }

    @After
    public void testAfter2(){
        System.out.println("Test method after each 2");
    }

    @AfterAll
    public static void testAfterAll(){
        System.out.println("Test method after all");
    }

    @AfterAll
    public static void testAfterAll2(){
        System.out.println("Test method after all 2");
    }

    @Test
    public void test2(){
        System.out.println("Test method 2");
    }
}
