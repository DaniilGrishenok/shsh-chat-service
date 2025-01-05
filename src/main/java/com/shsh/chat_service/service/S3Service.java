package com.shsh.chat_service.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class S3Service {

    private final AmazonS3 s3Client;


    // Retrieve AWS credentials from environment variables
    String ACCESS_KEY ="1GGQ99IARPI5XJ0YZGTM";
    String SECRET_KEY ="5DlV4rfBo6VfhDG5BzxmMD1rNVvWB1j2G2YDpY9z";
    String ENDPOINT = "https://s3.ru1.storage.beget.cloud";
    String region = "ru1";
    String BUCKET_NAME = "580647b53aad-shsh-s3";
    public S3Service() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .enablePathStyleAccess()
                .build();
    }

    // Загрузка файла в S3
    public String uploadFile(String chatId, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String s3Key = "chatPhotos/" + chatId + "/images/" + fileName;  // Путь в S3 для изображений

        // Преобразуем MultipartFile в File
        File tempFile = File.createTempFile("chat_", fileName);
        file.transferTo(tempFile);

        // Загрузка файла в S3
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, s3Key, tempFile);
        s3Client.putObject(request);

        // Возвращаем URL загруженного файла
        return s3Client.getUrl(BUCKET_NAME, s3Key).toString();
    }

    // Получение файла из S3
    public S3ObjectInputStream downloadFile(String chatId, String fileName) throws IOException {
        String s3Key = "chatPhotos/" + chatId + "/images/" + fileName;
        S3Object object = s3Client.getObject(BUCKET_NAME, s3Key);
        return object.getObjectContent();
    }

    // Проверка существования файла в S3
    public boolean fileExists(String chatId, String fileName) {
        String s3Key = "chatPhotos/" + chatId + "/images/" + fileName;
        return s3Client.doesObjectExist(BUCKET_NAME, s3Key);
    }

    // Удаление файла из S3
    public void deleteFileFromS3(String chatId, String fileName) {
        String s3Key = "chatPhotos/" + chatId + "/images/" + fileName;
        try {
            s3Client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, s3Key));
            System.out.println("Файл успешно удален из S3: " + s3Key);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении файла из S3: " + e.getMessage(), e);
        }
    }

    // Получение публичного URL для файла
    public String getFileUrl(String chatId, String fileName) {
        String s3Key = "chatPhotos/" + chatId + "/images/" + fileName;
        return s3Client.getUrl(BUCKET_NAME, s3Key).toString();
    }
}
