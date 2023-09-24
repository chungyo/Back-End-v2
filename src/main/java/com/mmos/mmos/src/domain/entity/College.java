package com.mmos.mmos.src.domain.entity;

import com.mmos.mmos.src.domain.dto.university.UniversitySaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collegeIndex;

    @Column
    private String collegeName;

    @OneToMany(mappedBy = "college", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Major> collegeMajors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "universityIndex")
    private University university;

    public College(UniversitySaveRequestDto requestDto, University university) {
        this.collegeName = requestDto.getName();
        this.university = university;

    }
}
