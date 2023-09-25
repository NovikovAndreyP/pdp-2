package org.example.common.dto;

import lombok.Getter;

@Getter
public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
