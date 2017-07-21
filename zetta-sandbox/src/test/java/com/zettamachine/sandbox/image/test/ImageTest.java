package com.zettamachine.sandbox.image.test;

import org.apache.log4j.Logger;

import com.zettamachine.sandbox.image.ImageProcessor;

public class ImageTest {
	public static void main(String[] args) {
		Logger log = Logger.getLogger("com.zettamachine.sandbox.image");
		try {
			ImageProcessor ip = new ImageProcessor("/Users/l088166/Workspace/neon/PGPTest/idea.jpg");
			log.debug("Image type is " + ip.getImg().getType());
			log.info(ip.getSize());
			int x = 100, y = 100, w = 300, h = 300;
			String where = "/Users/l088166/Workspace/neon/PGPTest/cropped-" + ip.getImgName();
			log.debug("Cropping image and saving as " + where);
			ip.crop(x, y, w, h, where);
			log.debug("Done.");
			where = "/Users/l088166/Workspace/neon/PGPTest/resized-" + ip.getImgName();
			log.debug("Now resizing image and saving as " + where);
			ip.resize(17, where);
			log.debug("Done.");
			where = "/Users/l088166/Workspace/neon/PGPTest/compressed-" + ip.getImgName();
			log.debug("Now compressing image and saving as " + where);
			ip.compress(0.4f, where);
			log.debug("Done.");
		} catch (Exception e) {
			log.error(e.getClass().getName(), e);}
		}
}
