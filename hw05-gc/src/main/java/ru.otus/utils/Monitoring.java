package ru.otus.utils;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class Monitoring {
    private int YoungCount = 0;
    private int OldCount = 0;
    private long YoungTime = 0;
    private long OldTime = 0;

    private String GCName;

    public int getYoungCount() {
        return YoungCount;
    }

    public int getOldCount() {
        return OldCount;
    }

    public long getYoungTime() {
        return YoungTime;
    }

    public long getOldTime() {
        return OldTime;
    }

    public String getGCName() {
        if (GCName.contains("G1")) {
            return "G1";
        }
        if (GCName.equals("Copy") || GCName.equals("MarkSweepCompact")) {
            return "Serial GC";
        }
        return "Parallel GC";
    }

    public void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause  = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    if (gcAction.contains("minor")) {
                        YoungCount++;
                        YoungTime += duration;
                    }
                    if (gcAction.contains("major")) {
                        OldCount++;
                        OldTime += duration;
                    }

                    GCName = gcName;

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
