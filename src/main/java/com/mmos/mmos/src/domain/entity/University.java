package com.mmos.mmos.src.domain.entity;

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
    private Long university_index;

    @Column
    private String university_name;

    @Column
    private Boolean university_status = true;

    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<User> university_users = new ArrayList<>();

    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Major> university_majors = new ArrayList<>();

    public void addUser(User user){
        this.university_users.add(user);
    }
}
