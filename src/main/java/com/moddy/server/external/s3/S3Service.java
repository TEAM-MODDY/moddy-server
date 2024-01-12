package com.moddy.server.external.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.moddy.server.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final static String APPLICATION_PATH = "APPLICATION";
    private final static String MODEL_PROFILE_PATH = "HAIR_MODEL_PROFILE";
    private final static String MODEL_PROFILE_IMAGE_NAME = "/model_default_profile.png";
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadProfileImage(MultipartFile multipartFile, Role role) {
        return uploadImage(multipartFile, role.name());
    }

    public String uploadApplicationImage(MultipartFile multipartFile) {
        return uploadImage(multipartFile, APPLICATION_PATH);
    }

    public String getDefaultProfileImageUrl(){
        return amazonS3.getUrl(bucket, MODEL_PROFILE_PATH + MODEL_PROFILE_IMAGE_NAME).toString();
    }

    private String uploadImage(MultipartFile multipartFile, String path) {
        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket + "/" + path, fileName, inputStream, objectMetadata));
            return amazonS3.getUrl(bucket + "/" + path, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        if (fileName.isEmpty()) throw new IllegalArgumentException();

        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");

        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) throw new IllegalArgumentException();

        return fileName.substring(fileName.lastIndexOf("."));
    }
}
