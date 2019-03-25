package com.example.wayup.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id_user;

    private String email;

    private String password;

    private String name;

    private String phone;

    private String image;

    private String gender;

    private String created_at;

    private int permission;

    private int lock;

    private String target;

    private String birthday;

    private String skype;

    private String address;

    private String education;

    private String experience;

    private String hard_skill;

    private String soft_skill;

    private String info;
}
