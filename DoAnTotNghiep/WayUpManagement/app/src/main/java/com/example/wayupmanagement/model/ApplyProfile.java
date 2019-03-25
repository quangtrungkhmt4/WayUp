package com.example.wayupmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyProfile extends AbstractModel{

    private int id_apply;

    private String email;

    private String name;

    private String created_at;

    private int status;

    private int sent_mail;

    private User user;

    private Job job;

    private String profile;

    public ApplyProfile(String email, String name, String created_at, int status, int sent_mail, User user, Job job, String profile) {
        this.email = email;
        this.name = name;
        this.created_at = created_at;
        this.status = status;
        this.sent_mail = sent_mail;
        this.user = user;
        this.job = job;
        this.profile = profile;
    }
}
