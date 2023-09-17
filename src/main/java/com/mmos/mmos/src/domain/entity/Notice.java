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
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notice_index;

    @Column
    private boolean notice_status = true;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;

    @OneToMany(mappedBy = "notice")
    private List<Post> notice_posts = new ArrayList<>();

    public Notice(Study study) {
        this.study = study;
    }

    public void addPost(Post post) {
        this.notice_posts.add(post);
    }
}