package com.sevya.launchpad.aws;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sevya.launchpad.util.LaunchpadUtility;

public class S3Utility {

	private static Logger logger = LoggerFactory.getLogger(S3Utility.class.getName());
	
	public static AmazonS3 initializeObjects(String accessKey, String secretKey) {
		
		logger.info("S3Utility initializeObjects()");
		
		try {

			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			new AmazonIdentityManagementClient(credentials);
			AmazonS3 s3client = new AmazonS3Client(credentials);
			return s3client;

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("S3Utility initializeObjects() error ...");
		}
		
		return null;
	}

	public static boolean uploadFile(AmazonS3 s3client, String keyName, InputStream stream, String uploadLocation) {

		logger.info("S3Utility uploadFile() ...");

		try {

			if(s3client != null) {
				
				ObjectMetadata objectMetadata = new ObjectMetadata();
				PutObjectRequest putObjectRequest = new PutObjectRequest(uploadLocation, keyName, stream, objectMetadata);
				String uniqueKey = putObjectRequest.getKey();
				putObjectRequest.setKey(uniqueKey);
				s3client.putObject(putObjectRequest);
				return true;
			
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("S3Utility uploadFile() error ...");
		}
		
		return false;
	}

	public static String uploadFileIntoS3Bucket(AmazonS3 s3client, MultipartFile file, String uploadLocation) {

		logger.info("S3Utility uploadFileIntoS3Bucket() ...");

		try {

			InputStream fileInputStream = file.getInputStream();
			String fileExtension = getFormat(file.getOriginalFilename());
			String fileName = LaunchpadUtility.md5UniqueCode();
			fileName += "." + fileExtension;
			boolean uploadStatus = uploadFile(s3client, fileName, fileInputStream, uploadLocation);
			if (uploadStatus == true) {
				logger.info("S3Utility uploadFileIntoS3Bucket() completed file name "+fileName+"...");
				return fileName;
			}
			
		} catch (Exception e) {

			e.printStackTrace();
			logger.info("S3Utility uploadFileIntoS3Bucket() error ...");

		}

		return "File Upload Error...";

	}

	public static String getFormat(String filename) {

		String[] str = filename.split("\\.");
		String extenstion = str[str.length - 1];
		return extenstion;

	}

}
