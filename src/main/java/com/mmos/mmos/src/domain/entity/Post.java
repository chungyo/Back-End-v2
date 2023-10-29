package com.mmos.mmos.src.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mmos.mmos.src.domain.dto.request.PostSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIndex;

    @Column
    private String postTitle;

    @Lob
    @Column
    private String postContents;

    @Column
    private String postImage;

    @Column
    private Boolean postIsNotice = true;

    @Column
    private String postWriterName;

    @Column
    private Long postWriterIndex;

    @Column
    private Timestamp postCreatedAt;

    @Column
    private Timestamp postUpdatedAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "studyIndex")
    private Study study = null;

    public Post(PostSaveRequestDto postSaveRequestDto, User user, Study study, Timestamp postCreatedAt, Timestamp postUpdatedAt) {
        this.postIsNotice = postSaveRequestDto.getIsNotice();
        this.postTitle = postSaveRequestDto.getPostTitle();
        this.postContents = postSaveRequestDto.getPostContents();
        this.postImage = postSaveRequestDto.getPostImage();
        this.postWriterIndex = user.getUserIndex();
        this.postWriterName = user.getUserName();
        this.study = study;
        this.postCreatedAt = postCreatedAt;
        this.postUpdatedAt = postUpdatedAt;
    }

    public void setUpdatedAt(Timestamp now) {
        this.postUpdatedAt = now;
    }

    public void updateWriter(User user) {
        this.postWriterIndex = user.getUserIndex();
        this.postWriterName = user.getUserName();
    }

    public void updateTitle(String title) {
        this.postTitle = title;
    }

    public void updateContents(String contents) {
        this.postContents = contents;
    }

    public void updateImage(String image) {
        this.postImage = image;
    }
}