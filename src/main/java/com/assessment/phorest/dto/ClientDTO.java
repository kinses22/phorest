package com.assessment.phorest.dto;

import com.assessment.phorest.enumeration.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO implements CsvDataDTO {

    public ClientDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Client id can't exceed 36 characters")
    @NotEmpty
    private String id;

    private List<AppointmentDTO> appointments;

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
    private Gender gender;

    @NotNull(message = "Client banned flag must be true or false")
    private boolean banned;

}
