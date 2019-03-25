package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.ApplyProfile;
import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.*;
import com.trung.wayup.WayUp.service.base.ApplyProfileService;
import com.trung.wayup.WayUp.service.base.JobService;
import com.trung.wayup.WayUp.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ApplyProfileController extends AbstractController {

    private ApplyProfileService applyProfileService;
    private UserService userService;
    private JobService jobService;

    @RequestMapping(method = RequestMethod.POST, value = "/apply")
    public ResponseEntity<Response> insertApply(@RequestBody ApplyProfile applyProfile){
        Job job = jobService.searchJobWithId(applyProfile.getJob().getId_job());
        if (job.getLock() == 1){
            ApplyProfileResponse objectResponse = new ApplyProfileResponse(null);
            return responseData(objectResponse);
        }else {
            ApplyProfileResponse objectResponse = new ApplyProfileResponse(applyProfileService.insert(applyProfile));
            return responseData(objectResponse);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apply/{id_user}")
    public ResponseEntity<Response> findAllByUser(@PathVariable("id_user") int id_user){
        User user = userService.searchUser(id_user);
        List<ApplyProfile> applyProfiles = applyProfileService.findAllByUser(user);
//        StringBuilder builder = new StringBuilder();
//        for (ApplyProfile ap : applyProfiles){
//            builder.append(",");
//            builder.append(ap.getJob().getId_job());
//        }
        return responseData(new ApplyProfilesResponse(applyProfiles));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apply/{id_user}/{id_job}")
    public ResponseEntity<Response> searchApply(@PathVariable("id_user") int id_user,
                                                @PathVariable("id_job") int id_job){
        User user = userService.searchUser(id_user);
        Job job = jobService.searchJobWithId(id_job);
        ApplyProfile applyProfile = applyProfileService.findApplyProfileByUserAndJob(user, job);
        return responseData(applyProfile==null?new BooleanResponse(false):new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apply/search/{id_company}")
    public ResponseEntity<Response> searchApply(@PathVariable("id_company") int id_company){
        List<ApplyProfile> applyProfiles = applyProfileService.searchApplyWothIdCompany(id_company);
        return responseData(new ApplyProfilesResponse(applyProfiles));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/apply")
    public ResponseEntity<Response> updateApply(@RequestBody ApplyProfile applyProfile){
        ApplyProfileResponse objectResponse = new ApplyProfileResponse(applyProfileService.update(applyProfile));
        return responseData(objectResponse);
    }
}
