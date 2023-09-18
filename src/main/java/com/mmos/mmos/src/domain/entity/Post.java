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
    private Boolean post_is_notice = true;

    @Column
    private String post_writer;

    @Column
    private Timestamp post_created_at;

    @Column
    private Timestamp post_updated_at;

    @Column
    private Boolean post_status = true;

    @ManyToOne
    @JoinColumn(name = "notice_index")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "promotion_index")
    private Promotion promotion;

    public Post(PostSaveRequestDto postSaveRequestDto, Notice notice, String userIdx) {
        this.post_title = postSaveRequestDto.getPostTitle();
        this.post_contents = postSaveRequestDto.getPostContents();
        this.post_image = postSaveRequestDto.getPostImage();
        this.post_is_notice = postSaveRequestDto.getIsNotice();
        this.post_writer = userIdx;
        this.notice = notice;
    }

    public Post(PostSaveRequestDto postSaveRequestDto, Promotion promotion, String userIdx) {
        this.post_title = postSaveRequestDto.getPostTitle();
        this.post_contents = postSaveRequestDto.getPostContents();
        this.post_image = postSaveRequestDto.getPostImage();
        this.post_is_notice = postSaveRequestDto.getIsNotice();
        this.post_writer = post_writer;
        this.promotion = promotion;
    }
}