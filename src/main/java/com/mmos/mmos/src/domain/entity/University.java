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
    private Long universityIndex;

    @Column
    private String universityName;

    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<User> universityUsers = new ArrayList<>();

    public void addUser(User user){
        this.universityUsers.add(user);
    }
}
