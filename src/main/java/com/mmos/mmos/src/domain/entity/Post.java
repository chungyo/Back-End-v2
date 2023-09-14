package com.mmos.mmos.src.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column
    private String post_contents;

    @Column
    private String post_image;

    @Column
    @ColumnDefault("true")
    private boolean post_is_notice;

    @Column
    private String post_writer;

    @Column
    private Timestamp post_created_at;

    @Column
    private Timestamp post_updated_at;

    @Column
    @ColumnDefault("true")
    private boolean post_status;

    @ManyToOne
    @JoinColumn(name = "notice_index")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "promotion_index")
    private Promotion promotion;

}