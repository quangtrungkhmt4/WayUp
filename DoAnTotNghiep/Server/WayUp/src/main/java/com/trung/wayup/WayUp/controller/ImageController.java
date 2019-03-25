package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Image;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.ImagesResponse;
import com.trung.wayup.WayUp.response.extend.JobResponse;
import com.trung.wayup.WayUp.response.extend.JobsResponse;
import com.trung.wayup.WayUp.service.base.ImageService;
import com.trung.wayup.WayUp.service.base.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ImageController extends AbstractController {

    private ImageService imageService;
    private JobService jobService;

    @RequestMapping(method = RequestMethod.GET, value = "/images")
    public ResponseEntity<Response> findImage(@RequestParam("id_job") int id_job){
        Job job = jobService.searchJobWithId(id_job);
        List<Image> images = imageService.findAllByJob(job);
        return responseData(new ImagesResponse(images));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/images")
    public ResponseEntity<Response> insertImage(@RequestBody Image image){
        Image img = imageService.insert(image);
        return responseData(img==null?new BooleanResponse(false):new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/images")
    public ResponseEntity<Response> deleteImage(@RequestParam("id_image") int id_image){
        imageService.delete(id_image);
        BooleanResponse objectResponse = new BooleanResponse(true);
        return responseData(objectResponse);
    }
}
