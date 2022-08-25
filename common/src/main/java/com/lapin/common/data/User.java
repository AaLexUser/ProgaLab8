package com.lapin.common.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class User implements Serializable {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public User(long id, String username){
        this.id = id;
        this.username = username;
    }
}
