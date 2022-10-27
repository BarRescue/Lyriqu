package com.lyriqu.monitorservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyriqu.monitorservice.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import java.util.Date;

@Entity
@Getter @Setter
public class Monitor extends BaseEntity {

    @JsonIgnore
    private String musicId;

    @JsonIgnore
    private String userId;

    private Status status;

    private String reason;

    private Date created;

    private Date dateApplied;

    public Monitor() {}
}
