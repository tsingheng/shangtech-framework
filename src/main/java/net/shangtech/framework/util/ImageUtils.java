package net.shangtech.framework.util;
import java.awt.AlphaComposite;
import java.awt.Color;
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
	public static void compress(InputStream is, OutputStream os, int width, int height, Image imageIcon) throws IOException{
		Image image = ImageIO.read(is);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
//		bi = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		bi = bi.createGraphics().getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		
		Graphics2D graphics = bi.createGraphics();
		graphics.setBackground(Color.WHITE);
		
//		bi.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		
		//水印
		if(imageIcon != null){
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5F));
			int iconWidth = imageIcon.getWidth(null);
			int iconHeight = imageIcon.getHeight(null);
			int iconX = 0;
			int iconY = 0;
			if(iconWidth/iconHeight > width/height){
				iconHeight = width*iconHeight/iconWidth;
				iconWidth = width;
				iconY = (height-iconHeight)/2;
			}else{
				iconWidth = height*iconWidth/iconHeight;
				iconHeight = height;
				iconX = (width - iconWidth)/2;
			}
//			bi.getGraphics().drawImage(imageIcon.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH), iconX, iconY, null);
			graphics.drawImage(imageIcon.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH), iconX, iconY, null);
			graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		}
		
		graphics.dispose();
		
		ImageIO.write(bi, "png", os);
	}
	
	public static void compress(File inputFile, File outputFile, int width, int height, Image imageIcon) throws IOException{
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
			compress(is, os, width, height, imageIcon);
		}catch(Exception e){
			throw new IOException(e);
		}finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	public static void compress(File inputFile, String outputPath, int width, int height) throws IOException{
		compress(inputFile, new File(outputPath), width, height, null);
	}
	
	public static void compress(File inputFile, String outputPath, int width, int height, Image imageIcon) throws IOException{
		compress(inputFile, new File(outputPath), width, height, imageIcon);
	}
}
