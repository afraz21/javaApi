package org.iqvis.nvolv3.utils;

public enum ImageSize {

	SMALL, MEDIUM, LARGE, THUMBNAIL, CUSTOM;

	public String toString() {
		switch (this) {
		case SMALL:
			return "small";
		case MEDIUM:
			return "medium";
		case LARGE:
			return "large";
		case THUMBNAIL:
			return "thumb";
		case CUSTOM:
			return "custom";
		}
		return null;
	}

	public static ImageSize valueOf(Class<ImageSize> enumType, String value) {
		if (value.equalsIgnoreCase(SMALL.toString()))
			return ImageSize.SMALL;
		else if (value.equalsIgnoreCase(MEDIUM.toString()))
			return ImageSize.MEDIUM;
		else if (value.equalsIgnoreCase(LARGE.toString()))
			return ImageSize.LARGE;
		else if (value.equalsIgnoreCase(THUMBNAIL.toString()))
			return ImageSize.THUMBNAIL;
		else if (value.equalsIgnoreCase(CUSTOM.toString()))
			return ImageSize.CUSTOM;
		else
			return null;
	}
}
