package com.kantilever.bffwebwinkel.security.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InlogRequest {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String wachtwoord;
}
