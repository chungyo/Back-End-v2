package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long majorIndex;

    @Column
    private Long majorName;

    @OneToMany(mappedBy = "major", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<User> majorUsers;

    public void addUser(User user){
        this.majorUsers.add(user);
    }
}
