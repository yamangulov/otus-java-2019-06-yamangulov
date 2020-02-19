package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageSystem;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Slf4j
public class MessageServer {
    @Value("${messageServer.port}")
    private int messageServerPort;
    private MessageSystem messageSystem;

    @Autowired
    public MessageServer(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(messageServerPort)) {
            log.info("Starting MessageServer on port: {}", messageServerPort);
            while (!Thread.currentThread().isInterrupted()) {
                log.info("Waiting for client connection...");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

    //так как здесь считывается объект из потока, то тестирование при помощи отправки строки через телнет в терминале не пройдет, отправляемые данные должны соответствовать принимаемым, то есть писать в сокет мы тоже должны, например, методом writeObject там, где идет отправка (и в тестах тоже)
    private void clientHandler(Socket clientSocket) {
        Message receivedMessage;
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {

            receivedMessage = (Message) inputStream.readObject();

            log.info("Received from {}: message ID[{}] ", receivedMessage.getFrom(), receivedMessage.getId());

            messageSystem.newMessage(receivedMessage);

        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

}
