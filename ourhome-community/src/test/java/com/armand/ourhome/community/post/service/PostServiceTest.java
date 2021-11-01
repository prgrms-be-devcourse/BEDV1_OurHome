package com.armand.ourhome.community.post.service;

import com.armand.ourhome.community.post.dto.*;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.entity.Tag;
import com.armand.ourhome.community.post.repository.ContentRepository;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.post.repository.TagRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostUpdate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/10/29.
 */

@Transactional(readOnly = true)
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostService postService;


    private User userSaved;
    private Long userId;

    @BeforeEach
    void setUp(){
        userSaved = userRepository.save(User.builder()
                        .email("test@email.com")
                        .password("1223")
                        .nickname("화이팅!!")
                        .description("모두의 집 개발자")
                        .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                        .build());
        userId = userSaved.getId();

    }
    @AfterEach
    void tearDown(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 정보를 통해서 post 정보를 추출할 수 있다.")
    void findByUser(){
        //When

        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build()))
                .build());
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build()))
                .build());

        List<Post> postList =postRepository.findAllByUser(userSaved, Pageable.ofSize(5).withPage(0));

        //Then
        assertThat(postList.size(), is(2));
        assertThat(postList.get(0).getContentList().size(), is(1));
        assertThat(postList.get(0).getContentList().get(0).getMediaUrl(), is("/post/picture-APARTMENT.jpg"));


    }

    @Test
    @DisplayName("저장된 게시물을 추출할 수 있다.")
    void findAll(){
        //Given
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build()))
                .build());
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build()))
                .build());

        //When
        List<PostDto> postDtoList = postService.getAll(Pageable.ofSize(5).withPage(0));

        //Then
        assertThat(postDtoList.size(), is(2));
    }

    @Test
    @DisplayName("저장된 게시물을 거주형태로 검색하여 추출할 수 있다.")
    void getAllByResidentialType(){
        //Given
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build()))
                .build());
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build()))
                .build());

        //When
        List<PostDto> postDtoList = postService.getAllByResidentialType(ResidentialType.APARTMENT, Pageable.ofSize(5).withPage(0));

        //Then
        assertThat(postDtoList.size(), is(1));
        assertThat(postDtoList.get(0).getResidentialType(), is(ResidentialType.APARTMENT));
    }


    @Test
    @DisplayName("저장된 게시물에서 공간 형태로 검색하여 추출할 수 있다. (가정: 1개의 content에서 1post 해당하는 것과 2개의 content에서 2post 해당할 때)")
    void getAllByPlaceTypeWith2Post2Content(){
        //Given
        postRepository.saveAndFlush(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build(),
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE122.jpg")
                                .description("DETACHED_HOUCEt121 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("추천")
                                                .build()))
                                .build()))
                .build());
        postRepository.saveAndFlush(Post.builder()
                .title("우리 집 입니다.")
                .squareType(SquareType.SIZE_20_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build()))
                .build());

        //When
        //List<PostDto> postDtoList = postService.getAllByPlaceType(PlaceType.BATHROOM, Pageable.ofSize(5).withPage(0));

        //Then
        //assertThat(postDtoList.size(), is(2));
    }
    @Test
    @DisplayName("저장된 게시물을 공간 형태로 검색하여 추출할 수 있다.(가정: 1개의 content에서 1post 해당할 때)")
    void getAllByPlaceTypeWith1Post1Content(){
        //Given
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build()))
                .build());
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build()))
                .build());

        //When
        //List<PostDto> postDtoList = postService.getAllByPlaceType(PlaceType.BATHROOM, Pageable.ofSize(5).withPage(0));

        //Then
        //assertThat(postDtoList.size(), is(1));
    }
    @Test
    @DisplayName("저장된 게시물을 공간 형태로 검색하여 추출할 수 있다.(가정: 2개의 content에서 1post 해당할 때)")
    void getAllByPlaceTypeWith1Post2Content(){
        //Given
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.APARTMENT)
                .styleType(StyleType.ASIAN_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-APARTMENT.jpg")
                                .description("APARTMENT 설명란")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("APARTMENT")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 APARTMENT")
                                                .build()))
                                .build()))
                .build());
        postRepository.save(Post.builder()
                .title("우리 집")
                .squareType(SquareType.SIZE_10_PYEONG)
                .residentialType(ResidentialType.DETACHED_HOUCE)
                .styleType(StyleType.NORDIC_STYPE)
                .user(userSaved)
                .contentList(List.of(
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCE.jpg")
                                .description("DETACHED_HOUCEt 설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE 거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCE")
                                                .build()))
                                .build(),
                        Content.builder()
                                .mediaUrl("/post/picture-2DETACHED_HOUCEWOW.jpg")
                                .description("DETACHED_HOUCEt WOW설명란")
                                .placeType(PlaceType.BATHROOM)
                                .tags(List.of(
                                        Tag.builder()
                                                .name("DETACHED_HOUCE WOW거실")
                                                .build(),
                                        Tag.builder()
                                                .name("깨끗 DETACHED_HOUCEWOW")
                                                .build()))
                                .build()))
                .build());

        //When
        //List<PostDto> postDtoList = postService.getAllByPlaceType(PlaceType.BATHROOM, Pageable.ofSize(5).withPage(0));

        //Then
        //assertThat(postDtoList.size(), is(1));
    }
    @Test
    @DisplayName("저장된 게시물을 테그로 검색하여 추출할 수 있다.")
    void getAllByTag(){

    }


    @Test
    @DisplayName("하나의 게시글에 하나의 내용과 하나의 테그를 저장할 수 있다.")
    void savePost1Content1Tag1(){
        //Given
        var postDto = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .userId(userId)
                .contentList(List.of(ContentDto.builder()
                        .description("집 안 내용입니다.")
                        .placeType(PlaceType.LIVINGROOM)
                        .tags(List.of(TagDto.builder()
                                .name("아파트")
                                .build()))

                        .build()))
                .build();

        //When
        var dataSaved = postService.save(postDto, List.of("/post/postpicture.jpg"));

        //Then
        assertThat(postRepository.count(), is(1L));
        assertThat(tagRepository.count(), is(1L));

        Post postSaved = postRepository.findAll().get(0);
        assertThat(postSaved.getSquareType(), is(SquareType.SIZE_10_PYEONG));
        assertThat(postSaved.getContentList().get(0).getMediaUrl(), is("/post/postpicture.jpg"));
        assertThat(postSaved.getContentList().get(0).getTags().get(0).getName(), is("아파트"));
    }

    @Test
    @DisplayName("하나의 게시글에 여러 개의 내용과 각 내용별 하나의 테그를 저장할 수 있다.")
    void Post1ContentMultiTag1(){
        //Given
        var postDto = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .userId(userId)
                .contentList(List.of(ContentDto.builder()
                        .description("집 안 내용입니다.")
                        .placeType(PlaceType.LIVINGROOM)
                        .tags(List.of(TagDto.builder()
                                .name("내부")
                                .build()))

                        .build(),
                        ContentDto.builder()
                                .description("집 밖 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                        .name("외관")
                                        .build()))
                                .build()))
                .build();

        //When
        var dataSaved = postService.save(postDto, List.of("/post/postpicture_1.jpg", "/post/postpicture_2.jpg"));

        //Then
        assertThat(postRepository.count(), is(1L));
        assertThat(tagRepository.count(), is(2L));
        assertThat(contentRepository.count(), is(2L));

        Post postSaved = postRepository.findAll().get(0);

        assertThat(postSaved.getSquareType(), is(SquareType.SIZE_10_PYEONG));
        assertThat(postSaved.getContentList().get(0).getMediaUrl(), is("/post/postpicture_1.jpg"));
        assertThat(postSaved.getContentList().get(0).getTags().get(0).getName(), is("내부"));

        assertThat(postSaved.getContentList().get(1).getMediaUrl(), is("/post/postpicture_2.jpg"));
        assertThat(postSaved.getContentList().get(1).getTags().get(0).getName(), is("외관"));
    }

    @Test
    @DisplayName("하나의 게시글에 여러 개의 내용과 여러 개의 테그를 저장할 수 있다.")
    void Post1ContentMultiTagMulti(){
        //Given
        var postDto = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .userId(userId)
                .contentList(List.of(ContentDto.builder()
                                .description("집 안 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                        .name("내부")
                                        .build(),
                                        TagDto.builder()
                                                .name("벽면")
                                                .build()))

                                .build(),
                        ContentDto.builder()
                                .description("집 밖 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                        .name("외관")
                                        .build(),
                                        TagDto.builder()
                                                .name("옆면")
                                                .build()))
                                .build()))
                .build();

        //When
        var dataSaved = postService.save(postDto, List.of("/post/postpicture_1.jpg", "/post/postpicture_2.jpg"));

        //Then
        assertThat(postRepository.count(), is(1L));
        assertThat(tagRepository.count(), is(4L));
        assertThat(contentRepository.count(), is(2L));

        Post postSaved = postRepository.findAll().get(0);

        assertThat(postSaved.getSquareType(), is(SquareType.SIZE_10_PYEONG));
        assertThat(postSaved.getContentList().get(0).getMediaUrl(), is("/post/postpicture_1.jpg"));
        assertThat(postSaved.getContentList().get(0).getTags().get(0).getName(), is("내부"));
        assertThat(postSaved.getContentList().get(0).getTags().get(1).getName(), is("벽면"));

        assertThat(postSaved.getContentList().get(1).getMediaUrl(), is("/post/postpicture_2.jpg"));
        assertThat(postSaved.getContentList().get(1).getTags().get(0).getName(), is("외관"));
        assertThat(postSaved.getContentList().get(1).getTags().get(1).getName(), is("옆면"));
    }

    @Test
    @DisplayName("하나의 게시물을 추출할 수 있다.")
    void getOne(){
        //Given
        var userSaved = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("해당 사용자 정보는 존재하지 않습니다."));

        Post postSaved = postRepository.save(Post.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .user(userSaved)
                .contentList(List.of(Content.builder()
                                .description("집 안 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .mediaUrl("/post/postPicture.png")
                                .tags(List.of(Tag.builder()
                                        .name("아파트")
                                        .build()))

                        .build()))
                .build());

        //When
        PostDto getOnePost = postService.getOne(postSaved.getPostId());

        //Then
        assertThat(getOnePost.getResidentialType(), is(ResidentialType.APARTMENT));
        assertThat(getOnePost.getStyleType(), is(StyleType.ASIAN_STYPE));
        assertThat(getOnePost.getViewCount(), is(1));
        assertThat(getOnePost.getContentList().get(0).getMediaUrl(), is("/post/postPicture.png"));
        assertThat(getOnePost.getContentList().get(0).getTags().get(0).getName(), is("아파트"));
    }

    @Disabled
    @DisplayName("특정 게시글을 수정할 수 있다.")
    void update(){
        //Given
        var postDto = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .userId(userId)
                .contentList(List.of(ContentDto.builder()
                                .description("집 안 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                                .name("내부")
                                                .build(),
                                        TagDto.builder()
                                                .name("벽면")
                                                .build()))

                                .build(),
                        ContentDto.builder()
                                .description("집 밖 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                                .name("외관")
                                                .build(),
                                        TagDto.builder()
                                                .name("옆면")
                                                .build()))
                                .build()))
                .build();

        var dataSaved = postService.save(postDto, List.of( "/post/postpicture_1.jpg", "/post/postpicture_2.jpg"));
        var postDtoUpdated = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .userId(userId)
                .contentList(List.of(ContentDto.builder()
                                .description("[수정]집 안 내용입니다.")  // 수정한 곳
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(TagDto.builder()
                                                .name("내부")
                                                .build(),
                                        TagDto.builder()
                                                .name("벽면")
                                                .build()))

                                .build(),
                        ContentDto.builder()
                                .description("집 밖 내용입니다.")
                                .placeType(PlaceType.BATHROOM)  // 수정한 곳
                                .tags(List.of(TagDto.builder()
                                                .name("외관_수정")  // 수정한 곳
                                                .build(),
                                        TagDto.builder()
                                                .name("옆면")
                                                .build()))
                                .build()))
                .build();


        //When
        //postService.update(postDtoUpdated, List.of( "/post/postpicture_1.jpg", "/post/postpicture_3.jpg")); // 수정한 곳

        //Then
        Post postSaved = postRepository.findById(dataSaved).orElseThrow( () -> new RuntimeException("해당 게시물 정보는 존재하지 않습니다."));
        assertThat(postSaved.getContentList().get(0).getMediaUrl(), is("/post/postpicture_1.jpg"));
        assertThat(postSaved.getContentList().get(1).getMediaUrl(), is("/post/postpicture_3.jpg"));
    }

    @Test
    @DisplayName("특정 게시글을 삭제할 수 있다.")
    void delete(){
        //Given
        var userSaved = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("해당 사용자 정보는 존재하지 않습니다."));

        Post postSaved = postRepository.save(Post.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("사는 집 입니다.")
                .user(userSaved)
                .contentList(List.of(Content.builder()
                        .description("집 안 내용입니다.")
                        .placeType(PlaceType.LIVINGROOM)
                        .mediaUrl("/post/postPicture.png")
                        .tags(List.of(Tag.builder()
                                .name("아파트")
                                .build()))

                        .build()))
                .build());

        //When
        assertThat(postRepository.findById(postSaved.getPostId()).isEmpty(), is(Boolean.FALSE));
        postService.delete(postSaved.getPostId());

        //Then
        assertThat(postRepository.findById(postSaved.getPostId()).isEmpty(), is(Boolean.TRUE));
    }

}