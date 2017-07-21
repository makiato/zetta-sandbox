package com.zettamachine.sandbox.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.log4j.Logger;

public class ImageProcessor {
	Logger log = Logger.getLogger("com.zettamachine.sandbox.image");
	private String imgPath = "";
	private String imgName = "";

	private BufferedImage img = null;
	
	public ImageProcessor() {
	}
	
	public ImageProcessor(String imgPath) throws IOException {
		this.imgPath = imgPath;
		File f = new File(imgPath);
		this.imgName = f.getName();
		img = ImageIO.read(f);
	}


	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	public String getSize() {
		return img.getWidth() + "x" + img.getHeight();
	}
	
	public void crop(int x, int y, int w, int h, String where) throws IOException {
		ImageIO.write(img.getSubimage(x, y, w, h), "jpg", new File(where));
	}
	
	public void resize(int percentage, String where) throws IOException {
		double w = (img.getWidth() * percentage) / 100;
		double h = (img.getHeight() * percentage) / 100;
		double s = new Double(percentage + ".0") / 100;
		log.debug("w -> " + w + ", h - > " + h + ", s -> " + s);
		BufferedImage resizedImg  = new BufferedImage(
			new Double(w).intValue(), 
			new Double(h).intValue(), 
			BufferedImage.TYPE_USHORT_565_RGB
		);
		Graphics2D g = resizedImg.createGraphics();
		AffineTransform at = AffineTransform.getScaleInstance(s, s);
		g.drawRenderedImage(img, at);
		ImageIO.write(resizedImg, "jpg", new File(where));
	}
	
	public void compress(float quality, String where) throws FileNotFoundException, IOException {
		Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");  
		ImageWriter writer = (ImageWriter)iter.next();  
		ImageWriteParam iwp = writer.getDefaultWriteParam();  
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);    
		iwp.setCompressionQuality(quality);  
		
		FileImageOutputStream output = new FileImageOutputStream(new File(where));  
        writer.setOutput(output);  
        
        IIOImage image = new IIOImage(img, null, null);
        writer.write(null, image, iwp);
		
		writer.dispose();
	}
	
}
