package fbp.image.processing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageProcessorAWT {
	private final static int IMAGE_SIZE_H = 720;
	private final static int IMAGE_SIZE_W = 480;
	private String watermarkText;
	private int fontSize;
	private double opacity;
	private Image imageToProcess;
	private Image imageProcessed;
	private String sourceFolder;
	private String outputFolder;
	private String imageFileName;
	
	public ImageProcessorAWT()  throws Exception {
	}
	
    /////////////////////////////////////////////////////////////////////////////
    // Initial settings
    /////////////////////////////////////////////////////////////////////////////
	public void setWatermarkText(String watermarkText){
		this.watermarkText = watermarkText;
	}
	
	public String getWatermarkText(){
		return watermarkText;
	}
	
	public void setFontSize(int fontSize){
		if (( fontSize > 15 ) && (fontSize < 100)){ 
			this.fontSize = fontSize;
		}else{
			this.fontSize = 16;
		}
	}

	public int getFontSize(){
		return fontSize;
	}
	
	public void setOpacity(double opacity){
		if (( opacity >= 0 ) && (opacity <= 1)){ 
			this.opacity = opacity;
		}else{
			this.opacity = 1;
		}
	}
	
	public double getOpacity(){
		return opacity;
	}
	
	public void setImageToProcess(Image imageToProcess){
		this.imageToProcess = imageToProcess;
	}

	public void setImageToProcessFromFile(String fileName){
		Image imageToProcess = new Image("file:"+fileName);
		this.imageToProcess = imageToProcess; 
		imageFileName = new File(fileName).getName();
		System.out.println("Image to process: "+fileName);
	}

	public void setSourceFolder(String sourceFolder){
		this.sourceFolder = sourceFolder;
	}

	public void setOutputFolder(String outputFolder){
		this.outputFolder = outputFolder;
	}

    /////////////////////////////////////////////////////////////////////////////
    // Processor commands
    /////////////////////////////////////////////////////////////////////////////
	public Image processImage(){
		BufferedImage sourceImage = null;
		sourceImage = SwingFXUtils.fromFXImage(imageToProcess, null);
		
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
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);

        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), watermarkText);
        Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
        Shape textShape = fontGV.getOutline();
        double textWidth = size.getWidth();
        double textHeight = size.getHeight();
        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / -5d);
        Shape rotatedText = rotate45.createTransformedShape(textShape);
        //Shape rotatedText = textShape;

        g2d.setColor(new Color(120,120,120,(int)(opacity*255)));
        //g2d.setStroke(new BasicStroke(1f));
        
        double yStep = Math.sqrt(textWidth * textWidth / 2)/2;
        double xStep = textHeight * 3;

        if ((yStep == 0)||(xStep == 0)) { 
        	imageProcessed = SwingFXUtils.toFXImage(destinationImage, null);
        	return imageProcessed;
        }
        
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
		System.out.println("image processed");
		imageProcessed = SwingFXUtils.toFXImage(destinationImage, null);
		return imageProcessed; 
	}

	public void processFolder() throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		System.out.println("######### Folder conversion #########");
		System.out.println(sdf.format(cal.getTime()) +" - Started" );
		
		FileProcessor fileProcessor = new FileProcessor();
		fileProcessor.setSourceFolderString(sourceFolder);
		fileProcessor.listFiles();
		SortedSet<File> imageFileSet = fileProcessor.getFileSet();
		
		for (File imageFile:imageFileSet){
			setImageToProcessFromFile(imageFile.getAbsolutePath());
			processImage();
			saveImageProcessed();
		}
	}	
    /////////////////////////////////////////////////////////////////////////////
    // Get processed images
    /////////////////////////////////////////////////////////////////////////////
	public Image getImageProcessed(){
		return imageProcessed;
	}	
	
	public void saveImageProcessed() throws IOException{
		ImageIO.write(SwingFXUtils.fromFXImage(imageProcessed, null), "jpg", 
				new File(outputFolder+"\\\\"+imageFileName));
	}
	
	public Image processImageFromFile(
			File fileToRead,
			String watermarkText,
			int fontSize,
			double opacity) throws Exception {
		//Calendar cal = Calendar.getInstance();
		//cal.getTime();
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		//System.out.println("######### File conversion #########");
		//System.out.println(sdf.format(cal.getTime()) +" - Started" );

//		Set<BufferedImage> sourceImageSet = new HashSet<BufferedImage>();    	
//		
//		FileProcessor fileProcessor = new FileProcessor();  
//		Set<File> imageSet = fileProcessor.listFiles("");
//		
//		for (File imageFile:imageSet){
			//String exportFileName = "export_"+imageFile.getName();
			
			BufferedImage sourceImage = ImageIO.read(fileToRead);
			
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
	        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
	        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), watermarkText);
	        Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
	        Shape textShape = fontGV.getOutline();
	        double textWidth = size.getWidth();
	        double textHeight = size.getHeight();
	        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / -5d);
	        Shape rotatedText = rotate45.createTransformedShape(textShape);
	        //Shape rotatedText = textShape;

	        g2d.setColor(new Color(120,120,120,(int)(opacity*255)));
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
			
			//ImageIO.write(destinationImage, "jpg", new File(fileToRead.getPath()+".xxx"));
			System.out.println("File processed: " + fileToRead.getPath());
			Image image = SwingFXUtils.toFXImage(destinationImage, null);
			return image;
//		}
//		cal = Calendar.getInstance();
//		System.out.println(sdf.format(cal.getTime()) +" - Finished" );
		
	}
}
