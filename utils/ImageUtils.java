package org.iqvis.nvolv3.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

	public static boolean validateImageFile(MultipartFile mfile) throws IllegalStateException, IOException {

		File file = Utils.multipartToFile(mfile, "");

		ImageValidator validator = new ImageValidator();

		if (validator.validate(file.getName())) {

			return true;
		}
		return false;
	}

	public static BufferedImage getImage(MultipartFile resource) throws IOException {
		InputStream is = resource.getInputStream();
		try {
			return ImageIO.read(is);
		}
		finally {
			is.close();
		}
	}

	public static BufferedImage getImage(File resource) throws IOException {
		InputStream is = new FileInputStream(resource);
		try {
			return ImageIO.read(is);
		}
		finally {
			is.close();
		}
	}

	public static File createImage(MultipartFile file, String type) throws IllegalArgumentException, ImagingOpException, IOException {

		String dimentions = getDimentions(type);

		String dimention[] = dimentions.split("x");

		int width = Integer.parseInt(dimention[0]);

		int height = Integer.parseInt(dimention[1]);

		return createCustomImage(file, type, width, height);
	}

	public static File createCustomImage(MultipartFile file, String type, int width, int height) throws IllegalStateException, IOException {

		File outputfile = Utils.multipartToFile(file, type);

		// BufferedImage thumb = Scalr.resize(getImage(file),
		// Method.SPEED,Scalr.Mode.FIT_EXACT,width,height, Scalr.OP_ANTIALIAS);
		// File fileX=convert(file);

		String fileType = FilenameUtils.getExtension(outputfile.getName());

		BufferedImage thumb = Scalr.resize(getImage(file), Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, width, height, Scalr.OP_ANTIALIAS);

		if(fileType.equalsIgnoreCase("gif")){
			fileType="png";
		}
		
		ImageIO.write(thumb, fileType, outputfile);

		return outputfile;
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static String getDimentions(String imageSize) {

		if (imageSize.equalsIgnoreCase(ImageSize.SMALL.toString())) {
			return "250x188";
		}
		else if (imageSize.equalsIgnoreCase(ImageSize.MEDIUM.toString())) {
			return "500x375";
		}
		else if (imageSize.equalsIgnoreCase(ImageSize.LARGE.toString())) {
			return "1024x768";
		}
		else if (imageSize.equalsIgnoreCase(ImageSize.THUMBNAIL.toString())) {
			return "150x100";
		}
		return "100x100";
	}

}
