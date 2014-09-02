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
import java.util.HashSet;
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

    	Set<BufferedImage> sourceImageSet = new HashSet<BufferedImage>();    	
    	
    	FileProcessor fileProcessor = new FileProcessor();  
    	Set<File> imageSet = fileProcessor.listFiles("");
		
		for (File imageFile:imageSet){
			//String exportFileName = "export_"+imageFile.getName();
			
			BufferedImage sourceImage = ImageIO.read(imageFile);
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
	        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / -5d);
	        Shape rotatedText = rotate45.createTransformedShape(textShape);
	        //Shape rotatedText = textShape;

	        g2d.setColor(new Color(120,120,120,70));
	        //g2d.setStroke(new BasicStroke(1f));
	        
	        double yStep = Math.sqrt(textWidth * textWidth / 2)/2;
	        double xStep = textHeight * 3;
	        
	        // step over image rendering watermark text
	        for (double x = -xStep ; x < destinationImage.getWidth(); x += xStep) {
	            double y = -yStep;
	            for (; y < destinationImage.getHeight(); y += yStep) {
	                //g2d.draw(rotatedText);
	                g2d.fill(rotatedText);
	                g2d.translate(0, yStep);
	            }
	            g2d.translate(xStep*5, -(y + yStep));
	        }
			
			ImageIO.write(destinationImage, "jpg", new File(fileProcessor.getOutputFolderName()+'/'+imageFile.getName()));
			System.out.println("File exported: " + imageFile.getName());
		}
		cal = Calendar.getInstance();
		System.out.println(sdf.format(cal.getTime()) +" - Finished" );
	}
}