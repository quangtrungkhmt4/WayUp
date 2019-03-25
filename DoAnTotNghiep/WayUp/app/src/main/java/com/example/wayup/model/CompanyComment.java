package com.example.wayup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyComment extends AbstractModel{

    private int id_comment;
    private String comment;
    private String created_at;
    private User user;
    private Company company;

    public CompanyComment(String comment, String created_at, User user, Company company) {
        this.comment = comment;
        this.created_at = created_at;
        this.user = user;
        this.company = company;
    }
}
