package com.mmos.mmos.src.domain.entity;

import com.mmos.mmos.src.domain.dto.post.PostSaveRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_index;

    @Column
    private String post_title;

    @Lob
    @Column
    private String post_contents;

    @Column
    private String post_image;

    @Column
    private boolean post_is_notice = true;

    @Column
    private String post_writer;

    @Column
    private Timestamp post_created_at;

    @Column
    private Timestamp post_updated_at;

    @Column
    private boolean post_status = true;

    @ManyToOne
    @JoinColumn(name = "study_index")
    private Study study = null;

    public Post(PostSaveRequestDto postSaveRequestDto,  String userName, Study study) {
        this.post_is_notice = postSaveRequestDto.getIsNotice();
        this.post_title = postSaveRequestDto.getPostTitle();
        this.post_contents = postSaveRequestDto.getPostContents();
        this.post_image = postSaveRequestDto.getPostImage();
        this.post_writer = userName;
        this.study = study;
    }
}