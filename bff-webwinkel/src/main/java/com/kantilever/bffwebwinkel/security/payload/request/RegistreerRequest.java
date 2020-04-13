package com.kantilever.bffwebwinkel.security.payload.request;

import java.util.Set;
import javax.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistreerRequest {
    @NotNull
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 40)
    private String wachtwoord;

    @NotNull
    @NotBlank
    private String initialen;

    @NotNull
    @NotBlank
    private String achternaam;

    @NotEmpty
    private Set<String> klantSoort;
}
