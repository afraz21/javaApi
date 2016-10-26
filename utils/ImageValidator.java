package org.iqvis.nvolv3.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)";

	/*
	 * Description ( #Start of the group #1 [^\s]+ # must contains one or more
	 * anything (except white space) ( # start of the group #2 \. # follow by a
	 * dot "." (?i) # ignore the case sensive checking for the following
	 * characters ( # start of the group #3 jpg # contains characters "jpg" | #
	 * ..or png # contains characters "png" | # ..or gif # contains characters
	 * "gif" | # ..or bmp # contains characters "bmp" ) # end of the group #3 )
	 * # end of the group #2 $ # end of the string )
	 */

	public ImageValidator() {
		pattern = Pattern.compile(IMAGE_PATTERN);
	}

	/**
	 * Validate image with regular expression
	 * 
	 * @param image
	 *            image for validation
	 * @return true valid image, false invalid image
	 */
	public boolean validate(final String image) {

		matcher = pattern.matcher(image);
		return matcher.matches();

	}
}