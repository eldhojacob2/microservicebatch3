package com.superleague.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Participant.
 */
@Entity
@Table(name = "participant")
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "emp_id", nullable = false)
    private String empId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JsonIgnoreProperties(value = "participants", allowSetters = true)
    private Batch sprint;

    @ManyToOne
    @JsonIgnoreProperties(value = "participants", allowSetters = true)
    private Learning learning;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public Participant empId(String empId) {
        this.empId = empId;
        return this;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public Participant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Participant email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Batch getSprint() {
        return sprint;
    }

    public Participant sprint(Batch batch) {
        this.sprint = batch;
        return this;
    }

    public void setSprint(Batch batch) {
        this.sprint = batch;
    }

    public Learning getLearning() {
        return learning;
    }

    public Participant learning(Learning learning) {
        this.learning = learning;
        return this;
    }

    public void setLearning(Learning learning) {
        this.learning = learning;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participant)) {
            return false;
        }
        return id != null && id.equals(((Participant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participant{" +
            "id=" + getId() +
            ", empId='" + getEmpId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
