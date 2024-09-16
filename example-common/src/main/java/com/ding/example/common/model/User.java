package com.ding.example.common.model;

import java.io.Serializable;

/**
 * @author: Dding
 * @date: 2024/09/16
 **/
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
