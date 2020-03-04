package ru.otus;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Slf4j
public class App {

    private static final File currentDir_front = new File("./hw16/hw16-frontend/target");
    private static final File currentDir_db = new File("./hw16/hw16-dbserver/target");
    private static final File currentDir_ms = new File("./hw16/hw16-message-server/target");

    private static final String startfile_front = "hw16-frontend-1.0-SNAPSHOT-spring-boot.jar";
    private static final String startfile_db = "hw16-dbserver-1.0-SNAPSHOT-spring-boot.jar";
    private static final String startfile_ms = "hw16-message-server-1.0-SNAPSHOT-spring-boot.jar";

    public static void main(String[] args) throws IOException, InterruptedException {

        startServer(currentDir_ms, startfile_ms, "default", "");
        startServer(currentDir_db, startfile_db, "db1", "");
        startServer(currentDir_db, startfile_db, "db2", "");
        startServer(currentDir_front, startfile_front, "front1", "--server.port=8080");
        startServer(currentDir_front, startfile_front, "front2", "--server.port=8090");
    }

    private static void startServer(File currentDir, String startfile, String profile, String optionalPort) throws IOException, InterruptedException {

        log.info( "begin {}", startfile );
        var processBuilder = new ProcessBuilder("java", "-Dspring.profiles.active=" + profile, "-jar", startfile, optionalPort)
                .inheritIO()
                .directory(currentDir);

        var process = processBuilder.start();

        try (var reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) ); ) {
            String line;
            while ( ( line = reader.readLine() ) != null ) {
                log.info( "proc out: {}", line );
            }
        }

        log.info( "waiting for process..." );
        process.waitFor( 1, TimeUnit.MINUTES );

        log.info( "end {}", startfile );
    }
}
