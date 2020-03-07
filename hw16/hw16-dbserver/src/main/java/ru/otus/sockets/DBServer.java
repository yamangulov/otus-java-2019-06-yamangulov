package ru.otus.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MsClient;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;


@Slf4j
public class DBServer {

    private MsClient dbServerMsClient;
    private int dbServerPort;


    public DBServer(MsClient dbServerMsClient, int dbServerPort) {
        this.dbServerMsClient = dbServerMsClient;
        this.dbServerPort = dbServerPort;
    }

    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(dbServerPort)) {
            log.info("Starting DBServer on port: {}", dbServerPort);

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
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())) {

            Message receivedMessage = (Message) ois.readObject();

            log.info("Received from {} message ID[{}]", receivedMessage.getFrom(), receivedMessage.getId());

            dbServerMsClient.handle(receivedMessage);


        } catch (Exception ex) {
            log.error("error", ex);
        }
    }


}

