package com.bca.cis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "mobile_number", indexes = {
        @Index(name = "mobile_number_secure_id", columnList = "secure_id", unique = true),
})
public class MobileNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false, unique = true, columnDefinition = "char(36)")
    private String secureId = UUID.randomUUID().toString();

    @Column(name = "phone", nullable = false, columnDefinition = "varchar(20)")
    private String phone;

    @Column(name = "code", columnDefinition = "varchar(3)")
    private String code = "62";

    @Column(name = "status", columnDefinition = "varchar(3)")
    private String status = "0";

    @ManyToOne
    @JoinColumn(name = "cis_id")
    private CIS cis;

    public MobileNumber(String phone, String code, String status, CIS cis) {
        this.phone = phone;
        this.code = code;
        this.status = status;
        this.cis = cis;
    }
}
