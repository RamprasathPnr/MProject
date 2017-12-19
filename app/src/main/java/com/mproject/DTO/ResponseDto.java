package com.mproject.DTO;

/**
 * Created by root on 28/10/17.
 */
public class ResponseDto {


    private int statusCode;

    private String errorMessage;

    private String response;



    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }




}
