package com.bca.cis.repository;

import com.bca.cis.entity.CIS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckCustomerRepository extends JpaRepository<CIS, Long> {

    CIS findByMemberBankAccount(String cardNumber);

    boolean existsByMemberBankAccount(String cardNumber);

    CIS findByParentBankAccount(String cisCustomerNumber);
}