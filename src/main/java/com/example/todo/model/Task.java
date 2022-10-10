package com.example.todo.model;

import com.example.todo.Marker;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String task_name;
    @NotNull
    private String task_desc;
    @FutureOrPresent
    private LocalDate task_date;
    @NotNull(groups = Marker.OnCreate.class)
//    Может быть null при удалении
    private Long task_tag;

    public Task(String task_name, String task_desc, LocalDate task_date, Long task_tag) {
        this.task_name = task_name;
        this.task_desc = task_desc;
        this.task_date = task_date;
        this.task_tag = task_tag;
    }


}
