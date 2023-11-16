package com.assessment.phorest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDTO {

    @Size(max = 20, min = 1, message = "Customer first name can't exceed 40 characters or be empty")
    private String firstName;

    @Size(max = 20, min = 1, message = "Customer second name can't exceed 40 characters or be empty")
    private String secondName;

    @Email(message = "invalid email")
    @NotEmpty(message = "Email can not be null or empty")
    private String email;

    @Size(max = 15, message = "Customer phone number can't exceed 15 characters")
    private String phone;

    @NotNull
    private String gender;

    @NotNull(message = "Client banned flag must be true or false")
    private Boolean banned;

}
