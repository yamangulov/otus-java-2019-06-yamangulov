package ru.otus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.sockets.DBServer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class App implements CommandLineRunner {
    private DBServer dbServer;

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }

    //для запуска db server точно в конце запуска App, когда все, что нужно, уже проинициализировано, смотри https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
    @Override
    public void run(String... args) throws Exception {
        log.info("args from db start {}", args);
        //по какой-то причине все аргументы при передаче из java -jar пишутся, как первый элемент массива args в виде строки, например, "[databaseService, 8014]", а не раскидываются по отдельным элементам массива args, как я ни бился. Совсем нет времени разбираться, поэтому я просто распарсил строку и переписал свойства в application.properties
        String[] arrArgs = StringUtils.substring(args[0], 1, args[0].length() - 1).split(", ");

        Properties properties = new Properties();
        OutputStream output = null;
        try {

            //при запуске из jar мы находимся в каталоге target запускаемого сервера
            output = new FileOutputStream("../src/main/resources/application.properties");

            //set the properties value
            properties.setProperty("databaseServiceClientName", arrArgs[0]);
            properties.setProperty("dbServer.port", arrArgs[1]);
            //неизменяемые значения тоже нужно перезаписать, иначе затрутся в ноль
            properties.setProperty("messageServer.host", "localhost");
            properties.setProperty("messageServer.port", "8010");
            properties.setProperty("server.port", "0");

            //save properties to project root folder
            properties.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        dbServer.go();
    }
}
