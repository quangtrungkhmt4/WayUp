package com.trung.wayup.WayUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tbl_jobs", schema = "public")
public class Job extends AbstractModel {

    @Id
    @Column(name = "id_job", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_job;

    @Column(name = "title")
    private String title;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "information")
    private String information;

    @Column(name = "address")
    private String address;

    @Column(name = "join_date")
    private String join_date;

    @Column(name = "estimatetime")
    private int estimatetime;

    @Column(name = "lock")
    private int lock;

    @Column(name = "salary")
    private int salary;

    @Column(name = "skills")
    private String skills;

    @Column(name = "fast_info")
    private String fast_info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company")
    @NotNull
    @JsonIgnoreProperties(value = {"handler","hibernateLazyInitializer","fieldHandler"})
    private Company company;

}
