package com.example.todo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;
    @NotBlank
    @NotEmpty
    private String tag_name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn (name="task_tag")
    private List<Task> tasks;

    public Tag(String tag_name) {
        this.tag_name = tag_name;
    }

    public Tag(Long tag_id, String tag_name) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
    }
}
