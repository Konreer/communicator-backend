package com.app.communicator.dto.securityDto;

import lombok.Data;

@Data
public class MessageDto <T>{
    private String authToken;
    private T message;
}
