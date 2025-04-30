package com.score.backend.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.score.backend.exceptions.ExceptionType;
import com.score.backend.exceptions.ScoreCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final String CLOUD_FRONT_DOMAIN_NAME = "https://dc70yyxvhpj8a.cloudfront.net";

    public String uploadImage(MultipartFile file) throws IOException {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension != null && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
            String fileName = UUID.randomUUID().toString();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
            return CLOUD_FRONT_DOMAIN_NAME + "/" + fileName;
        }
        throw new ScoreCustomException(ExceptionType.UNSUPPORTED_FILE_TYPE);
    }
}
