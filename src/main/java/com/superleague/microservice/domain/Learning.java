package com.superleague.microservice.domain;


import javax.persistence.*;

import java.io.Serializable;

import com.superleague.microservice.domain.enumeration.Status;

/**
 * A Learning.
 */
@Entity
@Table(name = "learning")
public class Learning implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Learning status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Learning)) {
            return false;
        }
        return id != null && id.equals(((Learning) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Learning{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
