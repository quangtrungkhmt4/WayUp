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
@Table(name = "tbl_notifications", schema = "public")
public class Notification extends AbstractModel{

    @Id
    @Column(name = "id_noti", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_noti;

    @Column(name = "skill")
    private String skill;

    @Column(name = "email")
    private String email;
}
