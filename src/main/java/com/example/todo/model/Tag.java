package com.example.todo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "tag_id")
@Data
@NoArgsConstructor
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String tag_name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task_tag")
    private List<Task> tasks;

    public Tag(String tag_name) {
        this.tag_name = tag_name;
    }

    public Tag(Long tag_id, String tag_name) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
    }

    @Override // Список задач не включаем в сравнение тегов
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!Objects.equals(tag_id, tag.tag_id)) return false;
        return tag_name.equals(tag.tag_name);
    }

    @Override
    public int hashCode() {
        int result = tag_id != null ? tag_id.hashCode() : 0;
        result = 31 * result + tag_name.hashCode();
        return result;
    }

    public void addTask(Task task) {
        if (tasks == null) tasks = new LinkedList<>();
        tasks.add(task);
    }

}
