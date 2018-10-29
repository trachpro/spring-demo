package springmvc.demo.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springmvc.demo.models.ErrorResponse;
import springmvc.demo.models.SuccessResponse;

public class Response {

    public static ResponseEntity<String> getSuccessMessage(Object data, String message, HttpStatus status) {

        SuccessResponse successResponse = new SuccessResponse(message, data);

        return new ResponseEntity<>(Converts.convertModelToJson(successResponse).toString(), status);
    }

    public static ResponseEntity<String> getSuccessObject(SuccessResponse successResponse, HttpStatus status) {

        return new ResponseEntity<>(Converts.convertModelToJson(successResponse).toString(), status);
    }


    public static ResponseEntity<String> getErrorMessage(String message, HttpStatus status) {

        ErrorResponse errorResponse = new ErrorResponse(message);

        return new ResponseEntity<>(Converts.convertModelToJson(errorResponse).toString(), status);
    }

    public static ResponseEntity<String> getErrorObject(ErrorResponse errorResponse, HttpStatus status) {

        return new ResponseEntity<>(Converts.convertModelToJson(errorResponse).toString(), status);
    }
}