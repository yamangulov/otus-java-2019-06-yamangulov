package ru.otus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.sockets.FrontendServer;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class App implements CommandLineRunner {
    private FrontendServer frontendServer;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    //для запуска frontend server точно в конце запуска App, когда все, что нужно, уже проинициализировано, смотри https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
    @Override
    public void run(String... args) throws Exception {
        frontendServer.go();
    }
}
