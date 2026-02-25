package com.skyline.exception;

public class BookingCancelException extends RuntimeException {
    public BookingCancelException(String message){
        super(message);
    }
}
