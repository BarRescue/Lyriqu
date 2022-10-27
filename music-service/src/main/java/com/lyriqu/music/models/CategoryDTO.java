package com.lyriqu.music.models;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDTO {

    private String name;

    private String description;

    private MultipartFile file;

}
