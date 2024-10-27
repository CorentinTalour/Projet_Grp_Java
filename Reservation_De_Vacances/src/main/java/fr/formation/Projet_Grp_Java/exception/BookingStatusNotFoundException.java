package fr.formation.Projet_Grp_Java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingStatusNotFoundException extends RuntimeException {

}