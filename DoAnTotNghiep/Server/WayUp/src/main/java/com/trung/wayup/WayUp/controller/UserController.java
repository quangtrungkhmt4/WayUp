package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.ExistsEmailResponse;
import com.trung.wayup.WayUp.response.extend.UserResponse;
import com.trung.wayup.WayUp.response.extend.UsersResponse;
import com.trung.wayup.WayUp.service.base.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class UserController extends AbstractController {

    private UserService userService;


    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public ResponseEntity<Response> login(@RequestParam("email") String email, @RequestParam("pass") String pass){
        User user = userService.findUserByEmailAndPassword(email, pass);
        return responseData(new UserResponse(user));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public ResponseEntity<Response> register(@RequestBody User user){
        UserResponse objectResponse = new UserResponse(userService.insert(user));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{email}")
    public ResponseEntity<Response> isExistsEmail(@PathVariable("email") String email){
        boolean isExists = userService.existsByEmail(email);
        return responseData(new ExistsEmailResponse(isExists));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users")
    public ResponseEntity<Response> updateUser(@RequestBody User user){
        UserResponse objectResponse = new UserResponse(userService.update(user));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users")
    public ResponseEntity<Response> deleteUser(@RequestParam("id_user") int id){
        userService.delete(id);
        BooleanResponse objectResponse = new BooleanResponse(true);
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/get")
    public ResponseEntity<Response> getUser(@RequestParam("permission") int permission){
        return responseData(new UsersResponse(userService.findAllByPermission(permission)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/lock")
    public ResponseEntity<Response> getUserLock(){
        return responseData(new UsersResponse(userService.getLockUser()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/email")
    public ResponseEntity<Response> searchWithMail(@RequestParam("email") String email){
        return responseData(new UserResponse(userService.findUserByEmail(email)));
    }

}
