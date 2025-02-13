package com.score.backend.config;

import com.score.backend.domain.exercise.emotion.EmotionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final OctetStreamReadMsgConverter octetStreamReadMsgConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EmotionTypeConverter());
    }
    static class EmotionTypeConverter implements Converter<String, EmotionType> {
        @Override
        public EmotionType convert(String requestType) {
            return EmotionType.create(requestType.toUpperCase());
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(octetStreamReadMsgConverter);
    }
}
