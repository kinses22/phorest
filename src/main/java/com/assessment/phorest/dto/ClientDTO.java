package com.assessment.phorest.dto;

import com.assessment.phorest.entity.Appointment;
import com.assessment.phorest.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO implements CsvDataDTO {

    public ClientDTO(String id, List<Appointment> appointments,
                     String firstName, String secondName,
                     String email, String phone, Gender gender, boolean banned) {
        this.id = id;
        this.appointments = mapAppointmentsToDTOs(appointments);
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.banned = banned;
    }

    private List<AppointmentDTO> mapAppointmentsToDTOs(List<Appointment> appointments) {
        return appointments.stream()
                .map(appointment -> new AppointmentDTO(appointment.getId().toString(), appointment.getStartTime(),
                        appointment.getEndTime()))
                .collect(Collectors.toList());
    }

    public ClientDTO(String id) {
        this.id = id;
    }

    @Size(max = 36, message = "Client id can't exceed 36 characters")
    @NotEmpty
    private String id;

    private List<AppointmentDTO> appointments;

    @Size(max = 20, min = 1, message = "Customer first name can't exceed 40 characters or be empty")
    private String firstName;

    @Size(max = 20, min = 1,  message = "Customer second name can't exceed 40 characters or be empty")
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
