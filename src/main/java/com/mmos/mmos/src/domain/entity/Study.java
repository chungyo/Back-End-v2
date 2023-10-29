package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mmos.mmos.src.domain.dto.request.StudySaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyIndex;

    @JsonManagedReference
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserStudy> studyUserstudies = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> studyPosts = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Project> studyProjects = new ArrayList<>();

    @Column
    private Integer studyMemberLimit;

    @Column
    private Integer studyMemberNum = 0;

    @Column
    private String studyName;

    @Column
    private String studyMemo;

    @Column
    private Boolean studyIsVisible = true;

    @Column
    private Boolean studyIsComplete = false;

    public void addProject(Project project) {
        this.studyProjects.add(project);
    }

    public void updateStudyName(String studyName) {
        this.studyName = studyName;
    }

    public void updateStudyMemo(String studyMemo) {
        this.studyMemo = studyMemo;
    }

    public void updateStudyIsComplete(){this.studyIsComplete = true;}

    public Study(StudySaveRequestDto requestDto) {
        this.studyName = requestDto.getName();
        this.studyMemberLimit = requestDto.getMemberLimit();
    }

    public void addPost(Post post){
        this.studyPosts.add(post);
    }
    public void addUserStudy(UserStudy userStudy){
        this.studyUserstudies.add(userStudy);
    }

    public void plusMemberNum() {
        this.studyMemberNum++;
    }
    public void minusMemberNum() {
        this.studyMemberNum--;
    }
}
