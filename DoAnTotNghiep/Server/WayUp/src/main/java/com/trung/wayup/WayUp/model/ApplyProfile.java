package com.trung.wayup.WayUp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_apply", schema = "public")
public class ApplyProfile extends AbstractModel {

    @Id
    @Column(name = "id_apply", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_apply;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private String created_at;

    @Column(name = "status")
    private int status;

    @Column(name = "sent_mail")
    private int sent_mail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_job")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private Job job;

    @Column(name = "profile")
    private String profile;
}
