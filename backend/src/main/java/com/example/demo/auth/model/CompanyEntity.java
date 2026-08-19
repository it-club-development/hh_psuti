package com.example.demo.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
public class CompanyEntity extends user {

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