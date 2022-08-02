package com.example.todo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;
    @NotBlank
    @NotEmpty
    private String tag_name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task_tag")
    private List<Task> tasks;

}
