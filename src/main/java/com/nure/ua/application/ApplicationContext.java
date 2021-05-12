package com.nure.ua.application;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.a_serverSide.Server;
import com.nure.ua.a_serverSide.application.ServerApp;
import com.nure.ua.a_serverSide.serverCommand.SendMessageCommand;
import com.nure.ua.a_serverSide.serverCommand.SignInCommand;
import com.nure.ua.a_serverSide.serverCommand.SignUpCommand;
import com.nure.ua.model.ConnectionPool;
import com.nure.ua.service.MessageService;
import com.nure.ua.service.UserService;
import com.nure.ua.service.impl.MessageServiceImpl;
import com.nure.ua.service.impl.UserServiceImpl;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ApplicationContext {
    private UserService userService;
    private MessageService messageService;
    private ConnectionPool pool;
    private ClientContainer clientContainer;

    public void config() throws Exception {
        try {
            initPool();
            initClientContainer();
            initServices();
            initCommands();
            initServer();
        } catch (SQLException | IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private void initPool() throws SQLException, IOException {

        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/SQL/database.properties"));

        pool = new ConnectionPool(properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password"),
                Integer.parseInt(properties.getProperty("pool.threads")));
    }

    private void initServices() {
        userService = new UserServiceImpl(pool);
        messageService = new MessageServiceImpl(pool);
    }

    private void initClientContainer() {
        clientContainer = new ClientContainer();
    }

    private void initCommands() {
        ServerApp.getCommands().addCommand("sign_in", new SignInCommand(clientContainer, userService, messageService));
        ServerApp.getCommands().addCommand("sign_up", new SignUpCommand(clientContainer, userService, messageService));
        ServerApp.getCommands().addCommand("send_message", new SendMessageCommand(clientContainer, userService, messageService));
    }

    private void initServer(){
        ServerApp.setServer(new Server(clientContainer));
    }
}
