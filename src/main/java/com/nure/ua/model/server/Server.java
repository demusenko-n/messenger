package com.nure.ua.model.server;

import com.nure.ua.model.entity.Message;
import com.nure.ua.model.entity.User;
import com.nure.ua.model.exception.ServiceException;
import com.nure.ua.service.MessageService;
import com.nure.ua.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Server extends Thread {
    private final MessageService messageService;
    private final UserService userService;

    public Server(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    private class SocketProcessor implements Runnable {
        private final Socket client;

        SocketProcessor(Socket client) {
            this.client = client;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            try (client;
                 var in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 var out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true)) {


                System.out.println("User connected");
                out.println("Welcome!");

                var dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                while (true) {
                    String word = in.readLine();
                    System.out.println(word);

                    User user = userService.getUserByLogin("login1");

                    Message newMessage = new Message(null, word, user, user, 1, 0,
                            LocalDateTime.now());

                    messageService.createMessage(newMessage);
                    out.println("Message received in " + dtf.format(LocalDateTime.now()));
                }
            } catch (IOException | ServiceException e) {
                System.err.println(e.getMessage());
            } finally {
                System.out.println("User disconnected");
            }

        }
    }

    public void testFunc() throws Exception {
        var users = new User[]{
                new User(1, "Oleksandr", "ch", "p"),
                new User(2, "Andrew", "us", "p"),
                new User(3, "Mykyta", "de", "p"),
                new User(4, "Anna", "ts", "p"),
                new User(5, "Lesya", "mo", "p"),
                new User(6, "Maksym", "ko", "p"),
                new User(7, "Maksym", "ku", "p")

        };


        var messages = new Message[]{
                new Message(1, null, "1-1", users[0], users[0], 0, 0, LocalDateTime.now()),
                new Message(2, null, "1-2", users[0], users[1], 0, 0, LocalDateTime.now()),
                new Message(3, null, "1-3", users[0], users[2], 0, 0, LocalDateTime.now()),
                new Message(4, null, "1-4", users[0], users[3], 0, 0, LocalDateTime.now()),
                new Message(5, null, "1-5", users[0], users[4], 0, 0, LocalDateTime.now()),
                new Message(6, null, "1-5.2", users[0], users[4], 0, 0, LocalDateTime.now()),
                new Message(7, null, "5-1", users[4], users[0], 0, 0, LocalDateTime.now()),
        };

//        for (var u : users) {
//            userService.createUser(u);
//        }
//        for (var m : messages) {
//            messageService.createMessage(m);
//        }


        var am = messageService.getAllMessagesWithUser(users[0]);

        for (var entry : am.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());


        System.out.println(am);

    }


    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
//        try {
//            testFunc();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try (var server = new ServerSocket(4004)) {
            System.out.println("Server is running.");
            while (!server.isClosed()) {
                new Thread(new SocketProcessor(server.accept())).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Server has been closed.");
        }
    }
}
