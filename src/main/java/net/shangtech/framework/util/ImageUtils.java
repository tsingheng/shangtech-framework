package net.shangtech.framework.util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

public class ImageUtils {
	public static void compress(InputStream is, OutputStream os, int width, int height) throws IOException{
		Image image = ImageIO.read(is);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		bi = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		graphics.dispose();
		bi.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		ImageIO.write(bi, "png", os);
	}
	
	public static void compress(File inputFile, File outputFile, int width, int height) throws IOException{
		InputStream is = null;
		OutputStream os = null;
		try{
			is = new FileInputStream(inputFile);
			File outputDir = outputFile.getParentFile();
			if(!outputDir.exists()){
				outputDir.mkdirs();
			}
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}
			os = new FileOutputStream(outputFile);
			compress(is, os, width, height);
		}catch(Exception e){
			throw new IOException(e);
		}finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	public static void compress(File inputFile, String outputPath, int width, int height) throws IOException{
		compress(inputFile, new File(outputPath), width, height);
	}
}
