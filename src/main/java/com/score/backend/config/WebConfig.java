package com.score.backend.config;

import com.score.backend.domain.exercise.emotion.EmotionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private OctetStreamReadMsgConverter octetStreamReadMsgConverter;

    @Autowired
    public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter) {
        this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
    }

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
        converters.add((HttpMessageConverter<?>) octetStreamReadMsgConverter);
    }
}
