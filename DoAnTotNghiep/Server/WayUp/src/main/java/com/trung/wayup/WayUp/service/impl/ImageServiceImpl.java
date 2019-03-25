package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Image;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.repository.ImageRepository;
import com.trung.wayup.WayUp.service.base.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> findAllByJob(Job job) {
        return imageRepository.findAllByJob(job);
    }

    @Override
    public Image insert(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image update(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(int id) {
        imageRepository.deleteById(id);
    }

    @Override
    public Collection<Image> gettAll() {
        return null;
    }
}
