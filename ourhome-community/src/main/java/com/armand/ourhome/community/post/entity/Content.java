package com.armand.ourhome.community.post.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long contentId;

    @Column(name ="media_url", columnDefinition = "TEXT", nullable = false)
    private String mediaUrl;

    @Column(name ="description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaceType placeType;

    @OneToMany(mappedBy = "content", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;

    public void addTag(Tag tag){
        tags.add(tag);
        tag.setContent(this);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
        tag.setContent(null);
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    void setPost(Post post){
        this.post = post;
    }

    @Builder
    public Content(Long contentId, String mediaUrl, String description, PlaceType placeType, List<Tag> tags){
        Assert.notNull(mediaUrl, "mediaUrl은 null 값을 허용하지 않습니다.");
        Assert.notNull(placeType, "placeType은 null 값을 허용하지 않습니다.");
        this.contentId = contentId;
        this.mediaUrl = mediaUrl;
        this.description = description;
        this.placeType = placeType;
        this.tags = tags;
    }
}
