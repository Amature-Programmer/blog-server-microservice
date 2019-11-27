package com.elixr.blog.events.blogs;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringToFetchTypeConverter implements Converter<String, FetchType> {
    @Override
    public FetchType convert(String s) {
        return Arrays.stream(FetchType.values())
                .filter(fetchType -> fetchType.toString().equalsIgnoreCase(s))
                .findFirst().get();
    }
}
