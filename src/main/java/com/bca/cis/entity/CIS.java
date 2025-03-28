package com.bca.cis.entity;

import com.bca.cis.entity.impl.SecureIdentifiable;
import com.bca.cis.enums.AdminApprovalStatus;
import com.bca.cis.enums.UserType;
import com.bca.cis.validator.annotation.MemberTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cis_data", indexes = {
        @Index(name = "cis_data_secure_id", columnList = "secure_id", unique = true),
})
public class CIS extends AbstractBaseEntity implements SecureIdentifiable {

    @Override
    public String getSecureId() {
        return super.getSecureId();
    }

    @Override
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @MemberTypeEnum
    @Column(name = "type")
    private String accountType = "NOT_MEMBER";

    @Enumerated(EnumType.STRING)
    @Column(name = "type_member")
    private UserType memberType = UserType.SL;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_parent")
    private UserType parentType = UserType.SL;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    // member data
    @Column(name = "member_bank_account", length = 20)
    private String memberBankAccount;

    @Column(name = "member_cin", length = 20)
    private String memberCin;

    @Column(name = "member_account_number", length = 10)
    private String memberAccountNumber;

    @Column(name = "member_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate memberBirthdate;
    // ---

    // parent data
    @Column(name = "parent_bank_account", length = 20, columnDefinition = "varchar(20) default '0'")
    private String parentBankAccount;

    @Column(name = "parent_cin", length = 20)
    private String parentCin;

    @Column(name = "parent_account_number", length = 10)
    private String parentAccountNumber;

    @Column(name = "parent_birthdate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate parentBirthdate;
    // ---

    // other data

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "orders", columnDefinition = "int default 1")
    private Integer orders = 1;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_approval")
    private AdminApprovalStatus statusApproval = AdminApprovalStatus.PENDING;



}
