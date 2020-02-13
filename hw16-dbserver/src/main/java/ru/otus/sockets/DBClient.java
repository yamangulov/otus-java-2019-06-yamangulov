package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.messagesystem.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DBClient {

    @Value("${messageServer.host}")
    private String messageServerHost;
    @Value("${messageServer.port}")
    private int messageServerPort;

    public void sendMessage(Message message) {

        try (Socket clientSocket = new Socket(messageServerHost, messageServerPort);
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

            outputStream.flush();
            outputStream.writeObject(message);
            log.info("Message with ID [{}] is send to {} via MessageServer", message.getId(), message.getTo());

            sleep();
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
