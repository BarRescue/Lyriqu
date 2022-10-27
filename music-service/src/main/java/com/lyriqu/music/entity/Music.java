package com.lyriqu.music.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyriqu.music.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter
public class Music {

    @Id
    @Type(type="uuid-char")
    private UUID id;

    private String name;

    private String path;

    @CreatedDate
    private Date createdDate = new Date();

    private boolean active = false;

    private Status status;

    private String reason;

    private String thumbnail;

    private UUID userId;

    public Music() {}

    @ManyToMany(mappedBy = "songs")
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getSongs().add(this);
    }
}
