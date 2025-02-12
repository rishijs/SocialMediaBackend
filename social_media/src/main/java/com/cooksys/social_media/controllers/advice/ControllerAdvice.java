package com.cooksys.social_media.controllers.advice;

import com.cooksys.social_media.dtos.ErrorDto;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice(basePackages = {"com.cooksys.social_media.controllers"})
@ResponseBody
public class ControllerAdvice {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorDto handleBadRequestException(BadRequestException badReq, HttpServletRequest request){
        return new ErrorDto(badReq.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorDto handleNotFoundException(HttpServletRequest request, NotFoundException exception){
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAuthorizedException.class)
    public ErrorDto handleNotAuthorizedException(HttpServletRequest request, NotFoundException exception){
        return new ErrorDto(exception.getMessage());
    }

}
