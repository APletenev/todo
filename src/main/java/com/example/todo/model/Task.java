package com.example.todo.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_id;
    @NotBlank
    @NotEmpty
    private String task_name;
    private String task_desc;
    private LocalDate task_date;
    private Long task_tag;
}
