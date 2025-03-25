package com.bca.cis.repository;

import com.bca.cis.entity.CIS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckCustomerRepository extends JpaRepository<CIS, Long> {

    CIS findByMemberBankAccount(String cardNumber);

    boolean existsByMemberBankAccount(String cardNumber);

    CIS findByParentBankAccount(String cisCustomerNumber);


    CIS findByMemberAccountNumber(String memberAccountNumber);

    CIS findByMemberCin(String memberCin);
}