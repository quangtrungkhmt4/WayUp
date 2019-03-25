package com.example.wayupmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vote extends AbstractModel{

    private int id_user_rank;

    private int point;

    private User user;

    private Company company;

    public Vote(int point, User user, Company company) {
        this.point = point;
        this.user = user;
        this.company = company;
    }
}
