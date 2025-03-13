package com.bca.cis.repository;

import com.bca.cis.entity.FileManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepository extends JpaRepository<FileManager, Long> {

}