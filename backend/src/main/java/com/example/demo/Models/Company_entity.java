package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class Company_entity extends User_entity {

    @Column(name = "company_name")
    private String companyName;

    private String description;

    private String site;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;
}