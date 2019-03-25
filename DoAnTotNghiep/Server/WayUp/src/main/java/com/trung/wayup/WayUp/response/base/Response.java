package com.trung.wayup.WayUp.response.base;

import com.trung.wayup.WayUp.constant.ResponseConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int code;
    private AbstractResponse data;
    private String message;

    public Response(int code, AbstractResponse data) {
        this.code = code;
        this.data = data;
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(int code) {
        this.code = code;
    }

    public Response(ResponseConstant responseConstant) {
        code = responseConstant.getCode();
        message = responseConstant.getMessage();
    }
}
