package com.assessment.phorest.dto;

import com.assessment.phorest.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDTO implements CsvDataDTO {

    public ClientDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Client id can't exceed 36 characters")
    @NotEmpty
    private String id;

    @Size(max = 20, message = "Customer name can't exceed 40 characters")
    private String firstName;

    @Size(max = 20, message = "Customer name can't exceed 40 characters")
    private String secondName;

    @Email(message = "invalid email")
    private String email;

    @Size(max = 15, message = "Customer phone number can't exceed 15 characters")
    private String phone;

    @NotNull(message = "Gender must be specified")
    private Gender gender;

    @NotNull(message = "Client banned flag must be true or false")
    private boolean banned;

}
