package com.nure.ua.application;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.server.Server;
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

    public void config() throws Exception {
        try {
            initPool();
            initServices();
            initServer();
        } catch (SQLException | IOException ex) {
            throw new Exception();
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

    private void initServer() {
        ServerApp.server = new Server(messageService, userService);
    }
}
