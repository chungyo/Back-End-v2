package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityIndex;

    @Column
    private String universityName;

    @JsonManagedReference
    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<College> universityColleges = new ArrayList<>();

    public University(String universityName) {
        this.universityName = universityName;
    }

    public void addCollege(College college){
        this.universityColleges.add(college);
    }
}
