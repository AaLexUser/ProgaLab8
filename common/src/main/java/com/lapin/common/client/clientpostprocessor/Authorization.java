package com.lapin.common.client.clientpostprocessor;

import com.lapin.common.client.Client;
import com.lapin.common.controllers.CommandManager;
import com.lapin.common.controllers.CommandManagerImpl;
import com.lapin.common.controllers.ConsoleManager;
import com.lapin.common.data.User;
import com.lapin.common.utility.Pair;
import com.lapin.di.annotation.Inject;
import com.lapin.di.context.ApplicationContext;
import com.lapin.network.StatusCodes;
import com.lapin.network.log.NetworkLogger;

public class Authorization {
    private User user;
    private Client client;
    private ConsoleManager consoleManager = ApplicationContext.getInstance().getBean(ConsoleManager.class);
    private CommandManager commandManager = ApplicationContext.getInstance().getBean(CommandManager.class);
    private NetworkLogger logger = ApplicationContext.getInstance().getBean(NetworkLogger.class);
    public Authorization(Client client){
        this.client = client;
    }
    public User signIn(String login, String password) throws RuntimeException{
        try {
            return authorization(login, password, "checkUser");
        }catch (RuntimeException e){
            if(e.getMessage().equals("Incorrect input data")){
                throw new RuntimeException("Incorrect username or password");
            }
            else throw e;
        }
    }
    public User signUp(String login, String password) throws RuntimeException{
        try {
            return authorization(login, password, "addUser");
        }catch (RuntimeException e){
            if(e.getMessage().equals("Incorrect input data")){
                throw new RuntimeException("That username is taken. Try another.");
            }
            else throw e;
        }
    }
    private User authorization(String login, String password, String userCommand) throws RuntimeException{
        user = new User(login,password);
        Pair res = commandManager.handle(userCommand, "", user);
        if (((StatusCodes) res.getFirst())
                .equals(StatusCodes.OK)) {
            if (res.getSecond() instanceof Pair) {
                user.setId((Long) ((Pair<?, ?>) res.getSecond()).getSecond());
                client.setUser(user);
                return user;
            }
            else throw new RuntimeException("Server incorrect answer");
        }
        else throw new RuntimeException("Incorrect input data");
    }
}