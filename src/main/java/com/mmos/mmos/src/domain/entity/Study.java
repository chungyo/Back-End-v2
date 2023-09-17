package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long study_index;

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserStudy> study_userstudies;

    @Column
    @ColumnDefault("null")
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notice> study_notices;

    @Column
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Promotion> study_promotions;


    @Column
    private Integer study_member_limit;

    @Column
    private String study_name;

    @Column
    private boolean study_is_visible = true;

    @Column
    private boolean study_status = true;

    public void updateStudy_name(String study_name) {
        this.study_name = study_name;
    }
    public void updateStudy_status(){
        this.study_status = false;
    }
    @Builder
    public Study(String study_name, Integer study_member_limit) {
        this.study_name = study_name;
        this.study_member_limit = study_member_limit;
    }

    public void addUserStudy(UserStudy userStudy){
        this.study_userstudies.add(userStudy);
    }
}