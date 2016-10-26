package org.iqvis.nvolv3.upload.factory;

import java.io.File;

import org.iqvis.nvolv3.service.ConstantFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.bitcasa.cloudfs.client.Exists;
import com.bitcasa.cloudfs.client.Folder;
import com.bitcasa.cloudfs.client.Session;

@PropertySource("classpath:media-upload-keys.properties")
public class MediaUploadBitcasaFactory implements MediaFactory {
	
	@Autowired Environment evnvironment;

	public String upload(File file, String entityName) throws Exception {
		String endPoint = evnvironment.getProperty("endpoint");

		String clientId = evnvironment.getProperty("client_id");

		String clientSecret = evnvironment.getProperty("client_secret");

		String username = evnvironment.getProperty("username");

		String password = evnvironment.getProperty("password");

		String url = ConstantFactoryImpl.DOWNLOAD_FILE_URL;

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

				url += mediaId+"/"+file.getName();

				// java.io.File uploadingFile =
				// temporaryFolder.newFile("Test.txt");

			}
			catch (Exception e) {

				e.printStackTrace();

			}

		}

		return url;
	}

	public String getMyClass() {

		return "BITCASA";
	}

}
