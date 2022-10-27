package com.lyriqu.music.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyriqu.music.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Category extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String logoPath;

    private String description;

    public Category() {}

    @ManyToMany
    @Where(clause = "active = true")
    private Set<Music> songs = new HashSet<>();
}
