package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.model.Vote;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.ExistsEmailResponse;
import com.trung.wayup.WayUp.response.extend.UserResponse;
import com.trung.wayup.WayUp.response.extend.VoteResponse;
import com.trung.wayup.WayUp.response.extend.VotesResponse;
import com.trung.wayup.WayUp.service.base.UserService;
import com.trung.wayup.WayUp.service.base.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class VoteController extends AbstractController{

    private VoteService voteService;
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/votes")
    public ResponseEntity<Response> register(@RequestBody Vote vote){
        VoteResponse objectResponse = new VoteResponse(voteService.insert(vote));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/votes/{id_user}")
    public ResponseEntity<Response> isExistsEmail(@PathVariable("id_user") int id_user){
        User user = userService.searchUser(id_user);
        return responseData(new VotesResponse(voteService.findAllByUser(user)));
    }
}
