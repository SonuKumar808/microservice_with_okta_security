package com.hrs.user_service.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private Date date;
    private String message;
    private boolean success;
    private HttpStatus status;
}
