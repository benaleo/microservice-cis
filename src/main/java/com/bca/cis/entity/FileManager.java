package com.bca.cis.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FileManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    @Column(name = "file_data", columnDefinition = "text")
    private byte[] fileData;

    @Lob
    @Column(name = "qr_code", columnDefinition = "text")
    private byte[] qrCode;

    private long fileSize;

    private LocalDateTime uploadedAt;

}
