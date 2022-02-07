package de.plehr.Exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN, reason="Forbidden")
public class ForbiddenException extends RuntimeException {}