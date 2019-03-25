package com.example.wayupmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSliding extends AbstractModel{

    private int id;
    private String url;
    private Job job;

    public ImageSliding(String url, Job job) {
        this.url = url;
        this.job = job;
    }
}
