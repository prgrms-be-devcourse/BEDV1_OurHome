package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.follow.entity.Follow;
import com.armand.ourhome.community.follow.repository.FollowRepository;
import com.armand.ourhome.community.post.dto.request.*;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.entity.Tag;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by yunyun on 2021/10/31.
 */


@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowRepository followRepository;

    private User writer;
    private User user;

    private Post post;


    private final String residentialTypeNames = getValuesFromEnum(List.of(ResidentialType.values()));
    private final String placeTypeNames = getValuesFromEnum(List.of(PlaceType.values()));
    private final String squareTypeNames = getValuesFromEnum(List.of(SquareType.values()));
    private final String styleTypeNames = getValuesFromEnum(List.of(StyleType.values()));

    @BeforeEach
    void setUp(){
        writer = userRepository.saveAndFlush(User.builder()
                .email("test@email.com")
                .password("1223")
                .nickname("화이팅!!")
                .description("모두의 집 개발자")
                .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                .build());

        user = userRepository.saveAndFlush(User.builder()
                .email("test1@email.com")
                .password("1223")
                .nickname("화이팅!!11")
                .description("모두의 집 개발자11")
                .profileImageUrl("/user/idslielfjidjf-jielj1911.jpg")
                .build());

        var userSaved = userRepository.findById(writer.getId()).orElseThrow( () -> new RuntimeException("해당 사용자 정보는 존재하지 않습니다."));

        followRepository.save(Follow.builder()
                .follower(userSaved)
                .following(user)
                .build());

        post = postRepository.saveAndFlush(Post.builder()
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
                                .name("tag1")
                                .build()))

                        .build()))
                .build());

    }
    @AfterEach
    void tearDown(){
        postRepository.deleteAll();
        followRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물을 저장할 수 있다.")
    void save() throws Exception {
        //Given

        var postDto = CreatePostRequest.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("this")
                .userId(writer.getId())
                .contentList(List.of(CreateContentRequest.builder()
                                .imageBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAArIAAAEOCAYAAAB1mfQTAAAAAXNSR0IArs4c6QACtMd0RVh0bXhmaWxlACUzQ214ZmlsZSUyMGhvc3QlM0QlMjJhcHAuZGlhZ3JhbXMubmV0JTIyJTIwbW9kaWZpZWQlM0QlMjIyMDIxLTEwLTE3VDIzJTNBMjMlM0EzNi4zMDFaJTIyJTIwYWdlbnQlM0QlMjI1LjAlMjAoV2luZG93cyUyME5UJTIwMTAuMCUzQiUyMFdpbjY0JTNCJTIweDY0KSUyMEFwcGxlV2ViS2l0JTJGNTM3LjM2JTIwKEtIVE1MJTJDJTIwbGlrZSUyMEdlY2tvKSUyMENocm9tZSUyRjk0LjAuNDYwNi44MSUyMFNhZmFyaSUyRjUzNy4zNiUyMiUyMGV0YWclM0QlMjJvRUg1c0FjdEZSdjFxcGZub2R1UCUyMiUyMHZlcnNpb24lM0QlMjIxNS41LjQlMjIlMjB0eXBlJTNEJTIyZ29vZ2xlJTIyJTNFJTNDZGlhZ3JhbSUyMGlkJTNEJTIyRGV5dmg3UG5jZ1R3djJIcXhyc0UlMjIlMjBuYW1lJTNEJTIyUGFnZS0xJTIyJTNFN0x6WGx1UklraVQ2TmYzWWRjREpJemh4TUFmZ1lDOTd3RGx4Y09EckZ4WloxZDIxTmJOMzdwNlpTemNpTXdOaERoaGdxbXFpSW1xRyUyRkJ2SzlhYzB4MU9sajFuZSUyRlEyQnN2TnZLUDgzNVBuQzZPY0hhTGwlMkJ0WkEwOGF1aG5PdnNWeFA4endhbnZ2UGZHNkhmVzdjNnk1YyUyRm5iaU9ZN2ZXMDU4YjAzRVk4blQ5VTFzOHolMkJQeDU5T0tzZnZ6WGFlNHpQJTJGUzRLUng5OWRXdjg3VzZsY3JoWkQlMkZiSmZ6dXF6JTJCdUROTSUyRkQ3Z1B2N2o1TjlIc2xSeE5oNyUyRjBvUUtmME81ZVJ6WFgwZjl5ZVVkTU40ZmR2bDFuZmp2ZlBxUEI1dnpZZjJQWE9CY1U1RXB4SDhyNXZYV0NVaVFYQ1Q2TyUyRktybHozdXR0OEh6STd4blAzJTJCeE92MWh4bWVoNSUyRkE0ZFozVExxTzg5OVFkcyUyRm50WDRNcGNWSjNsbmpVcSUyRjFPRHluSk9PNmp2MiUyRm5NQjBkUWslMkJXTWZwYWEzV3ZudCUyQmdaJTJGRGNWdTdlc2k1ZjdnT2VociUyRk9xemZSd3E2eTg5JTJGYWZwOW1GSSUyQjl2azZYODhwdjMlMkY2ZCUyRmlQNlBrOTZQQSUyRm5ITDgwNFhvNzZkVSUyRiUyQkk5NHZlMiUyQlBlZ0tmJTJGUjlUJTJGdCUyQmh6OGJ0ciUyRkUyWkdzYiUyRlltZkdkcDhGQiUyRjExVDElMkYxUGFQNGYyN2tESDdCeDJwYnp1QTBaTjNiQVAwOVhhUEh6OWUlMkI2SWw2bVgzWXY2ak4lMkZIcCUyRjl1U1h6Unl2MFI4dHpYSzBybUhBTU1BTWklMkZyUXV2JTJCMTVONWElMkYxZU0lMkZXcDZEUE1WUllLeHBYTmJuQjRWQ09ZbFMlMkJkJTJCUnBDRCUyRmpzRUk5bmNLUSUyQkMlMkZFekJHWmpHTW94bWFQcWZGeCUyRkxmRnZTM1pTJTJGJTJGY3lLQXBQNEglMkY2TiUyRjhUOU8lMkY0WmpmdzBCRXZtdkNnSGkzd2tCcmh1M3JKakhaN0QlMkZMd3lGNHpoJTJCbTRieUNZUnglMkJTMTlIdUpmWW1GY3EzeiUyQjVkMiUyRnAlMkY4WTVYJTJCZWwlMkYlMkJPJTJGbyUyRlRIUG1MbXduNE40ejZOOXlNJTJGVmU1R2Y2TG13VU8lMkJhdG5qN3J2NGdFNGRWbmplZjA5OFNIb2Y0NWxrTDhBSVBRWHk4QTAlMkZsZTd3TlIlMkZGUVQlMkJRUVQlMkJ4UWg1OXFUYTMzOGQ1N1Y2WW1pSU8lMkJHZnJleFBGSU40JTJGSW5BZjU2ampTQnFmNUpJazYlMkZyOWJ2NTRtMGQlMkY1eGklMkZsMWJnbnYlMkZUeTA1NTEyODF2dWZhY0MlMkZaWlhmTDdYR0dzemdQenlBNGslMkZjUWYlMkY4UXY3c0Q1cjZqU0wlMkIzT2N5Ym5PYSUyRjk3TnYlMkJieCUyRjZIblAwTDNIMTNSZiUyQjduQ2FjeVglMkYlMkZTejQlMkZUJTJGakclMkIlMkYzVSUyRjR0RCUyRnIlMkZ6NGR4TDlxJTJCY2VtJTJGJTJGajYzJTJGTmlROGolMkJBMGolMkY5a0xTZiUyRmY2bFR5cjhucDMwMUc2ZldRdGl5ZkFWUWRWYjNtemhTbjRJUGpVUUIlMkY5bHJ5eSUyQjFhOG8lMkJHZjZRbTh4ZjMlMkI3MTklMkJlVjNHUHFOSkVucUlkRVVBZE1rZ2Y5UFhmOGZoME1NJTJCek1jRXVpJTJGQVlmUXYwRUk2ZjhxTkNUSiUyRjdqQiUyRnglMkJWJTJGYk40alolMkZrJTJGJTJCdlhoJTJCWU41ZDhRcnZaWTB6NmdsMVNPelBObE9KOUslMkJKVFBrWmclMkIlMkY3QSUyQng0VFBUNDdvbUZsOURqNmEwQWx2ejhhWUhNNDR6JTJGNTQlMkJQUTNoR1h0cnh2ejhSM2JhaE5PY1RSODdTYWpWWFdDMEpySTZzejl4aTR0TzcxNjY3UkttJTJCclZhYUk0NVVvYXRuTVN2MVN1c3IzVHcwcWw1c1pTYWNteVhCTmVzQVdVWSUyQnF5dnVEbnhwJTJCUHRhQTRTWHVWYzNxeiUyQkJGeHViSHl6SGxZeWhObkxFRmVjUHFMMXp4JTJGOXVCcHVwdjAxZUo4eWE3cVRlUTdxdk5CaFgwUG80R3M3UmgzREN0ZWVWWHI1eVElMkJvY0ltSnBIQlRabHRNJTJGaU5jd25CdVBGdzREaE81cmhVNXZPd3ZROU9UNWIxUFlTOUtsbnhjeCUyQnRIMDd2ZmNDRHZ1aU1jZFVIYzklMkZwYXhEVGtLekNaM1RaTXdBem9NN1hhWTdZZnJ5dHAydGh1UEs4bk9BN242dmJYSWYwZVd6MmZlVG9tOWhsREdxcjQlMkJsWVppbFolMkZyekZSTzBIN2ZtOGVrdXVhU1kzRHZ1NFRta01ROXJ5QzFsdVRHTm5XNlZJV3RPWm1LaGZFaSUyRmhmSDJPRnF1VVVjcDMzVE83eEslMkJlQVglMkIlMkZlSmQ0bVJJajlVNFRLYWU5TWVXQXBaJTJGQ2hyJTJGRCUyQmd4YUpJZUFGOXZVV0NDenVTaUhVVTJCR2FiYWU1RUlhMzlnVTdNT2V5anpjR2MxeCUyRjZLbTlSbDclMkJGdzA0YkRPalRpaEFPdk00OTBhMnMlMkJoaUNOcGUlMkJOVURTMk4yeWJTdjFaNEJuRlVkak8xalMxdkEzaSUyRmN0VnpydmdLMlc5ZTlWMTcwaDZXb2lQb3owVWxmM1lpT3dUJTJCWmhVbXFETTZMc3hUeVhSR1JINWV0ZzVXUEtnRiUyQkpjaWZBNWwlMkI1OGVvSWlHRVRRMHVic3d2aW5tdFNNak5lUzJwTlBJenk5QmxqdGl0ZkI1NWkyamhLYThpdm1HcTkzYVpsJTJCcWVkQnFLcm95MHhrZVpGeGFmVFhlWWs4Tk9HWnlmWTNoMGRabFlsUTdSS3VlbG5rMWElMkJ6STZ5SXp2NTZTeCUyQmwzeDRXa3FGZDN5UG1QeG1lUmRZY2xVanhoaGcxWjZoYUg3dWVPNVNEbkNIclZjSVlubG44dWFiTmg3QnliMFdlV1NpYTZybWpJRGE2YTByTzhYTllZVHpFbTYlMkZjUEt5eUpHVW9MJTJGYUMlMkJQNFJIU0xQOWVVN3RlVENRM3U0MlFZeXhJelhONjZtUkMlMkZNTzVZZlZHT2hvc29nRGg4ekpiUTFNaVlXMjUlMkJobG16azV2bnd0SVBRUEk3M3J0aGhGNDBVR2Z1R2dMRXZ4N1R6V2M4RXN1R1IwdnZHdWRSSUdGd1N5a00yUlh6d3VTVGRQMEZwS1VGSnBkWkpvTDdxNmo2eVZzTThYTHJVblFsMlV3ZkgxZ1JibU5GM3lDRDRndWM0VGI0MSUyRncyOGRGNVhVR0VTUXk5Z1ZPak01S3lUVCUyRm10MktxZWFYbVRKaTFOWlV4TnJLVGxHVTdPVWlFWXJROFRIU0xDc3cwcGppeENYbG4yVkd1MSUyQjZ0TXUzWlp0d2Q1V2V6enJkNWpVVnR4d2hTaHdxWjB3b0twbHAxSCUyQm1ISVolMkJyYjg4Q1hKa3E5c3RobFh3cVdZMW0xcVRNZkpFSGJaV2RKQnMxUHhGVUJMWmNUYzg3UnRUb3VJdFZ2JTJGWUtzQ1N1WmdWTHZENnR0VkVUazZVbWI1Wm42YXo5THB3WGdKdzFxaUhJYkVNODRSR2FraTdISmFrdjhxdzdJQkVtJTJCNFdjbzFjOWg5MVo3SXc4MDdrNW9YbXRxJTJCQkFzN2VsVlk2cWFPN2pGUWlGekt0UHNCVkxucmozYmptY3UzNFVQdWZSR1RzRkUzNFE3NW43MWZzRE4xR3Nmdm8ydnh5QnVnakJ5T0c1YUY4UU56JTJGcEhpU2EzZEdrd3VUZHRYbEMwJTJCa1ZpRms4QVZDZU1iWHZ3SnR1VEolMkJwdHpTYlVkMlVkWHpkREplZmltNHNwZmZmUzhDUzZLJTJCb1FTaFBHNFBNTXkydThFcWtVN2JCT2dFJTJCV1BCc3FlJTJCbHclMkZIcFRVbWZIc0hVam5qVGttZTk0NkdYbzFEeFRQbDE4MzBoRnNZYkFpSSUyQlZSRU02eXhwSyUyQlkwUXBXUHFyeXVvZHBLcjRmcWp1UTcxUFElMkZIVW")
                                .description("집 안 내용입니다.")
                                .placeType(PlaceType.LIVINGROOM)
                                .tags(List.of(CreateTagRequest.builder()
                                                .name("내부")
                                                .build()))
                                .build()))
                .build();


        //When, Then
        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("post/savePost", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NULL).description("게시물 아이디"),
                                        fieldWithPath("residential_type").type(JsonFieldType.STRING).description("게시될 집의 거주유형"+residentialTypeNames),
                                        fieldWithPath("square_type").type(JsonFieldType.STRING).description("게시될 집의 평수"+squareTypeNames),
                                        fieldWithPath("style_type").type(JsonFieldType.STRING).description("게시될 집의 스타일"+styleTypeNames),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
                                        fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                        fieldWithPath("view_count").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("content_list").type(JsonFieldType.ARRAY).description("게시될 집의 구체적 내용들"),
                                        fieldWithPath("content_list[].id").type(JsonFieldType.NULL).description("게시물 구체 내용물 아이디"),
                                        fieldWithPath("content_list[].media_url").type(JsonFieldType.NULL).description("게시될 집의 사진이 저장된 위치"),
                                        fieldWithPath("content_list[].image_base64").type(JsonFieldType.STRING).description("게시될 집의 사진"),
                                        fieldWithPath("content_list[].description").type(JsonFieldType.STRING).description("게시된 사진에 대한 설명"),
                                        fieldWithPath("content_list[].place_type").type(JsonFieldType.STRING).description("게시된 사진의 공간 유형"+placeTypeNames),
                                        fieldWithPath("content_list[].tags").type(JsonFieldType.ARRAY).description("게시된 사진과 관련된 태그들"),
                                        fieldWithPath("content_list[].tags[].id").type(JsonFieldType.NULL).description("태그 아이디"),
                                        fieldWithPath("content_list[].tags[].name").type(JsonFieldType.STRING).description("태그 이름")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 게시물 아이디")
                                )
                                )
                );
    }

    @Test
    @DisplayName("게시된 모든 정보를 추출할 수 있다.")
    void getAll() throws Exception {
        //When, Then
       mockMvc.perform(get("/api/posts?size=5&page=0")
                .contentType(MediaType.APPLICATION_JSON)
                .param("user_id", user.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].square_type").value("SIZE_10_PYEONG"))
                .andDo(print())
                .andDo(
                       document("post/getAllPost", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                               responseFields(
                                       fieldWithPath("content").type(JsonFieldType.ARRAY).description("모든 게시물들"),
                                       fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                       fieldWithPath("content[].residential_type").type(JsonFieldType.STRING).description("게시될 집의 거주유형"+residentialTypeNames),
                                       fieldWithPath("content[].square_type").type(JsonFieldType.STRING).description("게시될 집의 평수"+squareTypeNames),
                                       fieldWithPath("content[].style_type").type(JsonFieldType.STRING).description("게시될 집의 스타일"+styleTypeNames),
                                       fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시물의 제목"),
                                       fieldWithPath("content[].view_count").type(JsonFieldType.NUMBER).description("조회수"),
                                       fieldWithPath("content[].is_follower").type(JsonFieldType.BOOLEAN).description("본 게시물 팔로우 여부"),
                                       fieldWithPath("content[].updated_at").type(JsonFieldType.STRING).description("게시물 생성일자"),
                                       fieldWithPath("content[].created_at").type(JsonFieldType.STRING).description("게시물 최근 수정일자"),
                                       fieldWithPath("content[].content_list").type(JsonFieldType.ARRAY).description("게시될 집의 구체적 내용들"),
                                       fieldWithPath("content[].content_list[].id").type(JsonFieldType.NUMBER).description("게시물 구체 내용물 아이디"),
                                       fieldWithPath("content[].content_list[].media_url").type(JsonFieldType.STRING).description("게시될 집의 사진이 저장된 위치"),
                                       fieldWithPath("content[].content_list[].description").type(JsonFieldType.STRING).description("게시된 사진에 대한 설명"),
                                       fieldWithPath("content[].content_list[].place_type").type(JsonFieldType.STRING).description("게시된 사진의 공간 유형"+placeTypeNames),
                                       fieldWithPath("content[].content_list[].tags").type(JsonFieldType.ARRAY).description("게시된 사진과 관련된 태그들"),
                                       fieldWithPath("content[].content_list[].tags[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                       fieldWithPath("content[].content_list[].tags[].name").type(JsonFieldType.STRING).description("태그 이름"),

                                       fieldWithPath("total_pages").type(JsonFieldType.NUMBER).description("총 페이지 숫자"),
                                       fieldWithPath("number_of_elements").type(JsonFieldType.NUMBER).description("row 몇개 들어갔는지"),
                                       fieldWithPath("total_elements").type(JsonFieldType.NUMBER).description("총 row 숫자"),
                                       fieldWithPath("number").type(JsonFieldType.NUMBER).description("요청받은 페이지 넘버")

                               )
                       )
               );

    }

    @Test
    @DisplayName("저장되어 있는 모든 게시물 중 거주형태를 기준으로 정보를 추출할 수 있다.")
    void getAllByResidentialType() throws Exception {

        //When, Then
        mockMvc.perform(get("/api/posts/type")
                        .param("criteria_type", "RESIDENTIAL_TYPE")
                        .param("criteria", "APARTMENT")
                        .param("size", "1")
                        .param("page", "0")
                        .param("user_id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].residential_type").value("APARTMENT"))
                .andDo(print())
                .andDo(
                        document("post/getAllByCriteria", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("content").type(JsonFieldType.ARRAY).description("모든 게시물들"),
                                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                        fieldWithPath("content[].residential_type").type(JsonFieldType.STRING).description("게시될 집의 거주유형"+residentialTypeNames),
                                        fieldWithPath("content[].square_type").type(JsonFieldType.STRING).description("게시될 집의 평수"+squareTypeNames),
                                        fieldWithPath("content[].style_type").type(JsonFieldType.STRING).description("게시될 집의 스타일"+styleTypeNames),
                                        fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시물의 제목"),
                                        fieldWithPath("content[].view_count").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("content[].is_follower").type(JsonFieldType.BOOLEAN).description("본 게시물 팔로우 여부"),
                                        fieldWithPath("content[].updated_at").type(JsonFieldType.STRING).description("게시물 생성일자"),
                                        fieldWithPath("content[].created_at").type(JsonFieldType.STRING).description("게시물 최근 수정일자"),
                                        fieldWithPath("content[].content_list").type(JsonFieldType.ARRAY).description("게시될 집의 구체적 내용들"),
                                        fieldWithPath("content[].content_list[].id").type(JsonFieldType.NUMBER).description("게시물 구체 내용물 아이디"),
                                        fieldWithPath("content[].content_list[].media_url").type(JsonFieldType.STRING).description("게시될 집의 사진이 저장된 위치"),
                                        fieldWithPath("content[].content_list[].description").type(JsonFieldType.STRING).description("게시된 사진에 대한 설명"),
                                        fieldWithPath("content[].content_list[].place_type").type(JsonFieldType.STRING).description("게시된 사진의 공간 유형"+placeTypeNames),
                                        fieldWithPath("content[].content_list[].tags").type(JsonFieldType.ARRAY).description("게시된 사진과 관련된 태그들"),
                                        fieldWithPath("content[].content_list[].tags[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                        fieldWithPath("content[].content_list[].tags[].name").type(JsonFieldType.STRING).description("태그 이름"),

                                        fieldWithPath("total_pages").type(JsonFieldType.NUMBER).description("총 페이지 숫자"),
                                        fieldWithPath("number_of_elements").type(JsonFieldType.NUMBER).description("row 몇개 들어갔는지"),
                                        fieldWithPath("total_elements").type(JsonFieldType.NUMBER).description("총 row 숫자"),
                                        fieldWithPath("number").type(JsonFieldType.NUMBER).description("요청받은 페이지 넘버")

                                )
                        )
                );

    }

    @Test
    @DisplayName("저장되어 있는 모든 게시물 중 공간형태를 기준으로 정보를 추출할 수 있다.")
    void getAllByPlaceType() throws Exception {

        //When, Then
        mockMvc.perform(get("/api/posts/type")
                        .param("criteria_type", "PLACE_TYPE")
                        .param("criteria", "LIVINGROOM")
                        .param("size", "1")
                        .param("page", "0")
                        .param("user_id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].content_list[0].place_type").value("LIVINGROOM"))
                .andDo(print());

    }

    @Test
    @DisplayName("저장되어 있는 모든 게시물 중 테그를 기준으로 정보를 추출할 수 있다.")
    void getAllByTag() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/posts/type")
                        .param("criteria_type", "TAG")
                        .param("criteria", "tag1")
                        .param("size", "1")
                        .param("page", "0")
                        .param("user_id", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].content_list[0].tags[0].name").value("tag1"))
                .andDo(print());

    }
    @Test
    @DisplayName("특정 게시물을 추출할 수 있다.")
    void getOne() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(post.getId()))
                .andDo(print())
                .andDo(
                        document("post/getOnePost", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("게시물 아이디(검색 기준)")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                        fieldWithPath("residential_type").type(JsonFieldType.STRING).description("게시될 집의 거주유형"+residentialTypeNames),
                                        fieldWithPath("square_type").type(JsonFieldType.STRING).description("게시될 집의 평수"+squareTypeNames),
                                        fieldWithPath("style_type").type(JsonFieldType.STRING).description("게시될 집의 스타일"+styleTypeNames),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
                                        fieldWithPath("view_count").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("updated_at").type(JsonFieldType.STRING).description("게시물 생성일자"),
                                        fieldWithPath("created_at").type(JsonFieldType.STRING).description("게시물 최근 수정일자"),
                                        fieldWithPath("content_list").type(JsonFieldType.ARRAY).description("게시될 집의 구체적 내용들"),
                                        fieldWithPath("content_list[].id").type(JsonFieldType.NUMBER).description("게시물 구체 내용물 아이디"),
                                        fieldWithPath("content_list[].media_url").type(JsonFieldType.STRING).description("게시될 집의 사진이 저장된 위치"),
                                        fieldWithPath("content_list[].description").type(JsonFieldType.STRING).description("게시된 사진에 대한 설명"),
                                        fieldWithPath("content_list[].place_type").type(JsonFieldType.STRING).description("게시된 사진의 공간 유형"+placeTypeNames),
                                        fieldWithPath("content_list[].tags").type(JsonFieldType.ARRAY).description("게시된 사진과 관련된 태그들"),
                                        fieldWithPath("content_list[].tags[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                        fieldWithPath("content_list[].tags[].name").type(JsonFieldType.STRING).description("태그 이름")
                                )
                        )
                );
    }

    @Test
    @DisplayName("특정 게시물을 삭제 할 수 있다.")
    @Order(value = 7)
    void deleteTest() throws Exception {
        //When, Then
        mockMvc.perform(delete("/api/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value(post.getId()))
                .andDo(print())
                .andDo(
                        document("post/deletePost", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("삭제 요청 게시물 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("사제된 게시물 아이디")
                                )
                        )
                );
        //assertThat(postRepository.count(), is(0L)); //each method 로는 잘 되는데, perclass 만 하면 삭제가 안됨.
    }
    @Test
    @DisplayName("게시물을 수정할 수 있다.")
    void update() throws Exception {
        //Given

        Post postSavedBeforeUpdate = postRepository.findById(post.getId()).orElseThrow( () -> new RuntimeException("해당 게시물 정보는 존재하지 않습니다."));
        var postDtoUpdated = UpdatePostRequest.builder()
                .id(postSavedBeforeUpdate.getId())
                .residentialType(postSavedBeforeUpdate.getResidentialType())
                .squareType(postSavedBeforeUpdate.getSquareType())
                .styleType(postSavedBeforeUpdate.getStyleType())
                .title(postSavedBeforeUpdate.getTitle())
                .userId(writer.getId())
                .contentList(List.of(UpdateContentRequest.builder()
                                .contentId(postSavedBeforeUpdate.getContentList().get(0).getContentId())
                                .imageBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAArIAAAEOCAYAAAB1mfQTAAAAAXNSR0IArs4c6QACtMd0RVh0bXhmaWxlACUzQ214ZmlsZSUyMGhvc3QlM0QlMjJhcHAuZGlhZ3JhbXMubmV0JTIyJTIwbW9kaWZpZWQlM0QlMjIyMDIxLTEwLTE3VDIzJTNBMjMlM0EzNi4zMDFaJTIyJTIwYWdlbnQlM0QlMjI1LjAlMjAoV2luZG93cyUyME5UJTIwMTAuMCUzQiUyMFdpbjY0JTNCJTIweDY0KSUyMEFwcGxlV2ViS2l0JTJGNTM3LjM2JTIwKEtIVE1MJTJDJTIwbGlrZSUyMEdlY2tvKSUyMENocm9tZSUyRjk0LjAuNDYwNi44MSUyMFNhZmFyaSUyRjUzNy4zNiUyMiUyMGV0YWclM0QlMjJvRUg1c0FjdEZSdjFxcGZub2R1UCUyMiUyMHZlcnNpb24lM0QlMjIxNS41LjQlMjIlMjB0eXBlJTNEJTIyZ29vZ2xlJTIyJTNFJTNDZGlhZ3JhbSUyMGlkJTNEJTIyRGV5dmg3UG5jZ1R3djJIcXhyc0UlMjIlMjBuYW1lJTNEJTIyUGFnZS0xJTIyJTNFN0x6WGx1UklraVQ2TmYzWWRjREpJemh4TUFmZ1lDOTd3RGx4Y09EckZ4WloxZDIxTmJOMzdwNlpTemNpTXdOaERoaGdxbXFpSW1xRyUyRkJ2SzlhYzB4MU9sajFuZSUyRlEyQnN2TnZLUDgzNVBuQzZPY0hhTGwlMkJ0WkEwOGF1aG5PdnNWeFA4endhbnZ2UGZHNkhmVzdjNnk1YyUyRm5iaU9ZN2ZXMDU4YjAzRVk4blQ5VTFzOHolMkJQeDU5T0tzZnZ6WGFlNHpQJTJGUzRLUng5OWRXdjg3VzZsY3JoWkQlMkZiSmZ6dXF6JTJCdUROTSUyRkQ3Z1B2N2o1TjlIc2xSeE5oNyUyRjBvUUtmME81ZVJ6WFgwZjl5ZVVkTU40ZmR2bDFuZmp2ZlBxUEI1dnpZZjJQWE9CY1U1RXB4SDhyNXZYV0NVaVFYQ1Q2TyUyRktybHozdXR0OEh6STd4blAzJTJCeE92MWh4bWVoNSUyRkE0ZFozVExxTzg5OVFkcyUyRm50WDRNcGNWSjNsbmpVcSUyRjFPRHluSk9PNmp2MiUyRm5NQjBkUWslMkJXTWZwYWEzV3ZudCUyQmdaJTJGRGNWdTdlc2k1ZjdnT2VociUyRk9xemZSd3E2eTg5JTJGYWZwOW1GSSUyQjl2azZYODhwdjMlMkY2ZCUyRmlQNlBrOTZQQSUyRm5ITDgwNFhvNzZkVSUyRiUyQkk5NHZlMiUyQlBlZ0tmJTJGUjlUJTJGdCUyQmh6OGJ0ciUyRkUyWkdzYiUyRlltZkdkcDhGQiUyRjExVDElMkYxUGFQNGYyN2tESDdCeDJwYnp1QTBaTjNiQVAwOVhhUEh6OWUlMkI2SWw2bVgzWXY2ak4lMkZIcCUyRjl1U1h6Unl2MFI4dHpYSzBybUhBTU1BTWklMkZyUXV2JTJCMTVONWElMkYxZU0lMkZXcDZEUE1WUllLeHBYTmJuQjRWQ09ZbFMlMkJkJTJCUnBDRCUyRmpzRUk5bmNLUSUyQkMlMkZFekJHWmpHTW94bWFQcWZGeCUyRkxmRnZTM1pTJTJGJTJGY3lLQXBQNEglMkY2TiUyRjhUOU8lMkY0WmpmdzBCRXZtdkNnSGkzd2tCcmh1M3JKakhaN0QlMkZMd3lGNHpoJTJCbTRieUNZUnglMkJTMTlIdUpmWW1GY3EzeiUyQjVkMiUyRnAlMkY4WTVYJTJCZWwlMkYlMkJPJTJGbyUyRlRIUG1MbXduNE40ejZOOXlNJTJGVmU1R2Y2TG13VU8lMkJhdG5qN3J2NGdFNGRWbmplZjA5OFNIb2Y0NWxrTDhBSVBRWHk4QTAlMkZsZTd3TlIlMkZGUVQlMkJRUVQlMkJ4UWg1OXFUYTMzOGQ1N1Y2WW1pSU8lMkJHZnJleFBGSU40JTJGSW5BZjU2ampTQnFmNUpJazYlMkZyOWJ2NTRtMGQlMkY1eGklMkZsMWJnbnYlMkZUeTA1NTEyODF2dWZhY0MlMkZaWlhmTDdYR0dzemdQenlBNGslMkZjUWYlMkY4UXY3c0Q1cjZqU0wlMkIzT2N5Ym5PYSUyRjk3TnYlMkJieCUyRjZIblAwTDNIMTNSZiUyQjduQ2FjeVglMkYlMkZTejQlMkZUJTJGakclMkIlMkYzVSUyRjR0RCUyRnIlMkZ6NGR4TDlxJTJCY2VtJTJGJTJGajYzJTJGTmlROGolMkJBMGolMkY5a0xTZiUyRmY2bFR5cjhucDMwMUc2ZldRdGl5ZkFWUWRWYjNtemhTbjRJUGpVUUIlMkY5bHJ5eSUyQjFhOG8lMkJHZjZRbTh4ZjMlMkI3MTklMkJlVjNHUHFOSkVucUlkRVVBZE1rZ2Y5UFhmOGZoME1NJTJCek1jRXVpJTJGQVlmUXYwRUk2ZjhxTkNUSiUyRjdqQiUyRnglMkJWJTJGYk40alolMkZrJTJGJTJCdlhoJTJCWU41ZDhRcnZaWTB6NmdsMVNPelBObE9KOUslMkJKVFBrWmclMkIlMkY3QSUyQng0VFBUNDdvbUZsOURqNmEwQWx2ejhhWUhNNDR6JTJGNTQlMkJQUTNoR1h0cnh2ejhSM2JhaE5PY1RSODdTYWpWWFdDMEpySTZzejl4aTR0TzcxNjY3UkttJTJCclZhYUk0NVVvYXRuTVN2MVN1c3IzVHcwcWw1c1pTYWNteVhCTmVzQVdVWSUyQnF5dnVEbnhwJTJCUHRhQTRTWHVWYzNxeiUyQkJGeHViSHl6SGxZeWhObkxFRmVjUHFMMXp4JTJGOXVCcHVwdjAxZUo4eWE3cVRlUTdxdk5CaFgwUG80R3M3UmgzREN0ZWVWWHI1eVElMkJvY0ltSnBIQlRabHRNJTJGaU5jd25CdVBGdzREaE81cmhVNXZPd3ZROU9UNWIxUFlTOUtsbnhjeCUyQnRIMDd2ZmNDRHZ1aU1jZFVIYzklMkZwYXhEVGtLekNaM1RaTXdBem9NN1hhWTdZZnJ5dHAydGh1UEs4bk9BN242dmJYSWYwZVd6MmZlVG9tOWhsREdxcjQlMkJsWVppbFolMkZyekZSTzBIN2ZtOGVrdXVhU1kzRHZ1NFRta01ROXJ5QzFsdVRHTm5XNlZJV3RPWm1LaGZFaSUyRmhmSDJPRnF1VVVjcDMzVE83eEslMkJlQVglMkIlMkZlSmQ0bVJJajlVNFRLYWU5TWVXQXBaJTJGQ2hyJTJGRCUyQmd4YUpJZUFGOXZVV0NDenVTaUhVVTJCR2FiYWU1RUlhMzlnVTdNT2V5anpjR2MxeCUyRjZLbTlSbDclMkJGdzA0YkRPalRpaEFPdk00OTBhMnMlMkJoaUNOcGUlMkJOVURTMk4yeWJTdjFaNEJuRlVkak8xalMxdkEzaSUyRmN0VnpydmdLMlc5ZTlWMTcwaDZXb2lQb3owVWxmM1lpT3dUJTJCWmhVbXFETTZMc3hUeVhSR1JINWV0ZzVXUEtnRiUyQkpjaWZBNWwlMkI1OGVvSWlHRVRRMHVic3d2aW5tdFNNak5lUzJwTlBJenk5QmxqdGl0ZkI1NWkyamhLYThpdm1HcTkzYVpsJTJCcWVkQnFLcm95MHhrZVpGeGFmVFhlWWs4Tk9HWnlmWTNoMGRabFlsUTdSS3VlbG5rMWElMkJ6STZ5SXp2NTZTeCUyQmwzeDRXa3FGZDN5UG1QeG1lUmRZY2xVanhoaGcxWjZoYUg3dWVPNVNEbkNIclZjSVlubG44dWFiTmg3QnliMFdlV1NpYTZybWpJRGE2YTByTzhYTllZVHpFbTYlMkZjUEt5eUpHVW9MJTJGYUMlMkJQNFJIU0xQOWVVN3RlVENRM3U0MlFZeXhJelhONjZtUkMlMkZNTzVZZlZHT2hvc29nRGg4ekpiUTFNaVlXMjUlMkJobG16azV2bnd0SVBRUEk3M3J0aGhGNDBVR2Z1R2dMRXZ4N1R6V2M4RXN1R1IwdnZHdWRSSUdGd1N5a00yUlh6d3VTVGRQMEZwS1VGSnBkWkpvTDdxNmo2eVZzTThYTHJVblFsMlV3ZkgxZ1JibU5GM3lDRDRndWM0VGI0MSUyRncyOGRGNVhVR0VTUXk5Z1ZPak01S3lUVCUyRm10MktxZWFYbVRKaTFOWlV4TnJLVGxHVTdPVWlFWXJROFRIU0xDc3cwcGppeENYbG4yVkd1MSUyQjZ0TXUzWlp0d2Q1V2V6enJkNWpVVnR4d2hTaHdxWjB3b0twbHAxSCUyQm1ISVolMkJyYjg4Q1hKa3E5c3RobFh3cVdZMW0xcVRNZkpFSGJaV2RKQnMxUHhGVUJMWmNUYzg3UnRUb3VJdFZ2JTJGWUtzQ1N1WmdWTHZENnR0VkVUazZVbWI1Wm42YXo5THB3WGdKdzFxaUhJYkVNODRSR2FraTdISmFrdjhxdzdJQkVtJTJCNFdjbzFjOWg5MVo3SXc4MDdrNW9YbXRxJTJCQkFzN2VsVlk2cWFPN2pGUWlGekt0UHNCVkxucmozYmptY3UzNFVQdWZSR1RzRkUzNFE3NW43MWZzRE4xR3Nmdm8ydnh5QnVnakJ5T0c1YUY4UU56JTJGcEhpU2EzZEdrd3VUZHRYbEMwJTJCa1ZpRms4QVZDZU1iWHZ3SnR1VEolMkJwdHpTYlVkMlVkWHpkREplZmltNHNwZmZmUzhDUzZLJTJCb1FTaFBHNFBNTXkydThFcWtVN2JCT2dFJTJCV1BCc3FlJTJCbHclMkZIcFRVbWZIc0hVam5qVGttZTk0NkdYbzFEeFRQbDE4MzBoRnNZYkFpSSUyQlZSRU02eXhwSyUyQlkwUXBXUHFyeXVvZHBLcjRmcWp1UTcxUFElMkZIVW")
                                .description("[수정]집 안 내용입니다.")  // 수정한 곳
                                .placeType(postSavedBeforeUpdate.getContentList().get(0).getPlaceType())
                                .tags(List.of(UpdateTagRequest.builder()
                                                .tagId(postSavedBeforeUpdate.getContentList().get(0).getTags().get(0).getTagId())
                                                .name(postSavedBeforeUpdate.getContentList().get(0).getTags().get(0).getName())
                                                .build()))

                                .build()))
                .build();

        //When, Then
        mockMvc.perform(post("/api/posts/{id}", post.getId())
                        .content(objectMapper.writeValueAsString(postDtoUpdated))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(
                        document("post/updatePost", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("수정 요청 게시물 아이디")
                                ),
                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시물 아이디"),
                                        fieldWithPath("residential_type").type(JsonFieldType.STRING).description("게시될 집의 거주유형"+residentialTypeNames),
                                        fieldWithPath("square_type").type(JsonFieldType.STRING).description("게시될 집의 평수"+squareTypeNames),
                                        fieldWithPath("style_type").type(JsonFieldType.STRING).description("게시될 집의 스타일"+styleTypeNames),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시물의 제목"),
                                        fieldWithPath("user_id").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                                        fieldWithPath("view_count").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("content_list").type(JsonFieldType.ARRAY).description("게시될 집의 구체적 내용들"),
                                        fieldWithPath("content_list[].id").type(JsonFieldType.NUMBER).description("게시물 구체 내용물 아이디"),
                                        fieldWithPath("content_list[].image_base64").type(JsonFieldType.STRING).description("게시될 집의 사진"),
                                        fieldWithPath("content_list[].description").type(JsonFieldType.STRING).description("게시된 사진에 대한 설명"),
                                        fieldWithPath("content_list[].place_type").type(JsonFieldType.STRING).description("게시된 사진의 공간 유형"+placeTypeNames),
                                        fieldWithPath("content_list[].tags").type(JsonFieldType.ARRAY).description("게시된 사진과 관련된 태그들"),
                                        fieldWithPath("content_list[].tags[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                        fieldWithPath("content_list[].tags[].name").type(JsonFieldType.STRING).description("태그 이름")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 게시물 아이디")
                                )
                        )
                );
    }


    private String getValuesFromEnum(List enumValues){ //
        StringBuffer typeNames = new StringBuffer();
        typeNames.append("\n").append("(유형: ");
        enumValues.forEach(typeName -> typeNames.append(typeName).append(" "));
        typeNames.append(")");
        return typeNames.toString();
    }


}