package com.trung.wayup.WayUp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseConstant {
    SUCCESS(0, "Success"),
    FAILED(1, "Failed"),
    BAD_REQUEST(2, "Bad request"),
    NO_RESULT(0, "No result");
    private int code;
    private String message;
}
