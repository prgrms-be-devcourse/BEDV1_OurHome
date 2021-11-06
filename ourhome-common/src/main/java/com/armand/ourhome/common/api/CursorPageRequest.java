package com.armand.ourhome.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
public class CursorPageRequest {
    private int size;
    private Long lastId;
    private Boolean isFirst;

    public void setSize(int size) {
        this.size = size;
    }

    // snake 파라미터를 camel로 받기
    // 참고 : https://stackoverflow.com/questions/38757946/jsonproperty-not-working-for-content-type-application-x-www-form-urlencoded
    public void setLast_id(Long lastId) {
        this.lastId = lastId;
    }

    public void setIs_first(Boolean first) {
        this.isFirst = first;
    }
}

