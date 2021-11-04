package com.armand.ourhome.community.comment.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDateTimeFormatter {

    public String print(final LocalDateTime createdAt) {
        final String DATE_TIME_FORMAT = "M월 d일 h시 m분";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return createdAt.format(formatter);
    }
}
