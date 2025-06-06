package com.programming.frank.client_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private String address;
}
