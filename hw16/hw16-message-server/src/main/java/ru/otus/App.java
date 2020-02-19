package ru.otus;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.sockets.MessageServer;

import java.util.Collections;

@SpringBootApplication
@AllArgsConstructor
public class App implements CommandLineRunner {
    private MessageServer messageServer;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    //для запуска message server точно в конце запуска App, когда все, что нужно, уже проинициализировано, смотри https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
    @Override
    public void run(String... args) throws Exception {
        messageServer.go();
    }
}
