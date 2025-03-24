package com.bca.cis.repository;

import com.bca.cis.entity.CIS;
import com.bca.cis.entity.MobileNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileNumberRepository extends JpaRepository<MobileNumber, Long> {
    List<MobileNumber> findByCis(CIS cis);
}
