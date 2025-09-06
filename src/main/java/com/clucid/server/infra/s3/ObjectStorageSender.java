package com.clucid.server.infra.s3;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageSender {
	@Value("${custom.img-prefix}")
	private String imgPrefix;
	@Value("${custom.s3.bucket-name}")
	private String bucketName;

	private final AmazonS3 s3;
	/**
	 * @return 파일의 URL
	 * */
	public String sendImage(String fileName, MultipartFile file){

		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), null);
			s3.putObject(request);
			log.info("Object {} has been created in bucket {}", request.getKey(), request.getBucketName());
			return imgPrefix + request.getKey();
		} catch (AmazonS3Exception e) {
			log.error("S3에 파일 업로드 중 오류 발생: {}", e.getMessage());
			// 런타임 예외로 던져줘
			throw new IllegalStateException("S3에 파일 업로드 중 오류 발생: " + e.getMessage(), e);
		} catch(SdkClientException e) {
			log.error("S3 클라이언트 오류 발생: {}", e.getMessage());
			throw new IllegalStateException("S3에 파일 업로드 중 오류 발생: " + e.getMessage(), e);
		} catch (IOException e) {
			log.error("Unexpected error occurred while uploading file to S3: {}", e.getMessage());
			throw new IllegalStateException("S3에 파일 업로드 중 오류 발생: " + e.getMessage(), e);
		}
	}
}
