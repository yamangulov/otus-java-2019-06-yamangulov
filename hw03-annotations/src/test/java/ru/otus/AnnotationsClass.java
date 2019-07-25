package ru.otus;

import ru.otus.annotations.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnotationsClass {

    @BeforeAll
    public static void testBeforeAll(){
        System.out.println("Test method before all");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @BeforeAll
    public static void testBeforeAll2(){
        System.out.println("Test method before all 2");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @Before
    public void testBefore(){
        System.out.println("Test method before each");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @Test
    public void test(){
        System.out.println("Test method");
        // для проверки, что исключение в одном тесте не прерывает последующее тестирование
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @Before
    public void testBefore2(){
        System.out.println("Test method before each 2");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @After
    public void testAfter(){
        System.out.println("Test method after each");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @After
    public void testAfter2(){
        System.out.println("Test method after each 2");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @AfterAll
    public static void testAfterAll(){
        System.out.println("Test method after all");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @AfterAll
    public static void testAfterAll2(){
        System.out.println("Test method after all 2");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }

    @Test
    public void test2(){
        System.out.println("Test method 2");
        //assertEquals(1 + 1, 3, "Interrupting test on error");
    }
}
