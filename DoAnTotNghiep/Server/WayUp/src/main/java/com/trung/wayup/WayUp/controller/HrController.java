package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Hr;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.HrResponse;
import com.trung.wayup.WayUp.response.extend.JobResponse;
import com.trung.wayup.WayUp.service.base.HrService;
import com.trung.wayup.WayUp.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class HrController extends AbstractController {

    private HrService hrService;
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/hrs")
    public ResponseEntity<Response> insertHr(@RequestBody Hr hr){
        HrResponse objectResponse = new HrResponse(hrService.insert(hr));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/hrs/{id_user}")
    public ResponseEntity<Response> searchHr(@PathVariable("id_user") int id_user){
        User user = userService.searchUser(id_user);
        HrResponse objectResponse = new HrResponse(hrService.findHrByUser(user));
        return responseData(objectResponse);
    }
}
