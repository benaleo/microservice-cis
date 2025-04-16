package com.bca.cis.repository;

import com.bca.cis.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByOtp(String otp);

    Otp findByOtpAndPhone(String otp, String phone);

    List<Otp> findAllByOtpAndPhoneAndIsValidIsTrueOrderByIdDesc(String otp, String pan);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Otp o
            SET o.isValid = false
            WHERE o.phone = :pan
            """)
    void updateExistingToIsValidFalse(String pan);
}
