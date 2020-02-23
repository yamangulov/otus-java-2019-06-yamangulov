package ru.otus;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@Slf4j
public class App {

    private static final File currentDir_frontend_1 = new File("./hw16/hw16-frontend/src/main/java");

    private static final File currentDir_frontend_2 = new File("./hw16/hw16-frontend/src/main/java");

    private static final File currentDir_db_1 = new File("./hw16/hw16-dbserver/src/main/java");

    private static final File currentDir_db_2 = new File("./hw16/hw16-dbserver/src/main/java");

    private static final File currentDir_ms = new File("./hw16/hw16-message-server/src/main/java");

    private static Map<String, String> frontend_map_1 = new HashMap<>();
    private static Map<String, String> frontend_map_2 = new HashMap<>();
    private static Map<String, String> db_map_1 = new HashMap<>();
    private static Map<String, String> db_map_2 = new HashMap<>();
    private static Map<String, String> ms_map = new HashMap<>();

    public static void main(String[] args) throws IOException {


        frontend_map_1.put("server.f1.port", "8080");
        frontend_map_1.put("messageServer.f1.host", "localhost");
        frontend_map_1.put("messageServer.f1.port", "8010");
        frontend_map_1.put("databaseServiceClientName.f1", "databaseService");
        frontend_map_1.put("frontendSocketServer.port.f1", "8012");
        frontend_map_1.put("frontendServiceClientName.f1", "frontendService");

        frontend_map_2.put("server.f2.port", "8090");
        frontend_map_2.put("messageServer.f2.host", "localhost");
        frontend_map_2.put("messageServer.f2.port", "8010");
        frontend_map_2.put("databaseServiceClientName.f2", "databaseService");
        frontend_map_2.put("frontendSocketServer.f2.port", "8022");
        frontend_map_2.put("frontendServiceClientName.f2", "frontendService");

        db_map_1.put("server.db1.port", "0");
        db_map_1.put("messageServer.db1.host", "localhost");
        db_map_1.put("messageServer.db1.port", "8010");
        db_map_1.put("databaseServiceClientName.db1", "databaseService");
        db_map_1.put("dbServer.db1.port", "8014");

        db_map_2.put("server.db2.port", "0");
        db_map_2.put("messageServer.db2.host", "localhost");
        db_map_2.put("messageServer.db2.port", "8010");
        db_map_2.put("databaseServiceClientName.db2", "databaseService");
        db_map_2.put("dbServer.db2.port", "8024");

        ms_map.put("server.ms.port", "0");
        ms_map.put("messageServer.ms.port", "8010");
        ms_map.put("frontend.ms.host", "localhost");
        ms_map.put("frontend.ms.name", "frontendService");
        ms_map.put("frontend.ms.port", "8012");
        ms_map.put("dbserver.ms.name", "databaseService");
        ms_map.put("dbserver.ms.host", "localhost");
        ms_map.put("dbserver.ms.port", "8014");




        startServer(currentDir_ms, ms_map);
//        startServer(currentDir_db_1, db_map_1);
//        startServer(currentDir_db_2, db_map_2);
//        startServer(currentDir_frontend_1, frontend_map_1);
//        startServer(currentDir_frontend_2, frontend_map_2);
    }

    private static void startServer(File currentDir, Map<String, String> currentMap) throws IOException {
        var processBuilder = new ProcessBuilder("java", "ru.otus.App")
                .inheritIO()
                .directory(currentDir);

//        Map<String, String> envProp = processBuilder.environment();
//
//        for (Map.Entry<String, String> entry: currentMap.entrySet()) {
//            envProp.put(entry.getKey(), entry.getValue());
//            System.setProperty(entry.getKey(), entry.getValue());
//        }

        //log.info("Map envProp {}", envProp);
        //log.info("System env {}", System.getenv());

        processBuilder.start();
    }
}
