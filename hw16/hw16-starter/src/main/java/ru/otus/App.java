package ru.otus;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class App {

    private static final File currentDir_front = new File("./hw16/hw16-frontend/target");
    private static final File currentDir_db = new File("./hw16/hw16-dbserver/target");
    private static final File currentDir_ms = new File("./hw16/hw16-message-server/target");

    private static final String startfile_front = "hw16-frontend-1.0-SNAPSHOT-spring-boot.jar";
    private static final String startfile_db = "hw16-dbserver-1.0-SNAPSHOT-spring-boot.jar";
    private static final String startfile_ms = "hw16-message-server-1.0-SNAPSHOT-spring-boot.jar";

    //имена сервисов здесь везде одинаковые, и их можно было бы захардкорить в соответствующих файлах application.properties, но я вынес их сюда, чтобы показать, что их можно передавать - это полезно, когда нужно будет зачем-то сделать их разными
    private static final String[] params_front_1 = "8080 localhost 8010 databaseService 8012 frontendService".split(" ");
    private static final String[] params_front_2 = "8090 localhost 8010 databaseService 8022 frontendService".split(" ");
    private static final String[] params_db_1 = "databaseService1 8014".split(" ");
    private static final String[] params_db_2 = "databaseService2 8024".split(" ");
    private static final String[] params_ms = "".split(" ");//здесь параметры не нужны, просто для единообразия входных параметров startServer()

    public static void main(String[] args) throws IOException, InterruptedException {

        startServer(currentDir_ms, startfile_ms, params_ms);
        startServer(currentDir_db, startfile_db, params_db_1);
        startServer(currentDir_db, startfile_db, params_db_2);
        startServer(currentDir_front, startfile_front, params_front_1);
        startServer(currentDir_front, startfile_front, params_front_2);
    }

    private static void startServer(File currentDir, String startfile, String[] params) throws IOException, InterruptedException {

        log.info( "begin {}", startfile );
        var processBuilder = new ProcessBuilder("java", "-jar", startfile, Arrays.toString(params))
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
