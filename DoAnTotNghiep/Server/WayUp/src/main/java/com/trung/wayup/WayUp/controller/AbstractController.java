package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.constant.ResponseConstant;
import com.trung.wayup.WayUp.response.base.AbstractResponse;
import com.trung.wayup.WayUp.response.base.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {
    protected static final ResponseEntity<Response> RESPONSE_FAILED = new ResponseEntity<>(new Response(ResponseConstant.FAILED), HttpStatus.OK);
    protected static final ResponseEntity<Response> RESPONSE_SUCCESS = new ResponseEntity<>(new Response(ResponseConstant.SUCCESS), HttpStatus.OK);

    protected static ResponseEntity<Response> responseData(AbstractResponse response) {
        return new ResponseEntity<>(new Response(ResponseConstant.SUCCESS.getCode(), response), HttpStatus.OK);
    }


    protected ResponseEntity<Response> successRequest(AbstractResponse data) {
        Response response = new Response(ResponseConstant.SUCCESS.getCode(), data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected ResponseEntity<Response> successRequest() {
        return successRequest(null);
    }

    protected ResponseEntity<Response> badRequest(String message) {
        Response response = new Response(ResponseConstant.BAD_REQUEST.getCode(), message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    protected ResponseEntity<Response> serverError() {
//        Response response = new Response(ResponseConstant.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

}
