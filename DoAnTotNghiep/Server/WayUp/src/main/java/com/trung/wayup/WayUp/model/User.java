package com.trung.wayup.WayUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_users", schema = "public")
public class User extends AbstractModel{

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "image")
    private String image;

    @Column(name = "gender")
    private String gender;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "permission")
    private int permission;

    @Column(name = "lock")
    private int lock;

    @Column(name = "target")
    private String target;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "skype")
    private String skype;

    @Column(name = "address")
    private String address;

    @Column(name = "education")
    private String education;

    @Column(name = "experience")
    private String experience;

    @Column(name = "hard_skill")
    private String hard_skill;

    @Column(name = "soft_skill")
    private String soft_skill;

    @Column(name = "info")
    private String info;
}
