package ru.otus;

import ru.otus.utils.Benchmark;
import ru.otus.utils.Monitoring;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;

/**
 * -Xms512m
 * -Xmx512m
 *
 * -Xlog:gc=debug:file=./hw05-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=20m
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:HeapDumpPath=./logs/dump
 *
 * Garbage Collectors:
 * -XX:+UseSerialGC
 * -XX:+UseParallelGC
 * -XX:+UseG1GC
 */

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        Monitoring monitoring = new Monitoring();
        monitoring.switchOnMonitoring();

        int loopCounter = 1000 * 1000 * 1000;

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        try {
            ObjectName name = new ObjectName("ru.otus:type=Benchmark");
            Benchmark mbean = new Benchmark(loopCounter);
            mbs.registerMBean(mbean, name);
            mbean.runForOutOfMemory();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String logText = "Garbage collector: " + monitoring.getGCName() + " has had\n" + monitoring.getYoungCount() + " young collections numbers in " + monitoring.getYoungTime() + " ms\n" + monitoring.getOldCount() + " old collections numbers in " + monitoring.getOldTime() + " ms\n\n";

            System.out.println(logText);

            try (OutputStream fos = new FileOutputStream("./hw05-gc/logs/report.log", true)) {
                fos.write(logText.getBytes());
            } catch (Exception exeption) {
                exeption.printStackTrace();
            }
        }

    }



}
