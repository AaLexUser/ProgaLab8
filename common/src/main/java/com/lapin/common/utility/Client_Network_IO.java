package com.lapin.common.utility;


import com.lapin.common.data.User;
import com.lapin.common.network.objimp.RequestCommand;
import com.lapin.network.listener.ClientListener;
import com.lapin.network.obj.NetObj;
import com.lapin.network.obj.ResponseBodyKeys;

import java.io.Serializable;

public class Client_Network_IO implements Client_IO{
    ClientListener listener;
    public Client_Network_IO(ClientListener listener){
        this.listener = listener;
    }
    @Override
    public Pair handle(String command, String arg, Serializable argObj, User user) {
        return Response2Pair(listener.handle(createRequest(command,arg,argObj, user)));
    }

    @Override
    public Pair handle(String command, String arg, User user) {
        return handle(command,arg,null, user);
    }

    private RequestCommand createRequest(String command, String arg, Serializable argObj, User user){
        return new RequestCommand(command,arg,argObj,user);
    }
    private Pair Response2Pair(NetObj response){
        return new Pair (response.getBody().get(ResponseBodyKeys.STATUS_CODE),response.getBody().get(ResponseBodyKeys.ANSWER));
    }
}
