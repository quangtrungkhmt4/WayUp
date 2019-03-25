package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Profile;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.CountProfileResponse;
import com.trung.wayup.WayUp.response.extend.ProfileResponse;
import com.trung.wayup.WayUp.response.extend.ProfilesResponse;
import com.trung.wayup.WayUp.service.base.ProfileService;
import com.trung.wayup.WayUp.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class ProfileController extends AbstractController{

    private ProfileService profileService;
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/profiles")
    public ResponseEntity<Response> getFirst(@RequestParam("id_user") int id_user){
        User user = userService.searchUser(id_user);
        Profile profile = profileService.getFirstByUser(user);
        return responseData(new ProfileResponse(profile));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/profiles")
    public ResponseEntity<Response> insertProfile(@RequestBody Profile profile){
        Profile profile1 = profileService.insert(profile);
        return responseData(profile1==null?new BooleanResponse(false):new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/profiles")
    public ResponseEntity<Response> deleteProfile(@RequestParam("id_profile") int id_profile){
        profileService.delete(id_profile);
        BooleanResponse objectResponse = new BooleanResponse(true);
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/profiles/{id_user}")
    public ResponseEntity<Response> countProfile(@PathVariable("id_user") int id_user){
        User user = userService.searchUser(id_user);
        List<Profile> profiles = profileService.findAllByUser(user);
        return responseData(new CountProfileResponse(profiles.size()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/profiles/all/{id_user}")
    public ResponseEntity<Response> getAllProfile(@PathVariable("id_user") int id_user){
        User user = userService.searchUser(id_user);
        List<Profile> profiles = profileService.findAllByUser(user);
        return responseData(new ProfilesResponse(profiles));
    }

}
