package org.iqvis.nvolv3.triggers;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.iqvis.nvolv3.utils.Urls;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("cache/")
public class CacheTriggerController {

	@SuppressWarnings("deprecation")
	@RequestMapping(value = "overlay_clean", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String clearOverlayCache(HttpServletRequest request) throws IOException {

		String root = request.getRealPath(Urls.ROOT) + "image\\cache";

		File cache = new File(root);

		File[] list = cache.listFiles();

		if (list != null && list.length > 0) {

			for (File file : list) {

				javaxt.io.File f = new javaxt.io.File(file);

				// BasicFileAttributes
				// attrs=Files.readAttributes(file.toPath(),BasicFileAttributes.class);

				// FileTime fileTime=attrs.lastAccessTime();

				int hours = Hours.hoursBetween(new DateTime(f.getLastAccessTime()), DateTime.now()).getHours();

				if (hours > 24) {

					file.delete();
				}
			}
		}
		else {

			return "Not Item Exist in Cache";
		}

		return "Cache Clean Succefull !";
	}

}
