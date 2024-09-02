package Volt.example.Volt.Shared.Dtos;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditModel {
    @CreatedDate
    @Column(nullable = false, updatable = false,name = "creation_date")
    private LocalDateTime createdByUser;
    @LastModifiedDate
    @Column(updatable = false,name = "modified_date")
    private LocalDateTime creationDate;
}
