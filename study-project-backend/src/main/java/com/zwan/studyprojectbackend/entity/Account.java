package com.zwan.studyprojectbackend.entity;

import lombok.Data;

@Data
public class Account {

    private Integer id;
    private String email;
    private String username;
    private String password;

}
