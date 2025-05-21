package com.projet.testing.vehicule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomHandlerException {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorModel>> handleFieldErrors(MethodArgumentNotValidException mav){
    List<ErrorModel> errorModelList=new ArrayList<>();
    ErrorModel errorModel;
    List<FieldError> fieldErrors=mav.getBindingResult().getFieldErrors();
    for(FieldError fe:fieldErrors){
      errorModel = new ErrorModel();
      errorModel.setCode(fe.getField());
      errorModel.setMessage(fe.getDefaultMessage());
      errorModelList.add(errorModel);
    }
    return new ResponseEntity<>(errorModelList, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<List<ErrorModel>> handleBusinessException(BusinessException bex){
    System.err.println("Une Exception coté client a été levée");
    return new ResponseEntity<>(bex.getErrorModels(), HttpStatus.BAD_REQUEST);
  }
}
