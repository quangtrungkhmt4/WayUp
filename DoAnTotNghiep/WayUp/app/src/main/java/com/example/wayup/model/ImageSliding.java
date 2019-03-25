package com.example.wayup.model;

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
}
