package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class FrontendClient {
    @Value("${messageServer.port}")
    private int messageServerPort;
    @Value("${messageServer.host}")
    private String messageServerHost;


    public void sendMessage(Message message) {
        try {
            try (Socket clientSocket = new Socket(messageServerHost, messageServerPort);
                 ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

                outputStream.flush();
                outputStream.writeObject(message);
                log.info("Message with ID [{}] is send to MessageServer", message.getId());

                sleep();

            }

        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
