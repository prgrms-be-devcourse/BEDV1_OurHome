package com.armand.ourhome.community.post.controller;

import com.armand.ourhome.community.post.dto.ContentDto;
import com.armand.ourhome.community.post.dto.PostDto;
import com.armand.ourhome.community.post.dto.TagDto;
import com.armand.ourhome.community.post.entity.*;
import com.armand.ourhome.community.post.repository.PostRepository;
import com.armand.ourhome.community.post.service.PostService;
import com.armand.ourhome.domain.user.User;
import com.armand.ourhome.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UTFDataFormatException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by yunyun on 2021/10/31.
 */



@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;

    private Post post;


    @BeforeEach
    void setUp(){
        user = userRepository.saveAndFlush(User.builder()
                .email("test@email.com")
                .password("1223")
                .nickname("화이팅!!")
                .description("모두의 집 개발자")
                .profileImageUrl("/user/idslielfjidjf-jielj19.jpg")
                .build());

        var userSaved = userRepository.findById(user.getId()).orElseThrow( () -> new RuntimeException("해당 사용자 정보는 존재하지 않습니다."));

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
                                .name("아파트")
                                .build()))

                        .build()))
                .build());

    }
    @AfterEach
    void tearDown(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시물을 저장할 수 있다.")
    void save() throws Exception {
        //Given

        var postDto = PostDto.builder()
                .residentialType(ResidentialType.APARTMENT)
                .squareType(SquareType.SIZE_10_PYEONG)
                .styleType(StyleType.ASIAN_STYPE)
                .title("this")
                .userId(user.getId())
                .contentList(List.of(ContentDto.builder()
                                .imageBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAArIAAAEOCAYAAAB1mfQTAAAAAXNSR0IArs4c6QACtMd0RVh0bXhmaWxlACUzQ214ZmlsZSUyMGhvc3QlM0QlMjJhcHAuZGlhZ3JhbXMubmV0JTIyJTIwbW9kaWZpZWQlM0QlMjIyMDIxLTEwLTE3VDIzJTNBMjMlM0EzNi4zMDFaJTIyJTIwYWdlbnQlM0QlMjI1LjAlMjAoV2luZG93cyUyME5UJTIwMTAuMCUzQiUyMFdpbjY0JTNCJTIweDY0KSUyMEFwcGxlV2ViS2l0JTJGNTM3LjM2JTIwKEtIVE1MJTJDJTIwbGlrZSUyMEdlY2tvKSUyMENocm9tZSUyRjk0LjAuNDYwNi44MSUyMFNhZmFyaSUyRjUzNy4zNiUyMiUyMGV0YWclM0QlMjJvRUg1c0FjdEZSdjFxcGZub2R1UCUyMiUyMHZlcnNpb24lM0QlMjIxNS41LjQlMjIlMjB0eXBlJTNEJTIyZ29vZ2xlJTIyJTNFJTNDZGlhZ3JhbSUyMGlkJTNEJTIyRGV5dmg3UG5jZ1R3djJIcXhyc0UlMjIlMjBuYW1lJTNEJTIyUGFnZS0xJTIyJTNFN0x6WGx1UklraVQ2TmYzWWRjREpJemh4TUFmZ1lDOTd3RGx4Y09EckZ4WloxZDIxTmJOMzdwNlpTemNpTXdOaERoaGdxbXFpSW1xRyUyRkJ2SzlhYzB4MU9sajFuZSUyRlEyQnN2TnZLUDgzNVBuQzZPY0hhTGwlMkJ0WkEwOGF1aG5PdnNWeFA4endhbnZ2UGZHNkhmVzdjNnk1YyUyRm5iaU9ZN2ZXMDU4YjAzRVk4blQ5VTFzOHolMkJQeDU5T0tzZnZ6WGFlNHpQJTJGUzRLUng5OWRXdjg3VzZsY3JoWkQlMkZiSmZ6dXF6JTJCdUROTSUyRkQ3Z1B2N2o1TjlIc2xSeE5oNyUyRjBvUUtmME81ZVJ6WFgwZjl5ZVVkTU40ZmR2bDFuZmp2ZlBxUEI1dnpZZjJQWE9CY1U1RXB4SDhyNXZYV0NVaVFYQ1Q2TyUyRktybHozdXR0OEh6STd4blAzJTJCeE92MWh4bWVoNSUyRkE0ZFozVExxTzg5OVFkcyUyRm50WDRNcGNWSjNsbmpVcSUyRjFPRHluSk9PNmp2MiUyRm5NQjBkUWslMkJXTWZwYWEzV3ZudCUyQmdaJTJGRGNWdTdlc2k1ZjdnT2VociUyRk9xemZSd3E2eTg5JTJGYWZwOW1GSSUyQjl2azZYODhwdjMlMkY2ZCUyRmlQNlBrOTZQQSUyRm5ITDgwNFhvNzZkVSUyRiUyQkk5NHZlMiUyQlBlZ0tmJTJGUjlUJTJGdCUyQmh6OGJ0ciUyRkUyWkdzYiUyRlltZkdkcDhGQiUyRjExVDElMkYxUGFQNGYyN2tESDdCeDJwYnp1QTBaTjNiQVAwOVhhUEh6OWUlMkI2SWw2bVgzWXY2ak4lMkZIcCUyRjl1U1h6Unl2MFI4dHpYSzBybUhBTU1BTWklMkZyUXV2JTJCMTVONWElMkYxZU0lMkZXcDZEUE1WUllLeHBYTmJuQjRWQ09ZbFMlMkJkJTJCUnBDRCUyRmpzRUk5bmNLUSUyQkMlMkZFekJHWmpHTW94bWFQcWZGeCUyRkxmRnZTM1pTJTJGJTJGY3lLQXBQNEglMkY2TiUyRjhUOU8lMkY0WmpmdzBCRXZtdkNnSGkzd2tCcmh1M3JKakhaN0QlMkZMd3lGNHpoJTJCbTRieUNZUnglMkJTMTlIdUpmWW1GY3EzeiUyQjVkMiUyRnAlMkY4WTVYJTJCZWwlMkYlMkJPJTJGbyUyRlRIUG1MbXduNE40ejZOOXlNJTJGVmU1R2Y2TG13VU8lMkJhdG5qN3J2NGdFNGRWbmplZjA5OFNIb2Y0NWxrTDhBSVBRWHk4QTAlMkZsZTd3TlIlMkZGUVQlMkJRUVQlMkJ4UWg1OXFUYTMzOGQ1N1Y2WW1pSU8lMkJHZnJleFBGSU40JTJGSW5BZjU2ampTQnFmNUpJazYlMkZyOWJ2NTRtMGQlMkY1eGklMkZsMWJnbnYlMkZUeTA1NTEyODF2dWZhY0MlMkZaWlhmTDdYR0dzemdQenlBNGslMkZjUWYlMkY4UXY3c0Q1cjZqU0wlMkIzT2N5Ym5PYSUyRjk3TnYlMkJieCUyRjZIblAwTDNIMTNSZiUyQjduQ2FjeVglMkYlMkZTejQlMkZUJTJGakclMkIlMkYzVSUyRjR0RCUyRnIlMkZ6NGR4TDlxJTJCY2VtJTJGJTJGajYzJTJGTmlROGolMkJBMGolMkY5a0xTZiUyRmY2bFR5cjhucDMwMUc2ZldRdGl5ZkFWUWRWYjNtemhTbjRJUGpVUUIlMkY5bHJ5eSUyQjFhOG8lMkJHZjZRbTh4ZjMlMkI3MTklMkJlVjNHUHFOSkVucUlkRVVBZE1rZ2Y5UFhmOGZoME1NJTJCek1jRXVpJTJGQVlmUXYwRUk2ZjhxTkNUSiUyRjdqQiUyRnglMkJWJTJGYk40alolMkZrJTJGJTJCdlhoJTJCWU41ZDhRcnZaWTB6NmdsMVNPelBObE9KOUslMkJKVFBrWmclMkIlMkY3QSUyQng0VFBUNDdvbUZsOURqNmEwQWx2ejhhWUhNNDR6JTJGNTQlMkJQUTNoR1h0cnh2ejhSM2JhaE5PY1RSODdTYWpWWFdDMEpySTZzejl4aTR0TzcxNjY3UkttJTJCclZhYUk0NVVvYXRuTVN2MVN1c3IzVHcwcWw1c1pTYWNteVhCTmVzQVdVWSUyQnF5dnVEbnhwJTJCUHRhQTRTWHVWYzNxeiUyQkJGeHViSHl6SGxZeWhObkxFRmVjUHFMMXp4JTJGOXVCcHVwdjAxZUo4eWE3cVRlUTdxdk5CaFgwUG80R3M3UmgzREN0ZWVWWHI1eVElMkJvY0ltSnBIQlRabHRNJTJGaU5jd25CdVBGdzREaE81cmhVNXZPd3ZROU9UNWIxUFlTOUtsbnhjeCUyQnRIMDd2ZmNDRHZ1aU1jZFVIYzklMkZwYXhEVGtLekNaM1RaTXdBem9NN1hhWTdZZnJ5dHAydGh1UEs4bk9BN242dmJYSWYwZVd6MmZlVG9tOWhsREdxcjQlMkJsWVppbFolMkZyekZSTzBIN2ZtOGVrdXVhU1kzRHZ1NFRta01ROXJ5QzFsdVRHTm5XNlZJV3RPWm1LaGZFaSUyRmhmSDJPRnF1VVVjcDMzVE83eEslMkJlQVglMkIlMkZlSmQ0bVJJajlVNFRLYWU5TWVXQXBaJTJGQ2hyJTJGRCUyQmd4YUpJZUFGOXZVV0NDenVTaUhVVTJCR2FiYWU1RUlhMzlnVTdNT2V5anpjR2MxeCUyRjZLbTlSbDclMkJGdzA0YkRPalRpaEFPdk00OTBhMnMlMkJoaUNOcGUlMkJOVURTMk4yeWJTdjFaNEJuRlVkak8xalMxdkEzaSUyRmN0VnpydmdLMlc5ZTlWMTcwaDZXb2lQb3owVWxmM1lpT3dUJTJCWmhVbXFETTZMc3hUeVhSR1JINWV0ZzVXUEtnRiUyQkpjaWZBNWwlMkI1OGVvSWlHRVRRMHVic3d2aW5tdFNNak5lUzJwTlBJenk5QmxqdGl0ZkI1NWkyamhLYThpdm1HcTkzYVpsJTJCcWVkQnFLcm95MHhrZVpGeGFmVFhlWWs4Tk9HWnlmWTNoMGRabFlsUTdSS3VlbG5rMWElMkJ6STZ5SXp2NTZTeCUyQmwzeDRXa3FGZDN5UG1QeG1lUmRZY2xVanhoaGcxWjZoYUg3dWVPNVNEbkNIclZjSVlubG44dWFiTmg3QnliMFdlV1NpYTZybWpJRGE2YTByTzhYTllZVHpFbTYlMkZjUEt5eUpHVW9MJTJGYUMlMkJQNFJIU0xQOWVVN3RlVENRM3U0MlFZeXhJelhONjZtUkMlMkZNTzVZZlZHT2hvc29nRGg4ekpiUTFNaVlXMjUlMkJobG16azV2bnd0SVBRUEk3M3J0aGhGNDBVR2Z1R2dMRXZ4N1R6V2M4RXN1R1IwdnZHdWRSSUdGd1N5a00yUlh6d3VTVGRQMEZwS1VGSnBkWkpvTDdxNmo2eVZzTThYTHJVblFsMlV3ZkgxZ1JibU5GM3lDRDRndWM0VGI0MSUyRncyOGRGNVhVR0VTUXk5Z1ZPak01S3lUVCUyRm10MktxZWFYbVRKaTFOWlV4TnJLVGxHVTdPVWlFWXJROFRIU0xDc3cwcGppeENYbG4yVkd1MSUyQjZ0TXUzWlp0d2Q1V2V6enJkNWpVVnR4d2hTaHdxWjB3b0twbHAxSCUyQm1ISVolMkJyYjg4Q1hKa3E5c3RobFh3cVdZMW0xcVRNZkpFSGJaV2RKQnMxUHhGVUJMWmNUYzg3UnRUb3VJdFZ2JTJGWUtzQ1N1WmdWTHZENnR0VkVUazZVbWI1Wm42YXo5THB3WGdKdzFxaUhJYkVNODRSR2FraTdISmFrdjhxdzdJQkVtJTJCNFdjbzFjOWg5MVo3SXc4MDdrNW9YbXRxJTJCQkFzN2VsVlk2cWFPN2pGUWlGekt0UHNCVkxucmozYmptY3UzNFVQdWZSR1RzRkUzNFE3NW43MWZzRE4xR3Nmdm8ydnh5QnVnakJ5T0c1YUY4UU56JTJGcEhpU2EzZEdrd3VUZHRYbEMwJTJCa1ZpRms4QVZDZU1iWHZ3SnR1VEolMkJwdHpTYlVkMlVkWHpkREplZmltNHNwZmZmUzhDUzZLJTJCb1FTaFBHNFBNTXkydThFcWtVN2JCT2dFJTJCV1BCc3FlJTJCbHclMkZIcFRVbWZIc0hVam5qVGttZTk0NkdYbzFEeFRQbDE4MzBoRnNZYkFpSSUyQlZSRU02eXhwSyUyQlkwUXBXUHFyeXVvZHBLcjRmcWp1UTcxUFElMkZIVW")
                                .updatedFlag(false)
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
                                .imageBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAArIAAAEOCAYAAAB1mfQTAAAAAXNSR0IArs4c6QACtMd0RVh0bXhmaWxlACUzQ214ZmlsZSUyMGhvc3QlM0QlMjJhcHAuZGlhZ3JhbXMubmV0JTIyJTIwbW9kaWZpZWQlM0QlMjIyMDIxLTEwLTE3VDIzJTNBMjMlM0EzNi4zMDFaJTIyJTIwYWdlbnQlM0QlMjI1LjAlMjAoV2luZG93cyUyME5UJTIwMTAuMCUzQiUyMFdpbjY0JTNCJTIweDY0KSUyMEFwcGxlV2ViS2l0JTJGNTM3LjM2JTIwKEtIVE1MJTJDJTIwbGlrZSUyMEdlY2tvKSUyMENocm9tZSUyRjk0LjAuNDYwNi44MSUyMFNhZmFyaSUyRjUzNy4zNiUyMiUyMGV0YWclM0QlMjJvRUg1c0FjdEZSdjFxcGZub2R1UCUyMiUyMHZlcnNpb24lM0QlMjIxNS41LjQlMjIlMjB0eXBlJTNEJTIyZ29vZ2xlJTIyJTNFJTNDZGlhZ3JhbSUyMGlkJTNEJTIyRGV5dmg3UG5jZ1R3djJIcXhyc0UlMjIlMjBuYW1lJTNEJTIyUGFnZS0xJTIyJTNFN0x6WGx1UklraVQ2TmYzWWRjREpJemh4TUFmZ1lDOTd3RGx4Y09EckZ4WloxZDIxTmJOMzdwNlpTemNpTXdOaERoaGdxbXFpSW1xRyUyRkJ2SzlhYzB4MU9sajFuZSUyRlEyQnN2TnZLUDgzNVBuQzZPY0hhTGwlMkJ0WkEwOGF1aG5PdnNWeFA4endhbnZ2UGZHNkhmVzdjNnk1YyUyRm5iaU9ZN2ZXMDU4YjAzRVk4blQ5VTFzOHolMkJQeDU5T0tzZnZ6WGFlNHpQJTJGUzRLUng5OWRXdjg3VzZsY3JoWkQlMkZiSmZ6dXF6JTJCdUROTSUyRkQ3Z1B2N2o1TjlIc2xSeE5oNyUyRjBvUUtmME81ZVJ6WFgwZjl5ZVVkTU40ZmR2bDFuZmp2ZlBxUEI1dnpZZjJQWE9CY1U1RXB4SDhyNXZYV0NVaVFYQ1Q2TyUyRktybHozdXR0OEh6STd4blAzJTJCeE92MWh4bWVoNSUyRkE0ZFozVExxTzg5OVFkcyUyRm50WDRNcGNWSjNsbmpVcSUyRjFPRHluSk9PNmp2MiUyRm5NQjBkUWslMkJXTWZwYWEzV3ZudCUyQmdaJTJGRGNWdTdlc2k1ZjdnT2VociUyRk9xemZSd3E2eTg5JTJGYWZwOW1GSSUyQjl2azZYODhwdjMlMkY2ZCUyRmlQNlBrOTZQQSUyRm5ITDgwNFhvNzZkVSUyRiUyQkk5NHZlMiUyQlBlZ0tmJTJGUjlUJTJGdCUyQmh6OGJ0ciUyRkUyWkdzYiUyRlltZkdkcDhGQiUyRjExVDElMkYxUGFQNGYyN2tESDdCeDJwYnp1QTBaTjNiQVAwOVhhUEh6OWUlMkI2SWw2bVgzWXY2ak4lMkZIcCUyRjl1U1h6Unl2MFI4dHpYSzBybUhBTU1BTWklMkZyUXV2JTJCMTVONWElMkYxZU0lMkZXcDZEUE1WUllLeHBYTmJuQjRWQ09ZbFMlMkJkJTJCUnBDRCUyRmpzRUk5bmNLUSUyQkMlMkZFekJHWmpHTW94bWFQcWZGeCUyRkxmRnZTM1pTJTJGJTJGY3lLQXBQNEglMkY2TiUyRjhUOU8lMkY0WmpmdzBCRXZtdkNnSGkzd2tCcmh1M3JKakhaN0QlMkZMd3lGNHpoJTJCbTRieUNZUnglMkJTMTlIdUpmWW1GY3EzeiUyQjVkMiUyRnAlMkY4WTVYJTJCZWwlMkYlMkJPJTJGbyUyRlRIUG1MbXduNE40ejZOOXlNJTJGVmU1R2Y2TG13VU8lMkJhdG5qN3J2NGdFNGRWbmplZjA5OFNIb2Y0NWxrTDhBSVBRWHk4QTAlMkZsZTd3TlIlMkZGUVQlMkJRUVQlMkJ4UWg1OXFUYTMzOGQ1N1Y2WW1pSU8lMkJHZnJleFBGSU40JTJGSW5BZjU2ampTQnFmNUpJazYlMkZyOWJ2NTRtMGQlMkY1eGklMkZsMWJnbnYlMkZUeTA1NTEyODF2dWZhY0MlMkZaWlhmTDdYR0dzemdQenlBNGslMkZjUWYlMkY4UXY3c0Q1cjZqU0wlMkIzT2N5Ym5PYSUyRjk3TnYlMkJieCUyRjZIblAwTDNIMTNSZiUyQjduQ2FjeVglMkYlMkZTejQlMkZUJTJGakclMkIlMkYzVSUyRjR0RCUyRnIlMkZ6NGR4TDlxJTJCY2VtJTJGJTJGajYzJTJGTmlROGolMkJBMGolMkY5a0xTZiUyRmY2bFR5cjhucDMwMUc2ZldRdGl5ZkFWUWRWYjNtemhTbjRJUGpVUUIlMkY5bHJ5eSUyQjFhOG8lMkJHZjZRbTh4ZjMlMkI3MTklMkJlVjNHUHFOSkVucUlkRVVBZE1rZ2Y5UFhmOGZoME1NJTJCek1jRXVpJTJGQVlmUXYwRUk2ZjhxTkNUSiUyRjdqQiUyRnglMkJWJTJGYk40alolMkZrJTJGJTJCdlhoJTJCWU41ZDhRcnZaWTB6NmdsMVNPelBObE9KOUslMkJKVFBrWmclMkIlMkY3QSUyQng0VFBUNDdvbUZsOURqNmEwQWx2ejhhWUhNNDR6JTJGNTQlMkJQUTNoR1h0cnh2ejhSM2JhaE5PY1RSODdTYWpWWFdDMEpySTZzejl4aTR0TzcxNjY3UkttJTJCclZhYUk0NVVvYXRuTVN2MVN1c3IzVHcwcWw1c1pTYWNteVhCTmVzQVdVWSUyQnF5dnVEbnhwJTJCUHRhQTRTWHVWYzNxeiUyQkJGeHViSHl6SGxZeWhObkxFRmVjUHFMMXp4JTJGOXVCcHVwdjAxZUo4eWE3cVRlUTdxdk5CaFgwUG80R3M3UmgzREN0ZWVWWHI1eVElMkJvY0ltSnBIQlRabHRNJTJGaU5jd25CdVBGdzREaE81cmhVNXZPd3ZROU9UNWIxUFlTOUtsbnhjeCUyQnRIMDd2ZmNDRHZ1aU1jZFVIYzklMkZwYXhEVGtLekNaM1RaTXdBem9NN1hhWTdZZnJ5dHAydGh1UEs4bk9BN242dmJYSWYwZVd6MmZlVG9tOWhsREdxcjQlMkJsWVppbFolMkZyekZSTzBIN2ZtOGVrdXVhU1kzRHZ1NFRta01ROXJ5QzFsdVRHTm5XNlZJV3RPWm1LaGZFaSUyRmhmSDJPRnF1VVVjcDMzVE83eEslMkJlQVglMkIlMkZlSmQ0bVJJajlVNFRLYWU5TWVXQXBaJTJGQ2hyJTJGRCUyQmd4YUpJZUFGOXZVV0NDenVTaUhVVTJCR2FiYWU1RUlhMzlnVTdNT2V5anpjR2MxeCUyRjZLbTlSbDclMkJGdzA0YkRPalRpaEFPdk00OTBhMnMlMkJoaUNOcGUlMkJOVURTMk4yeWJTdjFaNEJuRlVkak8xalMxdkEzaSUyRmN0VnpydmdLMlc5ZTlWMTcwaDZXb2lQb3owVWxmM1lpT3dUJTJCWmhVbXFETTZMc3hUeVhSR1JINWV0ZzVXUEtnRiUyQkpjaWZBNWwlMkI1OGVvSWlHRVRRMHVic3d2aW5tdFNNak5lUzJwTlBJenk5QmxqdGl0ZkI1NWkyamhLYThpdm1HcTkzYVpsJTJCcWVkQnFLcm95MHhrZVpGeGFmVFhlWWs4Tk9HWnlmWTNoMGRabFlsUTdSS3VlbG5rMWElMkJ6STZ5SXp2NTZTeCUyQmwzeDRXa3FGZDN5UG1QeG1lUmRZY2xVanhoaGcxWjZoYUg3dWVPNVNEbkNIclZjSVlubG44dWFiTmg3QnliMFdlV1NpYTZybWpJRGE2YTByTzhYTllZVHpFbTYlMkZjUEt5eUpHVW9MJTJGYUMlMkJQNFJIU0xQOWVVN3RlVENRM3U0MlFZeXhJelhONjZtUkMlMkZNTzVZZlZHT2hvc29nRGg4ekpiUTFNaVlXMjUlMkJobG16azV2bnd0SVBRUEk3M3J0aGhGNDBVR2Z1R2dMRXZ4N1R6V2M4RXN1R1IwdnZHdWRSSUdGd1N5a00yUlh6d3VTVGRQMEZwS1VGSnBkWkpvTDdxNmo2eVZzTThYTHJVblFsMlV3ZkgxZ1JibU5GM3lDRDRndWM0VGI0MSUyRncyOGRGNVhVR0VTUXk5Z1ZPak01S3lUVCUyRm10MktxZWFYbVRKaTFOWlV4TnJLVGxHVTdPVWlFWXJROFRIU0xDc3cwcGppeENYbG4yVkd1MSUyQjZ0TXUzWlp0d2Q1V2V6enJkNWpVVnR4d2hTaHdxWjB3b0twbHAxSCUyQm1ISVolMkJyYjg4Q1hKa3E5c3RobFh3cVdZMW0xcVRNZkpFSGJaV2RKQnMxUHhGVUJMWmNUYzg3UnRUb3VJdFZ2JTJGWUtzQ1N1WmdWTHZENnR0VkVUazZVbWI1Wm42YXo5THB3WGdKdzFxaUhJYkVNODRSR2FraTdISmFrdjhxdzdJQkVtJTJCNFdjbzFjOWg5MVo3SXc4MDdrNW9YbXRxJTJCQkFzN2VsVlk2cWFPN2pGUWlGekt0UHNCVkxucmozYmptY3UzNFVQdWZSR1RzRkUzNFE3NW43MWZzRE4xR3Nmdm8ydnh5QnVnakJ5T0c1YUY4UU56JTJGcEhpU2EzZEdrd3VUZHRYbEMwJTJCa1ZpRms4QVZDZU1iWHZ3SnR1VEolMkJwdHpTYlVkMlVkWHpkREplZmltNHNwZmZmUzhDUzZLJTJCb1FTaFBHNFBNTXkydThFcWtVN2JCT2dFJTJCV1BCc3FlJTJCbHclMkZIcFRVbWZIc0hVam5qVGttZTk0NkdYbzFEeFRQbDE4MzBoRnNZYkFpSSUyQlZSRU02eXhwSyUyQlkwUXBXUHFyeXVvZHBLcjRmcWp1UTcxUFElMkZIVW")
                                .updatedFlag(false)
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

        //When, Then
        mockMvc.perform(post("/api/v1/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시된 모든 정보를 추출할 수 있다.")
    void getAll() throws Exception {
        //When, Then
       mockMvc.perform(get("/api/v1/post?size=5&page=0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].square_type").value("SIZE_10_PYEONG"))
                .andDo(print());

    }

    @Test
    @DisplayName("저장되어 있는 모든 게시물 중 특정 거주형태 정보만 추출할 수 있다.")
    void getAllByResidentialType() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/v1/post/residentialType/APARTMENT?size=1&page=0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("content[0].residentialType").value("APARTMENT"))
                .andDo(print());

    }

    @Test
    @DisplayName("특정 게시물을 추출할 수 있다.")
    void getOne() throws Exception {
        //When, Then
        mockMvc.perform(get("/api/v1/post/"+post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("postId").value(post.getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 게시물을 삭제 할 수 있다.")
    void deleteTest() throws Exception {
        //When, Then
        mockMvc.perform(delete("/api/v1/post/"+post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(post.getId()))
                .andDo(print());
        assertThat(postRepository.count(), is(0L));
    }
    @Test
    @DisplayName("게시물을 수정할 수 있다.")
    void update() throws Exception {
        //Given

        Post postSavedBeforeUpdate = postRepository.findById(post.getId()).orElseThrow( () -> new RuntimeException("해당 게시물 정보는 존재하지 않습니다."));
        System.out.println(postSavedBeforeUpdate.getId());
        System.out.println(postSavedBeforeUpdate.getContentList().size());
        var postDtoUpdated = PostDto.builder()
                .id(postSavedBeforeUpdate.getId())
                .residentialType(postSavedBeforeUpdate.getResidentialType())
                .squareType(postSavedBeforeUpdate.getSquareType())
                .styleType(postSavedBeforeUpdate.getStyleType())
                .title(postSavedBeforeUpdate.getTitle())
                .userId(user.getId())
                .contentList(List.of(ContentDto.builder()
                                .updatedFlag(false)
                                .mediaUrl(postSavedBeforeUpdate.getContentList().get(0).getMediaUrl())
                                .description("[수정]집 안 내용입니다.")  // 수정한 곳
                                .placeType(postSavedBeforeUpdate.getContentList().get(0).getPlaceType())
                                .tags(List.of(TagDto.builder()
                                                .name(postSavedBeforeUpdate.getContentList().get(0).getTags().get(0).getName())
                                                .build()))

                                .build()))
                .build();

        //When, Then
        mockMvc.perform(post("/api/v1/post/"+post.getId())
                        .content(objectMapper.writeValueAsString(postDtoUpdated))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$").value(post.getPostId()))
                .andDo(print());
    }


}