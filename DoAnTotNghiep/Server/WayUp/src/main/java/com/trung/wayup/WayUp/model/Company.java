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
@Table(name = "tbl_companies", schema = "public")
public class Company extends AbstractModel{

    @Id
    @Column(name = "id_company", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_company;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "member")
    private String member;

    @Column(name = "country")
    private String country;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "image")
    private String image;

    @Column(name = "time_for_work")
    private String time_for_work;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Column(name = "description")
    private String description;

    @Column(name = "over_time")
    private String over_time;

    @Column(name = "title")
    private String title;

}
