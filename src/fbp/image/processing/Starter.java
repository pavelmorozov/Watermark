package fbp.image.processing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import javax.imageio.ImageIO;
//import javax.swing.text.Position;










import org.imgscalr.Scalr;

public class Starter {
	private final static int IMAGE_SIZE_H = 720;
	private final static int IMAGE_SIZE_W = 480;
	private final static String WATERMARK_TEXT = "=== < Watermark > ===";
	
	public static void main(String[] args) throws Exception {
    	Calendar cal = Calendar.getInstance();
    	//cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
    	System.out.println("######### File conversaion #########");
    	System.out.println(sdf.format(cal.getTime()) +" - Started" );
    	
    	FileProcessor fileProcessor = new FileProcessor();  
    	Set<String> imageSet = fileProcessor.listFiles("");  
    	
		InputStream inputStream;
		OutputStream outputStream;
		
		String watermark = "- < Test watermark > -";
		
		for (String fileName:imageSet){
			String exportFileName = "export_"+fileName;
			
			BufferedImage sourceImage = ImageIO.read(new File(fileName));
			int imageWidth = sourceImage.getWidth();
			int imageHeight = sourceImage.getHeight();
			
			int exportImageWidth, exportImageHeight;
			
			if (imageWidth>imageHeight) {
				exportImageWidth = IMAGE_SIZE_H;
				exportImageHeight = IMAGE_SIZE_W;
			}else{
				exportImageWidth = IMAGE_SIZE_W;
				exportImageHeight = IMAGE_SIZE_H;
			}
			
			BufferedImage destinationImage = Scalr.resize(
					sourceImage, 
					Scalr.Method.SPEED,
					Scalr.Mode.FIT_TO_WIDTH,
					exportImageWidth, exportImageHeight, 
					Scalr.OP_ANTIALIAS);
			
			Graphics2D g2d = destinationImage.createGraphics();
			
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                     RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		    // create watermark text shape for rendering
	        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), WATERMARK_TEXT);
	        Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
	        Shape textShape = fontGV.getOutline();
	        double textWidth = size.getWidth();
	        double textHeight = size.getHeight();
	        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / 5d);
	        Shape rotatedText = rotate45.createTransformedShape(textShape);

	        // use a gradient that repeats 4 times
	        g2d.setPaint((Paint) new GradientPaint(0, 0,
	                            new Color(0.5f, 0.5f, 0.5f, 0.5f),
	                            destinationImage.getWidth() / 2, destinationImage.getHeight() / 2,
	                            new Color(0.5f, 0.5f, 0.5f, 0.5f)));
	        g2d.setStroke(new BasicStroke(0.5f));

	        //double yStep = Math.sqrt(textWidth * textWidth / 2) + 20;
	        
//	        // step in y direction is calc'ed using pythagoras + 5 pixel padding
//	        double yStep = Math.sqrt(textWidth * textWidth / 2) + 20;
//
//	        // step over image rendering watermark text
//	        for (double x = -textHeight * 3; x < destinationImage.getWidth(); x += (textHeight * 3)) {
//	            double y = -yStep;
//	            for (y = -yStep * 2 ; y < destinationImage.getHeight(); y += yStep) {
//	                g2d.draw(rotatedText);
//	                g2d.fill(rotatedText);
//	                g2d.translate(0, yStep);
//	            }
//	            g2d.translate(textHeight * 3, -(y + yStep));
//	        }			
			
	        ImageIO.write(destinationImage, "jpg", new File(exportFileName));
			
			
			
//	        Graphics2D g2d = (Graphics2D) destinationImage.getGraphics();
//	        
//	        // initializes necessary graphic properties
//	        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
//	        g2d.setComposite(alphaChannel);
//	        g2d.setColor(Color.GRAY);
//	        g2d.setFont(new Font("Arial", Font.BOLD, 64));
//	        
//	        
//	        
//	        FontMetrics fontMetrics = g2d.getFontMetrics();
//	        Rectangle2D rect = fontMetrics.getStringBounds(WATERMARK_TEXT, g2d);
//	 
//	        // calculates the coordinate where the String is painted
//	        int centerX = (destinationImage.getWidth() - (int) rect.getWidth()) / 2;
//	        int centerY = destinationImage.getHeight() / 2;
//	 
//	        // paints the textual watermark
//	        g2d.drawString(WATERMARK_TEXT, centerX, centerY);
//	 
//	        //ImageIO.write(sourceImage, "jpg", destImageFile);
//	        ImageIO.write(destinationImage, "jpg", new File(exportFileName));
//	        g2d.dispose();
//	 
//	        //System.out.println("The tex watermark is added to the image.");			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			 Graphics2D graphics2D = (Graphics2D)destinationImage.getGraphics();
//			 graphics2D.setFont(new Font("Arial", Font.BOLD, 30));
//			 graphics2D.drawString("Watermarked!", destinationImage.getWidth()/2, destinationImage.getHeight() / 2);
			 

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
//			// Set up the caption properties
//			String caption = WATERMARK_TEXT;
//			Font font = new Font("Monospaced", Font.PLAIN, 14);
//			Color c = Color.black;
//			Position position = Position.CENTER;
//			int insetPixels = 0;
//
//			// Apply caption to the image
//			Caption filter = new Caption(caption, font, c, position, insetPixels);
//			BufferedImage captionedImage = filter.apply(originalImage);			
			
			
			
			
			
			
			
			
			
			
			
		
//			Graphics2D g2d = destinationImage.createGraphics();
//			Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
//	        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), WATERMARK_TEXT);
//	        Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
//	        Shape textShape = fontGV.getOutline();
//	        double textWidth = size.getWidth();
//	        double textHeight = size.getHeight();
//	        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / 4d);
//	        Shape rotatedText = rotate45.createTransformedShape(textShape);
//	        
//	        // use a gradient that repeats 4 times
//	        g2d.setPaint(new GradientPaint(0, 0,
//	                            new Color(0f, 0f, 0f, 0.1f),
//	                            destinationImage.getWidth() / 2, destinationImage.getHeight() / 2,
//	                            new Color(1f, 1f, 1f, 0.1f)));
//	        g2d.setStroke(new BasicStroke(0.5f));
//
//	        // step in y direction is calc'ed using pythagoras + 5 pixel padding
//	        double yStep = Math.sqrt(textWidth * textWidth / 2) + 5;
//
//	        // step over image rendering watermark text
//	        for (double x = -textHeight * 3; x < destinationImage.getWidth(); x += (textHeight * 3)) {
//	            double y = -yStep;
//	            for (; y < destinationImage.getHeight(); y += yStep) {
//	                g2d.draw(rotatedText);
//	                g2d.fill(rotatedText);
//	                g2d.translate(0, yStep);
//	            }
//	            g2d.translate(textHeight * 3, -(y + yStep));
//	        }	        
//	        
//	        g2d
	        
	        
	        
	        
	        
			
			
			//ImageIO.write(destinationImage, "jpg", new File(exportFileName));
			System.out.println("File exported: " + exportFileName);
		}
		cal = Calendar.getInstance();
		System.out.println(sdf.format(cal.getTime()) +" - Finished" );
	}
}