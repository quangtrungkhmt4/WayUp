package com.example.wayupmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends AbstractModel {

    private int id_profile;

    private String url;

    private User user;

    public Profile(String url, User user) {
        this.url = url;
        this.user = user;
    }

}
