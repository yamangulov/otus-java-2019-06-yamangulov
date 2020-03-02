package ru.otus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.sockets.MessageServer;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class App implements CommandLineRunner {
    private MessageServer messageServer;

    public static void main(String... args) {
        SpringApplication.run(App.class, args);
    }

    //для запуска message server точно в конце запуска App, когда все, что нужно, уже проинициализировано, смотри https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
    @Override
    public void run(String... args) throws Exception {
        messageServer.go();
    }
}
