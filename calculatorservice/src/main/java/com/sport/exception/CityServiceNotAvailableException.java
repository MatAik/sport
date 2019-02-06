package com.sport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE) // SERVICE_UNAVAILABLE = 503
public class CityServiceNotAvailableException extends RuntimeException {
}
