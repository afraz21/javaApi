package org.iqvis.nvolv3.upload.factory;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@PropertySource("classpath:media-upload-keys.properties")
public class MediaUploadS3Factory implements MediaFactory {

	@Autowired
	Environment environment;

	public String upload(File file, String entityName) throws Exception {
		String mediaUrl = "http://";// environment.getProperty("S3-bucket-prefix");

		if (file != null) {

			try {

				String accessKey = environment.getProperty("S3-accessKey");

				String secretKey = environment.getProperty("S3-secretKey");

				String existingBucketName = environment.getProperty("S3-bucket");

				// String filename =
				// FilenameUtils.removeExtension(file.getName()) + "-" +
				// System.currentTimeMillis() + "." +
				// FilenameUtils.getExtension(file.getName());
				String filename = FilenameUtils.removeExtension(file.getName()) + "." + FilenameUtils.getExtension(file.getName());

				String keyName = entityName + "/" + filename;

				mediaUrl += existingBucketName + "/";

				mediaUrl += keyName;

				String amazonFileUploadLocationOriginal = existingBucketName;

				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

				AmazonS3 s3Client = new AmazonS3Client(credentials);

				FileInputStream stream = new FileInputStream(file);

				ObjectMetadata objectMetadata = new ObjectMetadata();

				PutObjectRequest putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, keyName, stream, objectMetadata);

				PutObjectResult result = s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

				System.out.println("Etag:" + result.getETag() + "-->" + result);

			}
			catch (Exception e) {

				throw e;

			}

		}
		else {

			throw new NullPointerException();
		}

		return mediaUrl;
	}

	public static String upload(File file, String entityName, String mediaUrl, String accessKey, String secretKey, String existingBucketName) throws Exception {

		if (file != null) {

			try {

				String filename = FilenameUtils.removeExtension(file.getName()) + "-" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(file.getName());

				String keyName = entityName + "/" + filename;

				mediaUrl += existingBucketName + "/";

				mediaUrl += keyName;

				String amazonFileUploadLocationOriginal = existingBucketName;

				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

				AmazonS3 s3Client = new AmazonS3Client(credentials);

				FileInputStream stream = new FileInputStream(file);

				ObjectMetadata objectMetadata = new ObjectMetadata();

				PutObjectRequest putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, keyName, stream, objectMetadata);

				PutObjectResult result = s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

				System.out.println("Etag:" + result.getETag() + "-->" + result);

			}
			catch (Exception e) {

				throw e;

			}

		}
		else {

			throw new NullPointerException();
		}

		return mediaUrl;
	}

	public String getMyClass() {

		return "S3";
	}

}
