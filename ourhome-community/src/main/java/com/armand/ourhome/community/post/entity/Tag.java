package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.community.comment.entity.Comment;
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

    /**
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", referencedColumnName = "id", nullable = false)
    private Content content;
    **/

    @Builder
    public Tag(Long tagId, String name){
        Assert.notNull(name, "name은 null 값을 허용하지 않습니다.");
        validLength(0,30,"tag name", name);
        this.tagId = tagId;
        this.name = name;
    }

    private void validLength(int min, int max, String targetFieldName, String target){
        int length = target.length();
        if (length <= min) throw new IllegalArgumentException("{}은(는) {}초과의 자리수만을 허용합니다.".formatted(targetFieldName, min));
        if (max < length) throw new IllegalArgumentException("{}은(는) {}미만의 자리수만을 허용합니다.".formatted(targetFieldName, min));
    }
}
