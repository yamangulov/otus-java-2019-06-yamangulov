package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.front.FrontendService;
import ru.otus.messagesystem.Message;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class FrontendServer {

    private FrontendService frontendService;

    private int frontendSocketServerPort;

    public FrontendServer(int frontendSocketServerPort, FrontendService frontendService) {
        this.frontendSocketServerPort = frontendSocketServerPort;
        this.frontendService = frontendService;
    }

    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(frontendSocketServerPort)) {
            log.info("Starting frontendServerSocket on port: {}", frontendSocketServerPort);

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

    private void clientHandler(Socket clientSocket) {
        try (ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {

            Message receivedMessage = (Message) inputStream.readObject();

            log.info("Received from {} message ID[{}]", receivedMessage.getFrom(), receivedMessage.getId());

            frontendService.sendFrontMessage(receivedMessage);


        } catch (Exception ex) {
            log.error("error", ex);
        }
    }

}
