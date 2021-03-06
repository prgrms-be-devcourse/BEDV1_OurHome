package com.armand.ourhome.community.post.entity;

import com.armand.ourhome.community.post.util.Checking;
import com.armand.ourhome.domain.base.BaseEntity;
import com.armand.ourhome.domain.user.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;


    @Column(name="title", nullable = false, length = 50)
    private String title;

    @Column(name="square_type")
    @Enumerated(EnumType.STRING)
    private SquareType squareType;

    @Column(name="residential_type")
    @Enumerated(EnumType.STRING)
    private ResidentialType residentialType;

    @Column(name="style_type")
    @Enumerated(EnumType.STRING)
    private StyleType styleType;

    @Column(name="viewCount")
    @ColumnDefault("0")
    private int viewCount;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> contentList = new ArrayList<>();

    public void addContent(final Content content){
        contentList.add(content);
        content.setPost(this);
    }

    public void removeContent(final Content content){
        contentList.remove(content);
        content.setPost(null);
    }

    public void plusViewCount(){
        this.viewCount += 1;
    }

    @Builder
    public Post(Long id,
                String title,
                SquareType squareType,
                ResidentialType residentialType,
                StyleType styleType,
                int viewCount,
                User user,
                List<Content> contentList
                ){
        Assert.notNull(title, "title??? null ?????? ???????????? ????????????.");
        Assert.notNull(user, "????????? ????????? null ?????? ???????????? ????????????.");
        Checking.validLength(0, 50, "post title", title);

        this.id = id;
        this.title = title;
        this.squareType = squareType;
        this.residentialType = residentialType;
        this.styleType = styleType;
        this.viewCount = viewCount;
        this.user = user;
        contentList.forEach( content -> addContent(content)); // ???????????????????????? ???????????? ????????? ??? ???. // ????????? ????????? ????????? ?????????.
    }

    public void updatePost(String title,
                           SquareType squareType,
                           ResidentialType residentialType,
                           StyleType styleType){
        this.title = title;
        this.squareType = squareType;
        this.styleType = styleType;
        this.residentialType = residentialType;
    }

}
