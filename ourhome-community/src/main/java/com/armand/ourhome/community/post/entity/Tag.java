package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.community.post.util.Checking;
import com.armand.ourhome.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long tagId;

    @Column(name="name", nullable = false, length = 30)
    private String name;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    void setContent(Content content){
        this.content = content;
    }

    @Builder
    public Tag(Long tagId, String name, Content content){
        Assert.notNull(name, "name은 null 값을 허용하지 않습니다.");
        Checking.validLength(0,30,"tag name", name);
        this.tagId = tagId;
        this.name = name;
        this.content = content;
    }

}
