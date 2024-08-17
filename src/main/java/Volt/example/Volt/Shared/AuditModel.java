package Volt.example.Volt.Shared;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class AuditModel {
    @Column(nullable = false)
    @Setter @Getter
    private String createdByUser;
    @Column(nullable = false)
    @Setter @Getter
    private Date creationDate;
    @Column
    @Setter @Getter
    private String modifiedByUser;
    @Column
    @Setter @Getter
    private String lastModifiedDate;
}
