package com.kantilever.bffwebwinkel.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;

    public static MessageResponse of(String messsage) {
        return new MessageResponse(messsage);
    }
}
