package com.programming.frank.quotation_microservice.model;

import com.programming.frank.quotation_microservice.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_quotation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Column(name = "client_id")
    private Long clientId; // ID from client microservice

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_email")
    private String clientEmail;

    private String subject;

    private Status status;

    private BigDecimal total;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private Set<QuotationDetail> details = new HashSet<>();
}
