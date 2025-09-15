package com.example.school_management_backend.dto;

import com.example.school_management_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OgrenciResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String classroom;
    private String schoolNumber;
    private Role role;  // rol bilgisi de görünebilir
}
