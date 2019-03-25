package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Image;
import com.trung.wayup.WayUp.model.Job;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image, Integer> {

    List<Image> findAllByJob(Job job);
}
