package org.iqvis.nvolv3.utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.iqvis.nvolv3.bean.MultiLingual;
import org.iqvis.nvolv3.bean.MultiLingualAddresses;
import org.iqvis.nvolv3.bean.MultiLingualPersonnelInformation;
import org.iqvis.nvolv3.dao.TimeZoneResponse;
import org.iqvis.nvolv3.domain.Event;
import org.iqvis.nvolv3.domain.EventConfiguration;
import org.iqvis.nvolv3.domain.EventSearch;
import org.iqvis.nvolv3.mobile.bean.DataCMS;
import org.iqvis.nvolv3.mobile.bean.EventTexts;
import org.iqvis.nvolv3.mobile.bean.LabelEntities;
import org.iqvis.nvolv3.mobile.bean.ListEvent;
import org.iqvis.nvolv3.objectchangelog.domain.DataChangeLog;
import org.iqvis.nvolv3.push.DeviceToken;
import org.iqvis.nvolv3.search.Field;
import org.iqvis.nvolv3.search.OrderBy;
import org.iqvis.nvolv3.search.QueryOperator;
import org.iqvis.nvolv3.search.Where;
import org.iqvis.nvolv3.serializer.JodaDateTimeJsonSerializer;
import org.iqvis.nvolv3.serializer.TimeJsonSerializer;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ch.lambdaj.Lambda;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.bitcasa.cloudfs.client.Exists;
import com.bitcasa.cloudfs.client.Folder;
import com.bitcasa.cloudfs.client.Session;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jmx.snmp.Timestamp;

public class Utils {

	protected static Logger logger = Logger.getLogger("Utils");

	public static final String DATE_VALUE = "dateValue";

	public static final String TIME_VALUE = "time";

	public static final String BOOLEAN_VALUE = "boolean";

	public static String getURLWithContextPath(HttpServletRequest request) {

		return request.getSession().getServletContext().getInitParameter("applicationUrl") + request.getContextPath();
	}

