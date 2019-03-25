package com.example.wayup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hr extends AbstractModel {

    private int id_hr;
    private User user;
    private Company company;
    private int code_confirm;

    public Hr(User user, Company company, int code_confirm) {
        this.user = user;
        this.company = company;
        this.code_confirm = code_confirm;
    }
}
