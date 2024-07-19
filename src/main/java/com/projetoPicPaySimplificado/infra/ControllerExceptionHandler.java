package com.projetoPicPaySimplificado.infra;

import com.projetoPicPaySimplificado.dtos.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicateEntry(DataIntegrityViolationException e){
        ExceptionDto exceptionDto = new ExceptionDto("Usuário ja cadastrado","400");
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threatEntityNotFound404(EntityNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception e){
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(),"500");
        return ResponseEntity.internalServerError().body(exceptionDto);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity threatForbiddenException(Exception e){
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(),"403");
        return new ResponseEntity<>(exceptionDto,HttpStatus.FORBIDDEN);
    }
}
