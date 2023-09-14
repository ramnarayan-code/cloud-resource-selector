package com.cloud.resource.selector.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RegionNotFoundException extends RuntimeException {

    public RegionNotFoundException(String s) {
        super(s);
    }
}
