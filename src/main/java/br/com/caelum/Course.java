package br.com.caelum;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull @Positive
    private Integer estimatedHours;

    @Deprecated
    protected Course() { }

    public Course(String name, Integer estimatedHours) {
        this.name = name;
        this.estimatedHours = estimatedHours;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getEstimatedHours() {
        return estimatedHours;
    }
}
