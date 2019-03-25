package com.example.wayup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends AbstractModel{

    private int id_noti;
    private String skill;
    private String email;

    public Notification(String skill, String email) {
        this.skill = skill;
        this.email = email;
    }
}
