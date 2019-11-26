package com.ap.blog.events.blogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringToFetchTypeConverterTest {

    private final StringToFetchTypeConverter subject;

    StringToFetchTypeConverterTest() {
        subject = new StringToFetchTypeConverter();
    }

    @Test
    void convert() {
        assertEquals(FetchType.MARKDOWN, subject.convert("md"));
    }
}