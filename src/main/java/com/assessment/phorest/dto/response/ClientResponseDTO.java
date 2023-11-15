package com.assessment.phorest.dto.response;

import com.assessment.phorest.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientResponseDTO {

    private String firstName;
    private String secondName;
    private String email;
    private String phone;
    private Gender gender;
    private Boolean banned;

}
