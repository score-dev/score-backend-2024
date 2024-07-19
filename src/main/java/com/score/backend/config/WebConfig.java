package com.score.backend.config;

import com.score.backend.models.enums.EmotionType;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
}
