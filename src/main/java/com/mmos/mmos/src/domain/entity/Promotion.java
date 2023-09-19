package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/*홍보게시판*/
@Entity
@Getter
@NoArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotion_index;

    @Column
    private Boolean promotion_status = true;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study;

    @OneToMany(mappedBy = "promotion")
    private List<Post> promotion_posts = new ArrayList<>();

    public Promotion(Study study) {
        this.study = study;
    }

    public void addPost(Post post) {
        this.promotion_posts.add(post);
    }
}