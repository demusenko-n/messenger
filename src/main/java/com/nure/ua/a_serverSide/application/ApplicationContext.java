package com.nure.ua.a_serverSide.application;

import com.nure.ua.a_serverSide.serverCommand.FindUserCommand;
import com.nure.ua.a_serverSide.serverCommand.SendMessageCommand;
import com.nure.ua.a_serverSide.serverCommand.SignInCommand;
import com.nure.ua.a_serverSide.serverCommand.SignUpCommand;
import com.nure.ua.a_serverSide.model.ConnectionPool;
import com.nure.ua.a_serverSide.service.MessageService;
import com.nure.ua.a_serverSide.service.UserService;
import com.nure.ua.a_serverSide.service.impl.MessageServiceImpl;
import com.nure.ua.a_serverSide.service.impl.UserServiceImpl;

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
            initCommands();
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


    private void initCommands() {
        Server.getCommands().addCommand("sign_in", new SignInCommand(userService, messageService));
        Server.getCommands().addCommand("sign_up", new SignUpCommand(userService, messageService));
        Server.getCommands().addCommand("send_message", new SendMessageCommand(userService, messageService));
        Server.getCommands().addCommand("find_user", new FindUserCommand(userService));
    }

}
