package com.boubyan.api.dto;

import com.boubyan.api.model.Student;
import com.boubyan.api.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StudentDto {
    private long studentId;
    @NotNull(message = "FirstName is required")
    private String firstName;
    @NotNull(message = "LastName is required")
    private String lastName;


    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    public UserDto getUserDto() {
        UserDto dto = new UserDto();
        dto.setPassword(password);
        dto.setUsername(email);
        dto.setRole("Student");
        return dto;
    }

    public Student buildStudent() {
        Student student = new Student();
//        student.setUserId(userId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        return student;
    }
}
