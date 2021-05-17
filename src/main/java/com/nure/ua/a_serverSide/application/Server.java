package com.nure.ua.a_serverSide.application;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.a_serverSide.serverCommand.ServerCommandContainer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final ServerCommandContainer commands;

    static {
        commands = new ServerCommandContainer();
    }

    public static ServerCommandContainer getCommands() {
        return commands;
    }

    public static void main(String[] args) throws Exception {
        new ApplicationContext().config();
        new Server().run();
    }

    //    public void testFunc() throws Exception {
//        var users = new User[]{
//                new User(1, "Oleksandr", "ch", "p"),
//                new User(2, "Andrew", "us", "p"),
//                new User(3, "Mykyta", "de", "p"),
//                new User(4, "Anna", "ts", "p"),
//                new User(5, "Lesya", "mo", "p"),
//                new User(6, "Maksym", "ko", "p"),
//                new User(7, "Maksym", "ku", "p")
//
//        };
//
//
//        var messages = new Message[]{
//                new Message(1, null, "1-1", users[0], users[0], 0, 0, LocalDateTime.now()),
//                new Message(2, null, "1-2", users[0], users[1], 0, 0, LocalDateTime.now()),
//                new Message(3, null, "1-3", users[0], users[2], 0, 0, LocalDateTime.now()),
//                new Message(4, null, "1-4", users[0], users[3], 0, 0, LocalDateTime.now()),
//                new Message(5, null, "1-5", users[0], users[4], 0, 0, LocalDateTime.now()),
//                new Message(6, null, "1-5.2", users[0], users[4], 0, 0, LocalDateTime.now()),
//                new Message(7, null, "5-1", users[4], users[0], 0, 0, LocalDateTime.now()),
//        };
//
////        for (var u : users) {
////            userService.createUser(u);
////        }
////        for (var m : messages) {
////            messageService.createMessage(m);
////        }
//
//
//        var am = messageService.getAllMessagesWithUser(users[0]);
//
//        for (var entry : am.entrySet())
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
//
//
//        System.out.println(am);
//
//    }

    public void run() {
        try (var server = new ServerSocket(4004)) {
            System.out.println("Server is running.");
            while (!server.isClosed()) {
                ClientContainer.addClient(server.accept());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Server has been closed.");
        }
    }

}