	public static String uploadFileToS3(File file, String entityName, HttpServletRequest request) throws Exception {

		String mediaUrl = request.getSession().getServletContext().getInitParameter("S3-bucket-prefix");

		if (file != null) {

			try {

				String accessKey = request.getSession().getServletContext().getInitParameter("S3-accessKey");

				String secretKey = request.getSession().getServletContext().getInitParameter("S3-secretKey");

				String existingBucketName = request.getSession().getServletContext().getInitParameter("S3-bucket");

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

	public static String uploadFileToS3(File file, String entityName, String mediaUrl, String accessKey, String secretKey, String existingBucketName) throws Exception {

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

	public static String updloadFileToBitcasa(File file, String entityName, HttpServletRequest request) {

		String endPoint = request.getSession().getServletContext().getInitParameter("endpoint");

		String clientId = request.getSession().getServletContext().getInitParameter("client_id");

		String clientSecret = request.getSession().getServletContext().getInitParameter("client_secret");

		String username = request.getSession().getServletContext().getInitParameter("username");

		String password = request.getSession().getServletContext().getInitParameter("password");

		String url = request.getSession().getServletContext().getInitParameter("dowload_file_url");

		String mediaId = "";

		if (file != null) {

			try {

				Session session = new Session(endPoint, clientId, clientSecret);

				session.authenticate(username, password);

				Folder root = session.getFileSystem().getRoot();

				Folder[] folders = root.getSubFolders();

				Folder folderForFileUpload = null;

				for (Folder f : folders) {

					if (f.getName().equalsIgnoreCase(entityName)) {

						folderForFileUpload = f;
					}
				}

				if (folderForFileUpload == null) {

					folderForFileUpload = session.getFileSystem().createFolder(root.getAbsolutePath(), entityName, Exists.FAIL);

				}

				System.out.println(folderForFileUpload.getAbsolutePath());

				com.bitcasa.cloudfs.client.File uploadedFile = session.getFileSystem().uploadFile(folderForFileUpload.getAbsolutePath(), file, null, Exists.FAIL);

				mediaId = uploadedFile.getId();

				url += folderForFileUpload.getId() + "/";

				url += mediaId;

				// java.io.File uploadingFile =
				// temporaryFolder.newFile("Test.txt");

			}
			catch (Exception e) {

				e.printStackTrace();

			}

		}

		return url;
	}

	public S3Object getMediaFile(String fileName, String entityName, HttpServletRequest request) {

		// String mediaUrl =
		// request.getSession().getServletContext().getInitParameter("S3-bucket-prefix");

		String accessKey = request.getSession().getServletContext().getInitParameter("S3-accessKey");

		String secretKey = request.getSession().getServletContext().getInitParameter("S3-secretKey");

		String existingBucketName = request.getSession().getServletContext().getInitParameter("S3-bucket");

		String keyName = entityName + "/" + fileName;

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3Client = new AmazonS3Client(credentials);

		GetObjectRequest S3request = new GetObjectRequest(existingBucketName, keyName);

		S3Object object = s3Client.getObject(S3request);

		// S3ObjectInputStream objectContent = object.getObjectContent();
		// /IOUtils.copy(objectContent, new
		// FileOutputStream("D://upload//test.jpg"));

		return object;

	}

	public static File multipartToFile(MultipartFile multipart, String type) throws IllegalStateException, IOException {

		if (!StringUtils.isEmpty(type)) {

			type += "-";
		}

		File convFile = new File(type + System.currentTimeMillis() + multipart.getOriginalFilename());

		convFile.createNewFile();

		FileOutputStream fos = new FileOutputStream(convFile);

		fos.write(multipart.getBytes());

		fos.close();

		return convFile;

	}

	public static Query parseCriteria(org.iqvis.nvolv3.search.Criteria search, String collection) {

		Query query = new Query();

		if (search == null) {
			search = new org.iqvis.nvolv3.search.Criteria();

		}

		if (null != search.getQuery()) {

			if (null != search.getQuery().getWhere() && search.getQuery().getWhere().size() > 0) {
				List<Criteria> wheres = new ArrayList<Criteria>();

				for (Where whereClause : search.getQuery().getWhere()) {

					Criteria where = new Criteria();

					if (null != whereClause.getOr() && whereClause.getOr().size() > 0) {

						List<Criteria> orCriterias = new ArrayList<Criteria>(whereClause.getOr().size());

						for (Field field : whereClause.getOr()) {

							if (field.getOperator().equalsIgnoreCase(QueryOperator.IS)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(BOOLEAN_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(Boolean.valueOf(field.getFieldValue())));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).is(field.getFieldValue()));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToTime(field.getFieldValue())));

									}
								}
								else {
									orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(field.getFieldValue()));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToTime(field.getFieldValue())));
									}

								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(field.getFieldValue()));

								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToTime(field.getFieldValue())));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToTime(field.getFieldValue())));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.NE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(BOOLEAN_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(Boolean.valueOf(field.getFieldValue())));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.IN)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										String[] dates = field.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToDateTime(dates[i]));

										}

										orCriterias.add(Criteria.where(collection + field.getFieldName()).in(datesList));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										String[] dates = field.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToTime(dates[i]));

										}

										orCriterias.add(Criteria.where(collection + field.getFieldName()).in(datesList));

									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).in(Arrays.asList(whereClause.getFieldValue().split(","))));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LIKE)) {

								orCriterias.add(Criteria.where(collection + field.getFieldName()).regex(field.getFieldValue(), "i"));
							}
						}

						if (orCriterias.size() > 0) {
							where.orOperator(orCriterias.toArray(new Criteria[0]));
						}
					}

					if (null != whereClause.getAnd() && whereClause.getAnd().size() > 0) {

						List<Criteria> andCriterias = new ArrayList<Criteria>(whereClause.getAnd().size());

						for (Field field : whereClause.getAnd()) {

							if (field.getOperator().equalsIgnoreCase(QueryOperator.IS)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(BOOLEAN_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(Boolean.valueOf(field.getFieldValue())));
									}
								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).is(field.getFieldValue()));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToTime(field.getFieldValue())));

									}
								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToTime(field.getFieldValue())));
									}
								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToDateTime(field.getFieldValue())));
									}
									if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToTime(field.getFieldValue())));
									}
								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToTime(field.getFieldValue())));
									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(field.getFieldValue()));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.NE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToDateTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToTime(field.getFieldValue())));

									}
									else if (field.getFieldType().equals(BOOLEAN_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(Boolean.valueOf(field.getFieldValue())));
									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(field.getFieldValue()));

								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.IN)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										String[] dates = field.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToDateTime(dates[i]));

										}

										andCriterias.add(Criteria.where(collection + field.getFieldName()).in(datesList));

									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										String[] dates = field.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToTime(dates[i]));

										}

										andCriterias.add(Criteria.where(collection + field.getFieldName()).in(datesList));

									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).in(Arrays.asList(whereClause.getFieldValue().split(","))));

								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LIKE)) {

								andCriterias.add(Criteria.where(collection + field.getFieldName()).regex(field.getFieldValue(), "i"));
							}
						}

						if (andCriterias.size() > 0) {
							where.andOperator(andCriterias.toArray(new Criteria[0]));
						}
					}

					if (whereClause.getFieldName() != null && whereClause.getFieldValue() != null) {

						if (whereClause.getOperator().equals(QueryOperator.IS)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).is(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).is(convertStringToTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(BOOLEAN_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).is(Boolean.valueOf(whereClause.getFieldValue())));

								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).is(whereClause.getFieldValue()));

							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.GT)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gt(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gt(convertStringToTime(whereClause.getFieldValue())));
								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gt(whereClause.getFieldValue()));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LT)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lt(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lt(convertStringToTime(whereClause.getFieldValue())));
								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lt(whereClause.getFieldValue()));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LTE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lte(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lte(convertStringToTime(whereClause.getFieldValue())));
								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).lte(whereClause.getFieldValue()));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.NE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).ne(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).ne(convertStringToTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(BOOLEAN_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).ne(Boolean.valueOf(whereClause.getFieldValue())));

								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).ne(whereClause.getFieldValue()));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.IN)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									String[] dates = whereClause.getFieldValue().split(",");

									List<Date> datesList = new ArrayList<Date>();

									for (int i = 0; i < dates.length; i++) {

										datesList.add(convertStringToDateTime(dates[i]));

									}

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									String[] dates = whereClause.getFieldValue().split(",");

									List<Date> datesList = new ArrayList<Date>();

									for (int i = 0; i < dates.length; i++) {

										datesList.add(convertStringToTime(dates[i]));

									}

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

								}

							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).in(Arrays.asList(whereClause.getFieldValue().split(","))));

							}

						}
						else if (whereClause.getOperator().equals(QueryOperator.GTE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gte(convertStringToDateTime(whereClause.getFieldValue())));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gte(convertStringToTime(whereClause.getFieldValue())));
								}
							}
							else {

								query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).gte(whereClause.getFieldValue()));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LIKE)) {

							query.addCriteria(Criteria.where(collection + whereClause.getFieldName()).regex(whereClause.getFieldValue(), "i"));
						}
					}

					wheres.add(where);
				}

				if (wheres.size() > 0) {

					query.addCriteria(new Criteria().andOperator(wheres.toArray(new Criteria[0])));

				}
			}

			if (null != search.getQuery().getOrderBy() && search.getQuery().getOrderBy().size() > 0) {

				for (OrderBy orderBy : search.getQuery().getOrderBy()) {

					if (orderBy.getDirection().equalsIgnoreCase(QueryOperator.ASC)) {

						query.with(new Sort(Sort.Direction.ASC, collection + orderBy.getField()));
					}
					else {

						query.with(new Sort(Sort.Direction.DESC, collection + orderBy.getField()));
					}
				}

			}
		}

		query.addCriteria(Criteria.where("isDeleted").is(false));

		return query;
	}

	public static Criteria parseQuery(org.iqvis.nvolv3.search.Criteria search, String collection) {

		Criteria userCriteria = new Criteria();

		if (search == null) {

			search = new org.iqvis.nvolv3.search.Criteria();
		}

		if (null != search.getQuery()) {

			if (null != search.getQuery().getWhere() && search.getQuery().getWhere().size() > 0) {
				List<Criteria> wheres = new ArrayList<Criteria>();

				for (Where whereClause : search.getQuery().getWhere()) {

					Criteria where = new Criteria();

					if (null != whereClause.getOr() && whereClause.getOr().size() > 0) {

						List<Criteria> orCriterias = new ArrayList<Criteria>(whereClause.getOr().size());

						for (Field field : whereClause.getOr()) {

							// String fieldValue =
							// Constants.CASE_IN_SENSITIVE_PRE+field.getFieldValue()+Constants.CASE_IN_SENSITIVE_POST;
							String fieldValue = field.getFieldValue();
							if (field.getOperator().equalsIgnoreCase(QueryOperator.IS)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToDateTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).is(Boolean.valueOf(fieldValue)));
									}

								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).is(fieldValue));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToDateTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToTime(fieldValue)));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).lt(fieldValue));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToDateTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToTime(fieldValue)));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).gt(fieldValue));

								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToDateTime(fieldValue)));

									}
									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToTime(fieldValue)));
									}
								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).lte(fieldValue));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToDateTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToTime(fieldValue)));
									}

								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).gte(fieldValue));

								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.NE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToDateTime(fieldValue)));
									}
									if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToTime(fieldValue)));

									}
									else if (field.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

										orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(Boolean.valueOf(fieldValue)));
									}

								}
								else {
									orCriterias.add(Criteria.where(collection + field.getFieldName()).ne(fieldValue));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.IN)) {

								if (whereClause.getFieldType() != null) {

									if (whereClause.getFieldType().equals(DATE_VALUE)) {

										String[] dates = whereClause.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToDateTime(dates[i]));

										}

										orCriterias.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

									}
									else if (whereClause.getFieldType().equals(DATE_VALUE)) {

										String[] dates = whereClause.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToTime(dates[i]));

										}

										orCriterias.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

									}

								}
								else {

									orCriterias.add(Criteria.where(collection + field.getFieldName()).in(Arrays.asList(fieldValue.split(","))));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LIKE)) {

								orCriterias.add(Criteria.where(collection + field.getFieldName()).regex(fieldValue, "i"));
							}
						}

						// where.orOperator((Criteria) orCriterias);
						where.orOperator(orCriterias.toArray(new Criteria[0]));
					}

					if (null != whereClause.getAnd() && whereClause.getAnd().size() > 0) {

						List<Criteria> andCriterias = new ArrayList<Criteria>(whereClause.getAnd().size());

						for (Field field : whereClause.getAnd()) {

							// String fieldValue =
							// Constants.CASE_IN_SENSITIVE_PRE+field.getFieldValue()+Constants.CASE_IN_SENSITIVE_POST;
							String fieldValue = field.getFieldValue();

							if (field.getOperator().equalsIgnoreCase(QueryOperator.IS)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToDateTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(convertStringToTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).is(Boolean.valueOf(fieldValue)));
									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).is(fieldValue));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equals(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToDateTime(fieldValue)));
									}
									else if (field.getFieldType().equals(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(convertStringToTime(fieldValue)));
									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).lt(fieldValue));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GT)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToDateTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(convertStringToTime(fieldValue)));
									}

								}
								else {
									andCriterias.add(Criteria.where(collection + field.getFieldName()).gt(fieldValue));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToDateTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(convertStringToTime(fieldValue)));
									}
								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).lte(fieldValue));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.GTE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToDateTime(fieldValue)));
									}

									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(convertStringToTime(fieldValue)));
									}
								}
								else {
									andCriterias.add(Criteria.where(collection + field.getFieldName()).gte(fieldValue));
								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.NE)) {

								if (field.getFieldType() != null) {

									if (field.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToDateTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(convertStringToTime(fieldValue)));
									}
									else if (field.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

										andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(Boolean.valueOf(fieldValue)));
									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).ne(fieldValue));

								}

							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.IN)) {

								if (whereClause.getFieldType() != null) {

									if (whereClause.getFieldType().equals(DATE_VALUE)) {

										String[] dates = whereClause.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToDateTime(dates[i]));

										}

										andCriterias.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

									}
									else if (whereClause.getFieldType().equals(TIME_VALUE)) {

										String[] dates = whereClause.getFieldValue().split(",");

										List<Date> datesList = new ArrayList<Date>();

										for (int i = 0; i < dates.length; i++) {

											datesList.add(convertStringToTime(dates[i]));

										}

										andCriterias.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

									}

								}
								else {

									andCriterias.add(Criteria.where(collection + field.getFieldName()).in(Arrays.asList(fieldValue.split(","))));
								}
							}
							else if (field.getOperator().equalsIgnoreCase(QueryOperator.LIKE)) {

								andCriterias.add(Criteria.where(collection + field.getFieldName()).regex(fieldValue, "i"));
							}
						}

						where.andOperator(andCriterias.toArray(new Criteria[0]));

					}

					if (whereClause.getFieldName() != null && whereClause.getFieldValue() != null) {

						// String fieldValue =
						// Constants.CASE_IN_SENSITIVE_PRE+whereClause.getFieldValue()+Constants.CASE_IN_SENSITIVE_POST;
						String fieldValue = whereClause.getFieldValue();
						if (whereClause.getOperator().equals(QueryOperator.IS)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).is(convertStringToDateTime(fieldValue)));

								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).is(convertStringToTime(fieldValue)));

								}
								else if (whereClause.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).is(Boolean.valueOf(fieldValue)));

								}

							}
							else {
								wheres.add(Criteria.where(collection + whereClause.getFieldName()).is(fieldValue));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.GT)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).gt(convertStringToDateTime(fieldValue)));

								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).gt(convertStringToTime(fieldValue)));
								}

							}
							else {

								wheres.add(Criteria.where(collection + whereClause.getFieldName()).gt(fieldValue));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LT)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).lt(convertStringToDateTime(fieldValue)));
								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).lt(convertStringToTime(fieldValue)));
								}
							}
							else {
								wheres.add(Criteria.where(collection + whereClause.getFieldName()).lt(fieldValue));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LTE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).lte(convertStringToDateTime(fieldValue)));
								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).lte(convertStringToTime(fieldValue)));
								}
							}
							else {

								wheres.add(Criteria.where(collection + whereClause.getFieldName()).lte(fieldValue));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.GTE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).gte(convertStringToDateTime(fieldValue)));
								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).gte(convertStringToTime(fieldValue)));
								}
							}
							else {

								wheres.add(Criteria.where(collection + whereClause.getFieldName()).gte(fieldValue));

							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.NE)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equalsIgnoreCase(DATE_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).ne(convertStringToDateTime(fieldValue)));

								}
								else if (whereClause.getFieldType().equalsIgnoreCase(TIME_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).ne(convertStringToTime(fieldValue)));

								}
								else if (whereClause.getFieldType().equalsIgnoreCase(BOOLEAN_VALUE)) {

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).ne(Boolean.valueOf(fieldValue)));

								}

							}
							else {
								wheres.add(Criteria.where(collection + whereClause.getFieldName()).ne(fieldValue));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.IN)) {

							if (whereClause.getFieldType() != null) {

								if (whereClause.getFieldType().equals(DATE_VALUE)) {

									String[] dates = whereClause.getFieldValue().split(",");

									List<Date> datesList = new ArrayList<Date>();

									for (int i = 0; i < dates.length; i++) {

										datesList.add(convertStringToDateTime(dates[i]));

									}

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

								}
								else if (whereClause.getFieldType().equals(TIME_VALUE)) {

									String[] dates = whereClause.getFieldValue().split(",");

									List<Date> datesList = new ArrayList<Date>();

									for (int i = 0; i < dates.length; i++) {

										datesList.add(convertStringToTime(dates[i]));

									}

									wheres.add(Criteria.where(collection + whereClause.getFieldName()).in(datesList));

								}

							}
							else {

								wheres.add(Criteria.where(collection + whereClause.getFieldName()).in(Arrays.asList(fieldValue.split(","))));
							}
						}
						else if (whereClause.getOperator().equals(QueryOperator.LIKE)) {

							wheres.add(Criteria.where(collection + whereClause.getFieldName()).regex(fieldValue, "i"));
						}
					}

					wheres.add(where);
				}

				userCriteria = new Criteria().andOperator(wheres.toArray(new Criteria[0]));

			}
		}

		return userCriteria;
	}

	public static boolean validateFile(MultipartFile mfile) throws IllegalStateException, IOException {

		File file = multipartToFile(mfile, "");

		String extention = FilenameUtils.getExtension(file.getAbsolutePath());

		if (extention.equalsIgnoreCase("exe")) {

			return false;
		}

		return true;
	}

	public static List<MultiLingual> updateMultiLingual(List<MultiLingual> existingLanguages, List<MultiLingual> newLanguages) throws Exception {

		List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			for (MultiLingual newLanguage : newLanguages) {

				if (newLanguage.getLanguageCode() != null) {

					if (existingLanguages.contains(newLanguage)) {

						for (MultiLingual existingLanguage : finalLanguages) {

							if (newLanguage.getLanguageCode().equalsIgnoreCase(existingLanguage.getLanguageCode())) {

								if (null != newLanguage.getTitle() && !StringUtils.isEmpty(newLanguage.getTitle())) {

									existingLanguage.setTitle(newLanguage.getTitle());
								}

								if (null != newLanguage.getDescriptionShort() && !StringUtils.isEmpty(newLanguage.getDescriptionShort())) {

									existingLanguage.setDescriptionShort(newLanguage.getDescriptionShort());
								}

								if (null != newLanguage.getDescriptionLong() && !StringUtils.isEmpty(newLanguage.getDescriptionLong())) {

									existingLanguage.setDescriptionLong(newLanguage.getDescriptionLong());
								}

								break;
							}

						}
					}
					else {

						finalLanguages.add(newLanguage);
					}

				}
				else {

					throw new Exception("Language code cannnot be null or empty.");
				}
			}
		}
		else {

			finalLanguages = newLanguages;
		}

		return finalLanguages;
	}

	public static List<MultiLingualAddresses> updateMultiLingualAddress(List<MultiLingualAddresses> existingLanguages, List<MultiLingualAddresses> newLanguages) throws Exception {

		List<MultiLingualAddresses> finalLanguages = new ArrayList<MultiLingualAddresses>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			for (MultiLingualAddresses newLanguage : newLanguages) {

				if (newLanguage.getLanguageCode() != null) {

					if (existingLanguages.contains(newLanguage)) {

						for (MultiLingualAddresses existingLanguage : finalLanguages) {

							if (newLanguage.getLanguageCode().equalsIgnoreCase(existingLanguage.getLanguageCode())) {

								if (null != newLanguage.getTitle() && !StringUtils.isEmpty(newLanguage.getTitle())) {

									existingLanguage.setTitle(newLanguage.getTitle());
								}

								if (null != newLanguage.getDescriptionShort() && !StringUtils.isEmpty(newLanguage.getDescriptionShort())) {

									existingLanguage.setDescriptionShort(newLanguage.getDescriptionShort());
								}

								if (null != newLanguage.getDescriptionLong() && !StringUtils.isEmpty(newLanguage.getDescriptionLong())) {

									existingLanguage.setDescriptionLong(newLanguage.getDescriptionLong());
								}

								if (null != newLanguage.getAddress1() && !StringUtils.isEmpty(newLanguage.getAddress1())) {

									existingLanguage.setAddress1(newLanguage.getAddress1());
								}

								if (null != newLanguage.getAddress2() && !StringUtils.isEmpty(newLanguage.getAddress2())) {

									existingLanguage.setAddress2(newLanguage.getAddress2());
								}

								if (null != newLanguage.getCity() && !StringUtils.isEmpty(newLanguage.getCity())) {

									existingLanguage.setCity(newLanguage.getCity());
								}

								if (null != newLanguage.getState() && !StringUtils.isEmpty(newLanguage.getState())) {

									existingLanguage.setState(newLanguage.getState());
								}

								if (null != newLanguage.getZip() && !StringUtils.isEmpty(newLanguage.getZip())) {

									existingLanguage.setZip(newLanguage.getZip());
								}

								if (null != newLanguage.getCountry() && !StringUtils.isEmpty(newLanguage.getCountry())) {

									existingLanguage.setCountry(newLanguage.getCountry());
								}

								if (null != newLanguage.getCountryCode() && !StringUtils.isEmpty(newLanguage.getCountryCode())) {

									existingLanguage.setCountryCode(newLanguage.getCountryCode());
								}

								if (null != newLanguage.getStateCode() && !StringUtils.isEmpty(newLanguage.getStateCode())) {

									existingLanguage.setStateCode(newLanguage.getStateCode());
								}

								break;
							}

						}
					}
					else {

						finalLanguages.add(newLanguage);
					}

				}
				else {

					throw new Exception("Language code cannnot be null or empty.");
				}
			}
		}
		else {

			finalLanguages = newLanguages;
		}

		return finalLanguages;
	}

	public static List<MultiLingualPersonnelInformation> updateMultiLingualPersonnel(List<MultiLingualPersonnelInformation> existingLanguages, List<MultiLingualPersonnelInformation> newLanguages) throws Exception {

		List<MultiLingualPersonnelInformation> finalLanguages = new ArrayList<MultiLingualPersonnelInformation>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			for (MultiLingualPersonnelInformation newLanguage : newLanguages) {

				if (newLanguage.getLanguageCode() != null) {

					if (existingLanguages.contains(newLanguage)) {

						for (MultiLingualPersonnelInformation existingLanguage : finalLanguages) {

							if (newLanguage.getLanguageCode().equalsIgnoreCase(existingLanguage.getLanguageCode())) {

								if (null != newLanguage.getTitle() && !StringUtils.isEmpty(newLanguage.getTitle())) {

									existingLanguage.setTitle(newLanguage.getTitle());
								}

								if (null != newLanguage.getDescriptionShort()) {

									existingLanguage.setDescriptionShort(newLanguage.getDescriptionShort());
								}

								if (null != newLanguage.getDescriptionLong()) {

									existingLanguage.setDescriptionLong(newLanguage.getDescriptionLong());
								}

								if (null != newLanguage.getBio()) {

									existingLanguage.setBio(newLanguage.getBio());
								}

								if (null != newLanguage.getOrganization()) {

									existingLanguage.setOrganization(newLanguage.getOrganization());
								}

								if (null != newLanguage.getQualification()) {

									existingLanguage.setQualification(newLanguage.getQualification());
								}

								if (null != newLanguage.getDesignation()) {

									existingLanguage.setDesignation(newLanguage.getDesignation());
								}

								break;
							}

						}
					}
					else {

						finalLanguages.add(newLanguage);
					}

				}
				else {

					throw new Exception("Language code cannnot be null or empty.");
				}
			}
		}
		else {

			finalLanguages = newLanguages;
		}

		return finalLanguages;
	}

	public static List<MultiLingual> getMultiLingualByLangCode(List<MultiLingual> existingLanguages, String code) throws Exception {

		List<MultiLingual> finalLanguages = new ArrayList<MultiLingual>(existingLanguages == null ? new ArrayList<MultiLingual>() : existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingual.class).getLanguageCode(), Matchers.equalToIgnoringCase(code + "")));
		}

		return finalLanguages;
	}

	public static List<MultiLingual> getMultiLingualByLangCode(List<MultiLingual> existingLanguages, String code, String event_default) throws Exception {

		List<MultiLingual> finalLanguages = getMultiLingualByLangCode(existingLanguages, code);

		if (finalLanguages.size() <= 0) {
			finalLanguages = getMultiLingualByLangCode(existingLanguages, event_default);
		}

		if (finalLanguages.size() <= 0) {
			finalLanguages = getMultiLingualByLangCode(existingLanguages, Constants.APPLICATION_DEFAULT_LANGUAGE);
		}

		return finalLanguages;
	}

	public static List<MultiLingualPersonnelInformation> getPersonnelMultiLingualByLangCode(List<MultiLingualPersonnelInformation> existingLanguages, String code) throws Exception {

		List<MultiLingualPersonnelInformation> finalLanguages = new ArrayList<MultiLingualPersonnelInformation>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualPersonnelInformation.class).getLanguageCode(), Matchers.equalToIgnoringCase(code + "")));
		}

		return finalLanguages;
	}

	public static List<MultiLingualAddresses> getMultiLingualAddressesByLangCode(List<MultiLingualAddresses> existingLanguages, String code) throws Exception {

		List<MultiLingualAddresses> finalLanguages = new ArrayList<MultiLingualAddresses>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualAddresses.class).getLanguageCode(), Matchers.equalToIgnoringCase(code)));
		}

		return finalLanguages;
	}

	public static List<MultiLingualAddresses> getMultiLingualAddressesByLangCode(List<MultiLingualAddresses> existingLanguages, String code, String event_default) throws Exception {

		List<MultiLingualAddresses> finalLanguages = new ArrayList<MultiLingualAddresses>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualAddresses.class).getLanguageCode(), Matchers.equalToIgnoringCase(code)));
		}

		if (finalLanguages != null && finalLanguages.size() <= 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualAddresses.class).getLanguageCode(), Matchers.equalToIgnoringCase(event_default)));
		}

		if (finalLanguages != null && finalLanguages.size() <= 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualAddresses.class).getLanguageCode(), Matchers.equalToIgnoringCase(Constants.APPLICATION_DEFAULT_LANGUAGE)));
		}

		return finalLanguages;
	}

	public static List<MultiLingualPersonnelInformation> getMultiLingualPersonnelInformationByLangCode(List<MultiLingualPersonnelInformation> existingLanguages, String code) throws Exception {

		List<MultiLingualPersonnelInformation> finalLanguages = new ArrayList<MultiLingualPersonnelInformation>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualPersonnelInformation.class).getLanguageCode(), Matchers.equalToIgnoringCase(code)));
		}

		return finalLanguages;
	}

	public static List<MultiLingualPersonnelInformation> getMultiLingualPersonnelInformationByLangCode(List<MultiLingualPersonnelInformation> existingLanguages, String code, String event_default_lang) throws Exception {

		List<MultiLingualPersonnelInformation> finalLanguages = new ArrayList<MultiLingualPersonnelInformation>(existingLanguages);

		if (finalLanguages != null && finalLanguages.size() > 0) {

			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualPersonnelInformation.class).getLanguageCode(), Matchers.equalToIgnoringCase(code)));
		}

		if (finalLanguages.size() <= 0) {
			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualPersonnelInformation.class).getLanguageCode(), Matchers.equalToIgnoringCase(event_default_lang)));
		}

		if (finalLanguages.size() <= 0) {
			finalLanguages = Lambda.select(existingLanguages, Lambda.having(Lambda.on(MultiLingualPersonnelInformation.class).getLanguageCode(), Matchers.equalToIgnoringCase(Constants.APPLICATION_DEFAULT_LANGUAGE)));
		}

		return finalLanguages;
	}

	public static String getSHA512Hash(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

		MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");

		digest.update(password.getBytes());

		byte messageDigest[] = digest.digest();

		// Create Hex String
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < messageDigest.length; i++) {

			String h = Integer.toHexString(0xFF & messageDigest[i]);

			while (h.length() < 2) {

				h = "0" + h;
			}

			hexString.append(h);
		}

		return hexString.toString();
	}

	public static String md5(String input) throws NoSuchAlgorithmException {

		String result = input;

		if (input != null) {

			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"

			md.update(input.getBytes());

			BigInteger hash = new BigInteger(1, md.digest());

			result = hash.toString(16);

			while (result.length() < 32) { // 40 for SHA-1

				result = "0" + result;
			}
		}
		System.out.print("MD5: " + result);

		return result;
	}

	public static Date convertStringToDateTime(String dateString) {

		Date date = null;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JodaDateTimeJsonSerializer.dateFormat);

		try {
			date = simpleDateFormat.parse(dateString);

			System.out.println("date : " + simpleDateFormat.format(date));
		}
		catch (ParseException ex) {
			System.out.println("Exception " + ex);
		}

		return date;
	}

	public static Date convertStringToTime(String dateString) {

		Date date = new Date();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimeJsonSerializer.dateFormat);

		try {
			date = simpleDateFormat.parse(dateString);

			System.out.println("date : " + simpleDateFormat.format(date));
		}
		catch (ParseException ex) {
			System.out.println("Exception " + ex);
		}

		return date;
	}

	/***
	 * DateTime Conveters
	 * ***/
	public static String convertTimeZones(final String fromTimeZoneString, final String toTimeZoneString, final String fromDateTime) {

		final DateTimeZone fromTimeZone = DateTimeZone.forID(fromTimeZoneString);

		final DateTimeZone toTimeZone = DateTimeZone.forID(toTimeZoneString);

		final DateTime dateTime = new DateTime(fromDateTime, fromTimeZone);

		final org.joda.time.format.DateTimeFormatter outputFormatter = DateTimeFormat.forPattern("yyyy-MM-dd H:mm:ss").withZone(toTimeZone);

		return outputFormatter.print(dateTime);
	}

	public static DateTime convertTimeZones(final String fromTimeZoneString, final String toTimeZoneString, final DateTime fromDateTime, String format) {

		final DateTimeZone fromTimeZone = DateTimeZone.forID(fromTimeZoneString);

		final DateTimeZone toTimeZone = DateTimeZone.forID(toTimeZoneString);

		final DateTime dateTime = new DateTime(fromDateTime, fromTimeZone);

		final org.joda.time.format.DateTimeFormatter outputFormatter = DateTimeFormat.forPattern(format).withZone(toTimeZone);

		return new DateTime(outputFormatter.print(dateTime));
	}

	// public static ZoneId getTimeZoneFromOffset(String offsetString){
	// Set<String> allZones = ZoneId.getAvailableZoneIds();
	// LocalDateTime dt = LocalDateTime.now();
	//
	// // Create a List using the set of zones and sort it.
	// List<String> zoneList = new ArrayList<String>(allZones);
	//
	// Collections.sort(zoneList);
	//
	// for (String s : zoneList) {
	//
	// ZoneId zone = ZoneId.of(s);
	//
	// ZonedDateTime zdt = dt.atZone(zone);
	//
	// ZoneOffset offset = zdt.getOffset();
	//
	// String out = String.format("%s", offset);
	//
	// System.out.println(out);
	//
	// if (out.trim().equals(offsetString)) {
	//
	// return zone;
	//
	// }
	//
	// }
	//
	// return null;
	// }
	//

	public static String toGMT(DateTime dateTime, String timeOffset) {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, dateTime.getYear());

		c.set(Calendar.MONTH, dateTime.getMonthOfYear() - 1);

		c.set(Calendar.DAY_OF_MONTH, dateTime.getDayOfMonth());

		c.set(Calendar.HOUR_OF_DAY, dateTime.getHourOfDay());

		c.set(Calendar.MINUTE, dateTime.getMinuteOfHour());

		c.set(Calendar.SECOND, dateTime.getSecondOfMinute());

		String offsets[] = timeOffset.split(":");

		int multi = -1, minMulti = -1;

		if (Integer.parseInt(offsets[0]) < 0) {

			multi = -1;

			minMulti = 1;

		}

		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(offsets[0]) * multi);

		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + Integer.parseInt(offsets[1]) * minMulti);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

		return formater.print(new DateTime(c.getTime()));

	}

	public static String toEventTimeZoon(DateTime dateTime, String timeOffset) {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, dateTime.getYear());

		c.set(Calendar.MONTH, dateTime.getMonthOfYear() - 1);

		c.set(Calendar.DAY_OF_MONTH, dateTime.getDayOfMonth());

		c.set(Calendar.HOUR_OF_DAY, dateTime.getHourOfDay());

		c.set(Calendar.MINUTE, dateTime.getMinuteOfHour());

		c.set(Calendar.SECOND, dateTime.getSecondOfMinute());

		String offsets[] = timeOffset.split(":");

		int multi = 1, minMulti = 1;

		if (Integer.parseInt(offsets[0]) < 0) {

			multi = 1;

			minMulti = -1;

		}

		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(offsets[0]) * multi);

		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + Integer.parseInt(offsets[1]) * minMulti);

		DateTimeFormatter formater = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

		return formater.print(new DateTime(c.getTime()));

	}

	/**
	 * Test Driver For Utils Class
	 * 
	 * *
	 * 
	 * @throws ParseException
	 **/

	public static DateTime setTimeToDate(String time, DateTime dateTime) throws ParseException {

		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy/MM/dd");

		String date = df.print(dateTime);

		String dateTimeString = date + " " + time;

		DateFormat dformat = new SimpleDateFormat("yyyy/MM/dd hh:mm aa");

		Date dateObject = dformat.parse(dateTimeString);

		return new DateTime(dateObject);
	}

	public static EventConfiguration toEventConfiguration(Object eventConfiguration) {

		Gson gson = new Gson();

		String jsonEventConfiguration = gson.toJson(eventConfiguration);

		return gson.fromJson(jsonEventConfiguration, EventConfiguration.class);

	}

	public static File applyOverLay(File imageFile, File overlayFile, String root) throws IOException {

		BufferedImage image = ImageIO.read(imageFile);

		BufferedImage overlay = ImageIO.read(overlayFile);

		int w = overlay.getWidth();

		int h = overlay.getHeight();

		Image image_ex = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);

		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		Graphics g = combined.getGraphics();

		g.drawImage(image_ex, 0, 0, null);

		g.drawImage(overlay, 0, 0, null);

		File tempFile = new File(root, System.currentTimeMillis() + ".jpg");

		ImageIO.write(combined, "JPG", tempFile);

		return tempFile;
	}

	public static File applyOverlay(File image, String media, String requestRootPath) throws IOException {

		String mainRoot = requestRootPath;

		String root = requestRootPath + "image\\cache\\";

		if (!new File(requestRootPath + "image").exists()) {

			new File(requestRootPath + "image").mkdir();

			new File(requestRootPath + "tem").mkdir();

			new File(root).mkdir();
		}

		URL url = new URL(media);

		media = media.replace("\\", "/");

		String[] mediaList = media.split("/");

		File overlayImage = new File(root, mediaList[mediaList.length - 1]);

		if (!overlayImage.exists()) {

			BufferedImage overlayImageBuffer = ImageIO.read(url);

			ImageIO.write(overlayImageBuffer, "PNG", overlayImage);

			logger.debug("File " + overlayImage + " moved to cache ");
		}

		logger.debug("Appling Overlay On Subject Image");

		return Utils.applyOverLay(image, overlayImage, mainRoot + "tem");
	}

	public static Object sendAlert(Object data) {
		Client client = Client.create();

		WebResource resource = client.resource(Constants.PUSH_NOFICATION_URL);

		// LIVE
		// Object bean = resource.header("X-Parse-Application-Id",
		// "6dvjrS8nnUH74VmhfRo8YKUwUpEQVGBgZ004ZK9p").header("X-Parse-REST-API-Key",
		// "8bPogCDvMynb1aKQ9dFEWb8xI6F1m6qBIlTXZsCC").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_OCTET_STREAM).post(Object.class,
		// data);

		// DEV
		// Object bean = resource.header("X-Parse-Application-Id",
		// "wz4PQquMgX6xenJHBHuMbNDPqEDJMq1IgR3UmfKu").header("X-Parse-REST-API-Key",
		// "ycF1BVuNltr5M1StM3LMVDoWskzp07XBGn51Wdnq").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_OCTET_STREAM).post(Object.class,
		// data);

		Object bean = resource.header("X-Parse-Application-Id", "sAoMnMaSdnf1DSLT5GxLt9I7q9I6rsXBTzeqPk95").header("X-Parse-REST-API-Key", "rxDGOT0w6sxdq1CyALmTM9qNRwLooggIicuSinnw").type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_OCTET_STREAM).post(Object.class, data);

		org.iqvis.nvolv3.push.Query query = (org.iqvis.nvolv3.push.Query) data;

		return bean;
	}

	public static Object sendAlert(Object data, String appId, String apiKey) {

		org.iqvis.nvolv3.push.Query query = (org.iqvis.nvolv3.push.Query) data;

		Client client = Client.create();

		WebResource resource = client.resource(Constants.PUSH_NOFICATION_URL);

		// Gson gson=new Gson();
		//
		// System.out.println(gson.toJson(data));

		if (query.getWhere().getDeviceToken().get$in() == null || query.getWhere().getDeviceToken().get$in().size() < 1) {

			return null;
		}

		Object bean = resource.header("X-Parse-Application-Id", appId).header("X-Parse-REST-API-Key", apiKey).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_OCTET_STREAM).post(Object.class, data);

		return bean;
	}

	public static org.iqvis.nvolv3.push.Query getQuery(List<String> deviceTokens) {

		org.iqvis.nvolv3.push.Query query = new org.iqvis.nvolv3.push.Query();

		org.iqvis.nvolv3.push.Where where = new org.iqvis.nvolv3.push.Where();

		DeviceToken deviceToken = new DeviceToken();

		deviceToken.set$in(deviceTokens);

		where.setDeviceToken(deviceToken);

		query.setWhere(where);

		return query;
	}

	public static Query getQueryForEventSearch(EventSearch eventSearch) {

		Query query = new Query();

		if (eventSearch != null) {

			if (eventSearch.getText() != null && eventSearch.getText() != "") {

				query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(eventSearch.getText())).sortByScore();

			}

			if (eventSearch.getLocation() != null && eventSearch.getLocation().trim() != "") {

				query = query.addCriteria(Criteria.where("isLive").is(true).orOperator(Criteria.where("venue.multiLingual.city").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.title").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.name").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.country").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.state").regex(eventSearch.getLocation().trim(), "i")));

			}
			else {

				query = query.addCriteria(Criteria.where("isLive").is(true));
			}

			if (eventSearch.getFromDateTime() != null && eventSearch.getToDateTime() == null) {

				query = query.addCriteria(Criteria.where("eventDates").gte(eventSearch.getFromDateTime().toDate()));

			}

			if (eventSearch.getToDateTime() != null && eventSearch.getFromDateTime() == null) {

				query = query.addCriteria(Criteria.where("eventDates").lte(eventSearch.getToDateTime().toDate()));
			}

			if (eventSearch.getToDateTime() != null && eventSearch.getFromDateTime() != null) {

				query = query.addCriteria(Criteria.where("eventDates").gte(eventSearch.getFromDateTime()).andOperator(Criteria.where("eventDates").lte(eventSearch.getToDateTime())));
			}

			if (eventSearch.getPartnerId() != null) {

				query = query.addCriteria(Criteria.where("partnerId").is(eventSearch.getPartnerId()));
			}

			query = query.addCriteria(Criteria.where("linkedApp.appId").is(eventSearch.getAppId()));

		}

		return query;
	}

	public static Query getQueryForEventSearchOrganizer(EventSearch eventSearch) {

		Query query = new Query();

		if (eventSearch != null) {

			if (eventSearch.getText() != null && eventSearch.getText() != "") {

				query = TextQuery.queryText(TextCriteria.forDefaultLanguage().matching(eventSearch.getText())).sortByScore();

			}

			if (eventSearch.getLocation() != null && eventSearch.getLocation().trim() != "") {

				query = query.addCriteria(Criteria.where("isLive").is(true).orOperator(Criteria.where("venue.multiLingual.city").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.title").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.name").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.country").regex(eventSearch.getLocation().trim(), "i"), Criteria.where("venue.multiLingual.state").regex(eventSearch.getLocation().trim(), "i")));

			}
			else {

				query = query.addCriteria(Criteria.where("isLive").is(true));
			}

			if (eventSearch.getFromDateTime() != null && eventSearch.getToDateTime() == null) {

				query = query.addCriteria(Criteria.where("eventDates").gte(eventSearch.getFromDateTime().toDate()));

			}

			if (eventSearch.getToDateTime() != null && eventSearch.getFromDateTime() == null) {

				query = query.addCriteria(Criteria.where("eventDates").lte(eventSearch.getToDateTime().toDate()));
			}

			if (eventSearch.getToDateTime() != null && eventSearch.getFromDateTime() != null) {

				query = query.addCriteria(Criteria.where("eventDates").gte(eventSearch.getFromDateTime()).andOperator(Criteria.where("eventDates").lte(eventSearch.getToDateTime())));
			}

			query = query.addCriteria(Criteria.where("organizerId").is(eventSearch.getPartnerId()));

		}

		return query;
	}

	public static String getThemeId(Object eventConfiguration) {

		Gson gson = new Gson();

		Map<String, Object> map = new HashMap<String, Object>();

		map = (Map<String, Object>) gson.fromJson(gson.toJson(eventConfiguration), map.getClass());

		if (map.containsKey("themeId") == true) {

			return (String) map.get("themeId");
		}

		return null;

	}

	public static Object getKeyFromObjectTypeConfiguration(Object eventConfiguration, String key) {

		if (eventConfiguration != null) {
			Gson gson = new Gson();

			Map<String, Object> map = new HashMap<String, Object>();

			map = (Map<String, Object>) gson.fromJson(gson.toJson(eventConfiguration), map.getClass());

			if (map.containsKey(key)) {

				return map.get(key);
			}

		}

		return null;
	}

	public static Object filterForEventConfiguration(Object eventConfiguration, String code, Object theme) {

		Gson gson = new Gson();

		Map<String, Object> map = new HashMap<String, Object>();

		map = (Map<String, Object>) gson.fromJson(gson.toJson(eventConfiguration), map.getClass());

		if (theme != null) {

			map.put("theme", theme);
		}

		if (map.containsKey("texts") == true) {

			String json = gson.toJson(map.get("texts"));

			System.out.println(code);

			List<DataCMS> texts = gson.fromJson(json, new TypeToken<List<DataCMS>>() {
			}.getType());

			for (DataCMS dataCMS : texts) {

				if (dataCMS.getMultiLingual() != null) {

					try {

						dataCMS.setMultiLingual(Utils.getMultiLingualByLangCode(dataCMS.getMultiLingual(), code));

					}
					catch (Exception e) {

						// e.printStackTrace();
					}

				}

			}

			List<LabelEntities> labels = new ArrayList<LabelEntities>();

			if (texts == null) {

				texts = new ArrayList<DataCMS>();
			}

			for (DataCMS dataCMS : texts) {

				LabelEntities label = new LabelEntities();

				label.setKey(dataCMS.getKey());

				if (dataCMS.getMultiLingual() != null && dataCMS.getMultiLingual().size() > 0) {

					label.setText(dataCMS.getMultiLingual().get(0).getTitle());
				}
				else {

					label.setText(dataCMS.getDefaultText());
				}

				labels.add(label);
			}

			EventTexts eventText = new EventTexts();

			eventText.setLanguageCode(code);

			eventText.setTexts(labels);

			map.put("texts", eventText);

		}

		String json = gson.toJson(map);

		return gson.fromJson(json, Object.class);
	}

	public static boolean isEmptyOrNull(Object object) {

		if (object == null) {

			return true;
		}

		if (object.getClass().toString().equals(String.class.toString())) {

			String string = (String) object;

			if (string == "") {

				return true;
			}

		}

		return false;

	}

	/*
	 * @param dbPath
	 * 
	 * @param ip
	 * 
	 * return
	 */

	public static String getCountryByGeoIPLocation(String dbPath, String ip) throws IOException, GeoIp2Exception {

		File database = new File(dbPath);

		DatabaseReader reader = new DatabaseReader.Builder(database).build();

		InetAddress ipAddress = InetAddress.getByName(ip);

		CityResponse response = reader.city(ipAddress);

		Country country = response.getCountry();

		return country.getName();
	}

	public static List<DataChangeLog> getUnique(List<DataChangeLog> list) {

		List<DataChangeLog> tempList = new ArrayList<DataChangeLog>();

		if (list != null) {

			for (DataChangeLog dataChangeLog : list) {

				if (!tempList.contains(dataChangeLog)) {

					tempList.add(dataChangeLog);
				}

				if (Constants.LOG_ACTION_DELETE.equals(dataChangeLog.getAction() + "")) {

					if (tempList.contains(dataChangeLog)) {

						tempList.remove(dataChangeLog);

					}

					tempList.add(dataChangeLog);
				}

			}
		}

		return tempList;
	}

	public static List<ListEvent> getFilteredEventListWightLabel(List<ListEvent> eventList) {

		List<ListEvent> eventtemp = new ArrayList<ListEvent>();

		if (eventList != null) {

			for (ListEvent listEvent : eventList) {

				if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEventDates().get(listEvent.getEventDates().size() - 1))) {

					eventtemp.add(listEvent);

				}
				else {

					eventtemp.add(listEvent);
				}
			}
		}

		List<ListEvent> eventOld = new ArrayList<ListEvent>();

		List<ListEvent> eventFuture = new ArrayList<ListEvent>();

		for (ListEvent listEvent : eventtemp) {

			if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEndDate())) {
				// TODO sort descending startDate
				eventOld.add(listEvent);

			}
			else {
				// TODO sort ascending startDate
				eventFuture.add(listEvent);
			}

		}

		int i = 0;

		Collections.sort(eventFuture, new EventObjectSort());

		for (ListEvent listEvent : eventFuture) {

			listEvent.setSortOrder(i++);
		}

		Collections.sort(eventOld, new EventObjectSortDesc());

		for (ListEvent listEvent : eventOld) {

			listEvent.setSortOrder(i++);
		}

		eventFuture.addAll(eventOld);

		return eventFuture;
	}

	public static List<ListEvent> getFilteredEventList(List<ListEvent> eventList, Integer lastDays) {

		lastDays = lastDays == null ? 0 : lastDays;

		List<ListEvent> eventtemp = new ArrayList<ListEvent>();

		if (eventList != null) {

			for (ListEvent listEvent : eventList) {

				if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEventDates().get(listEvent.getEventDates().size() - 1))) {

					long diff = DateTime.now(DateTimeZone.UTC).toDate().getTime() - listEvent.getEventDates().get(listEvent.getEventDates().size() - 1).toDate().getTime();

					long days = TimeUnit.MILLISECONDS.toDays(diff);
					// TODO make this check configurable

					if (days < lastDays) {

						eventtemp.add(listEvent);
					}

				}
				else {

					eventtemp.add(listEvent);
				}
			}
		}

		List<ListEvent> eventOld = new ArrayList<ListEvent>();

		List<ListEvent> eventFuture = new ArrayList<ListEvent>();

		for (ListEvent listEvent : eventtemp) {

			if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEndDate())) {
				// TODO sort descending startDate
				eventOld.add(listEvent);

			}
			else {
				// TODO sort ascending startDate
				eventFuture.add(listEvent);
			}

		}

		int i = 0;

		Collections.sort(eventFuture, new EventObjectSort());

		for (ListEvent listEvent : eventFuture) {

			listEvent.setSortOrder(i++);
		}

		Collections.sort(eventOld, new EventObjectSortDesc());

		for (ListEvent listEvent : eventOld) {

			listEvent.setSortOrder(i++);
		}

		eventFuture.addAll(eventOld);

		return eventFuture;
	}

	public static List<Event> getFilteredEventDomainList(List<Event> eventList, Integer lastDays) {

		lastDays = lastDays == null ? 0 : lastDays;

		List<Event> eventtemp = new ArrayList<Event>();

		if (eventList != null) {

			for (Event listEvent : eventList) {

				if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEventDates().get(listEvent.getEventDates().size() - 1))) {

					long diff = DateTime.now(DateTimeZone.UTC).toDate().getTime() - listEvent.getEventDates().get(listEvent.getEventDates().size() - 1).toDate().getTime();

					long days = TimeUnit.MILLISECONDS.toDays(diff);
					// TODO make this check configurable

					if (days < lastDays) {

						eventtemp.add(listEvent);
					}

				}
				else {

					eventtemp.add(listEvent);
				}
			}
		}

		List<Event> eventOld = new ArrayList<Event>();

		List<Event> eventFuture = new ArrayList<Event>();

		for (Event listEvent : eventtemp) {

			if (DateTime.now(DateTimeZone.UTC).isAfter(listEvent.getEndDate())) {
				// TODO sort descending startDate
				eventOld.add(listEvent);

			}
			else {
				// TODO sort ascending startDate
				eventFuture.add(listEvent);
			}

		}

		Collections.sort(eventFuture, new EventDomainObjectSort());

		Collections.sort(eventOld, new EventDomainObjectSortDesc());

		eventFuture.addAll(eventOld);

		return eventFuture;
	}

	public static List<Event> sortList(List<Event> events, Integer lastDays) {

		Map<Float, List<Event>> map = new HashMap<Float, List<Event>>();

		List<Float> keys = new ArrayList<Float>();

		for (Event event : events) {

			if (map.containsKey(event.getScore())) {

				map.get(event.getScore()).add(event);
			}
			else {

				keys.add(event.getScore());

				List<Event> list = new ArrayList<Event>();

				list.add(event);

				map.put(event.getScore(), list);
			}
		}

		List<Event> finalList = new ArrayList<Event>();

		for (Float score : keys) {

			finalList.addAll(Utils.getFilteredEventDomainList(map.get(score), lastDays));

		}

		return finalList;
	}

	public static class EventObjectSort implements Comparator<ListEvent> {

		public int compare(ListEvent o1, ListEvent o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2) * -1;
		}
	}

	public static class EventObjectSortDesc implements Comparator<ListEvent> {

		public int compare(ListEvent o1, ListEvent o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

	public static class EventDomainObjectSort implements Comparator<Event> {

		public int compare(Event o1, Event o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2) * -1;
		}
	}

	public static class EventDomainObjectSortDesc implements Comparator<Event> {

		public int compare(Event o1, Event o2) {
			// write comparison logic here like below , it's just a sample
			return o1.compareTo(o2);
		}
	}

	public static <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
		if (c == null)
			return Collections.emptyList();
		List<T> list = new ArrayList<T>(c);
		if (pageSize == null || pageSize <= 0 || pageSize > list.size())
			pageSize = list.size();
		int numPages = (int) Math.ceil((double) list.size() / (double) pageSize);
		List<List<T>> pages = new ArrayList<List<T>>(numPages);
		for (int pageNum = 0; pageNum < numPages;)
			pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
		return pages;
	}

	public static TimeZoneResponse getTimeZone(double longi, double lati, DateTime dateTime) {

		Client client = Client.create();

		MessageFormat messageFormat = new MessageFormat(Constants.GOOGLE_TIMEZONE_API_URL + "location={0},{1}&timestamp={2}&language=es");

		//int timeStamp = ((Long) new java.sql.Timestamp(dateTime.toDate().getTime()).getTime()).intValue();

		String timeStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
		
		// new Timestamp(dateTime.toDate().getTime()).getTimeTicks().intValue()

		Object[] args = { longi + "", lati + "", timeStamp + "" };

		WebResource resource = client.resource(messageFormat.format(args));

		System.out.println(messageFormat.format(args));

		return resource.accept(MediaType.APPLICATION_JSON).get(TimeZoneResponse.class);

	}
	
	public static TimeZoneResponse getTimeZone(double longi, double lati, Date dateTime) {

		Client client = Client.create();

		MessageFormat messageFormat = new MessageFormat(Constants.GOOGLE_TIMEZONE_API_URL + "location={0},{1}&timestamp={2}&language=es");

		//int timeStamp = ((Long) new java.sql.Timestamp(dateTime.toDate().getTime()).getTime()).intValue();

		//String timeStamp = new SimpleDateFormat("ddMMyyyy").format(dateTime);
		
		// new Timestamp(dateTime.toDate().getTime()).getTimeTicks().intValue()

		int timeStamp= (int) (dateTime.getTime()/1000);
		
		Object[] args = { longi + "", lati + "", timeStamp + "" };

		WebResource resource = client.resource(messageFormat.format(args));

		System.out.println(messageFormat.format(args));

		return resource.accept(MediaType.APPLICATION_JSON).get(TimeZoneResponse.class);

	}
	

	@SuppressWarnings("deprecation")
	public static Date dateTime(Date date, Date time) {
	    return new Date(
	                     date.getYear(), date.getMonth(), date.getDay(), 
	                     time.getHours(), time.getMinutes(), time.getSeconds()
	                   );
	}
	
	
	public static void main(String args[]) throws ParseException, IOException {

		// double x = 37.0000, y = -120.0000;
		//
		// TimeZoneResponse timeZoneResponse = Utils.getTimeZone(x, y,
		// DateTime.now());
		//
		// double rawOffSet = timeZoneResponse.getRawOffset() / 3600;
		//
		// double dstOffSet = timeZoneResponse.getDstOffset() / 3600;
		//
		// double sumTotalOffSet = rawOffSet + dstOffSet;
		//
		// System.out.println(String.format("%.2f", sumTotalOffSet).replace(".",
		// ":"));

		// String data =
		// "{   \"where\": {   \"deviceType\" : \"ios\" , \"deviceToken\": {\"$in\":[\"e33dfab7302be6d2c3fa84b7ff2656449db6fbd9b1d064edbde69c6d05c5f0d0\",\"0f986b157523e0ac338eeff7a23fff0dbfd058127dbf2fbe62948e1a39cb7adb\"] } }, \"data\" : { \"alert\" :  \"Sending from Java Client :-)\" }}";

		// Gson gson = new Gson();

		// FacebookController.postOnFacebookWall("CAAOGDrDPRVsBACL0luu7VeMcJEADRdlmuI2w4aDJpOxOTZAOwymtJ265vZAkeqrOfB7CxHVhZBS61sPvVn18u1c4FMxUrPuNkehcUdsetZBcxtctDZCZBEyQBLqXNXoG6fKHPjPDBfKfAi5Ykoh5RHfKu07CCLWj4DD4ioonFCiavU6GLel63fwbLeCIGrcJpF8TVs66Q8V1FrcfLPDQeu",
		// "UPdload","google.com");

		// FacebookController.postOnFacebookWall("CAALm72Bc7GYBAEvZAFoCuL3VyOoYqASkVTInCdtv5vNPbP8psI6xClCm4oH6Rga3yPHRNpnuXlGI2FTvErHkaVM04rsoXSlTlTZCog73zHuzDduR7FENkle3F7lXj3QyIVlz9tDbbAZCYL2GDfz2F1knMt006qMr0aIH8WpMWCdFOhNBUZAH2WI91c4J9MRgQeEiSvdpXOHGI1OUiNkz",
		// "UPdload", "google.com");

		// Utils.sendAlert(gson.fromJson(data, Object.class));

	}
}
