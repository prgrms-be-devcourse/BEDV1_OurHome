package com.armand.ourhome.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(String base64, String dirName) throws IOException {
        if(base64 == null){
            log.info("이미지 base64가 비어있습니다. null를 반환합니다.");
            return null;
        }
        
        // content type 파싱
        String[] split = base64.split(",");
        String contentType = split[0].substring(split[0].indexOf(":") + 1, split[0].indexOf(";"));

        // base64 -> byte
        byte[] decode = Base64.getMimeDecoder().decode(split[1]);

        // byte -> inputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);

        // 메타데이터 생성
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(decode.length);
        metadata.setContentType(contentType);

        // filename 지정
        String fileName = dirName + "/" + UUID.randomUUID();

        // s3 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }




    // 1. Controller에서 MultipartFile 받고 Service로 전달
    // 2. Service에서 아래 메소드를 부름
//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        // multipartFile -> File로 변환
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//        return upload(uploadFile, dirName);
//    }
//
//    private String upload(File uploadFile, String dirName) {
//        String fileName = dirName + "/" + UUID.randomUUID() + "_" + uploadFile.getName();
//        // S3에 저장 -> url 리턴됨
//        String uploadImageUrl = putS3(uploadFile, fileName);
//        // 로컬 파일 삭제
//        removeNewFile(uploadFile);
//        return uploadImageUrl;
//    }
//
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
//        return amazonS3Client.getUrl(bucket, fileName).toString();
//    }
//
//    private void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            log.info("S3 업로드 완료 (로컬 파일은 삭제되었습니다.)");
//        } else {
//            log.info("파일이 삭제되지 못했습니다.");
//        }
//    }
//
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File convertFile = new File(file.getOriginalFilename());
//        if(convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
//            return Optional.of(convertFile);
//        }
//        return Optional.empty();
//    }
}

