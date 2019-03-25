package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Image;
import com.trung.wayup.WayUp.model.Job;

import java.util.List;

public interface ImageService extends Service<Image> {

    List<Image> findAllByJob(Job job);
}
