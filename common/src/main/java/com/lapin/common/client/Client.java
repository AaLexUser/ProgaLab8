package com.lapin.common.client;

import com.lapin.common.utility.*;
import com.lapin.di.context.ApplicationContext;
import com.lapin.di.factory.BeanFactory;
import com.lapin.network.ClientType;
import com.lapin.network.StatusCodes;
import com.lapin.network.TCPConnection;
import com.lapin.network.conop.ClientTCPConnection;
import com.lapin.network.listener.ClientListener;
import com.lapin.network.listener.ServerListener;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Client implements Runnable{
    @Getter
    private final ClientType clientType;
    private final HistoryStack historyStack;
    private ServerListener serverListener;
    private Properties properties;
    private File configPath;
    private StatusCodes sc = StatusCodes.OK;
    public Client(File configPath, ServerListener serverListener){
        this(configPath);
        this.serverListener = serverListener;
    }
    public Client(File configPath){
        this.configPath = configPath;
        historyStack = new HistoryStack();
        properties = new Properties();
        try {
            properties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            System.err.println("Не удалось загрузить config");
        }
        if(properties.getProperty("ClientType").equals("Local")){
            clientType = ClientType.LOCAL;
        }else clientType = ClientType.REMOTE;
    }
    public void run(){
        BeanFactory beanFactory = new BeanFactory(ApplicationContext.getInstance());
        ApplicationContext.getInstance().setBeanFactory(beanFactory);
        CommandManager commandManager = new CommandManager(this);
        if (clientType.equals(ClientType.REMOTE)) {
            TCPConnection session = new TCPConnection(new ClientTCPConnection(configPath));
            ClientListener listener = (ClientListener) session.start();
            Client_IO client_io = new Client_Network_IO(listener);
            commandManager.setClientIO(client_io);
        }
        ConsoleManager consoleManager = new ConsoleManager(commandManager);
        FileManager.setConsoleManager(consoleManager);
        while (!sc.equals(StatusCodes.EXIT_CLIENT)){
            consoleManager.interactiveMode();
        }
    }
    public void setStatusCode(StatusCodes sc){
        if(sc.equals(StatusCodes.EXIT_SERVER) && serverListener != null){
            serverListener.setServerStatus(sc);
            this.sc = StatusCodes.EXIT_CLIENT;
        }
        else this.sc = sc;
    }
    public HistoryStack getHistory(){
        return historyStack;
    }
}
