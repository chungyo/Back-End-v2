package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyIndex;

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserStudy> studyUserstudies = new ArrayList<>();

    @Column
    @ColumnDefault("null")
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> studyPosts = new ArrayList<>();

    @Column
    private Integer studyMemberLimit;

    @Column
    private String studyName;

    @Column
    private Boolean studyIsVisible = true;

    @Column
    private Boolean studyIsComplete = false;

    public void updateStudy_name(String study_name) {
        this.studyName = study_name;
    }

    @Builder
    public Study(String study_name, Integer study_member_limit) {
        this.studyName = study_name;
        this.studyMemberLimit = study_member_limit;
    }
    public void addPost(Post post){
        this.studyPosts.add(post);
    }
    public void addUserStudy(UserStudy userStudy){
        this.studyUserstudies.add(userStudy);
    }
}