package com.mmos.mmos.src.domain.entity;

import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
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
    private boolean postIsNotice = true;

    @Column
    private String postWriter;

    @Column
    private Timestamp postCreatedAt;

    @Column
    private Timestamp postUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "studyIndex")
    private Study study = null;

    public Post(PostSaveRequestDto postSaveRequestDto,  String userName, Study study) {
        this.postIsNotice = postSaveRequestDto.getIsNotice();
        this.postTitle = postSaveRequestDto.getPostTitle();
        this.postContents = postSaveRequestDto.getPostContents();
        this.postImage = postSaveRequestDto.getPostImage();
        this.postWriter = userName;
        this.study = study;
    }


    public void updateTitle(String newTitle) {
        this.postTitle = newTitle;
    }

    public void updateContents(String newContents){
        this.postContents = newContents;
    }

    public void updateImage(String newImage){
        this.postImage = newImage;
    }
}