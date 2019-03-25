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
@Table(name = "tbl_company_comments", schema = "public")
public class CompanyComment extends AbstractModel{

    @Id
    @Column(name = "id_comment", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_comment;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private String created_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private Company company;

    public CompanyComment(@NotNull User user, @NotNull Company company){
        this.user = user;
        this.company = company;
    }
}
