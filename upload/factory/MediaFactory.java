package org.iqvis.nvolv3.upload.factory;

import java.io.File;

public interface MediaFactory {
	
	String upload(File file, String entityName) throws Exception ;
	
	String getMyClass();

}
