package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.Message;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MessageClient {
    public void sendMessage(Message message, String clientHost, int clientPort) {

        try (Socket clientSocket = new Socket(clientHost, clientPort);
             ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())) {

            outputStream.writeObject(message);
            log.info("Message with ID [{}] is send to {}", message.getId(), message.getTo());

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
