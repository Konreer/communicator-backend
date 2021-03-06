package com.app.communicator.dto.securityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {
    @Email(message = "Email must have appropriate format")
    private String email;
    @Size(min = 3, message = "Username should contain at least 3 letters")
    private String username;
    @Size(min = 3, message = "Password should contain at least 3 letters")
    private String password;
    private String repeatedPassword;
    @Size(min = 3, message = "name should contain at least 3 letters")
    private String name;
    @Size(min = 3, message = "surname should contain at least 3 letters")
    private String surname;
}
