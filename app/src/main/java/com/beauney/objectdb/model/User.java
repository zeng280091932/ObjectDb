package com.beauney.objectdb.model;


import com.beauney.objectdb.annotation.DbField;
import com.beauney.objectdb.annotation.DbTable;

import java.io.Serializable;

/**
 * @author zengjiantao
 * @since 2020-07-30
 */
@DbTable("t_user")
public class User implements Serializable {

    @DbField("t_username")
    private String username;

    @DbField("t_password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
