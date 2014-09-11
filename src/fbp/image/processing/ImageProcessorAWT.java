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
import java.util.SortedSet;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class ImageProcessorAWT extends Task{
	private final static int IMAGE_SIZE_H = 720;
	private final static int IMAGE_SIZE_W = 480;
	private String watermarkText;
	private int fontSize;
	private double opacity;
	private BufferedImage imageToProcess;
	private BufferedImage imageProcessed;
	private String sourceFolder;
	private String outputFolder;
	private String imageFileName;
	private boolean stopFolderProcess;
	private java.awt.Color textColor;
	
	public ImageProcessorAWT() throws Exception {
		stopFolderProcess = false;
	}
	
    /////////////////////////////////////////////////////////////////////////////
    // Initial settings
    /////////////////////////////////////////////////////////////////////////////
	public void setTextColor(javafx.scene.paint.Color textColor){
		this.textColor = new java.awt.Color(
				(int)(textColor.getRed()*255),
				(int)(textColor.getGreen()*255),
				(int)(textColor.getBlue()*255),
				(int)(textColor.getOpacity()*255));
	}
	
	public void setStopFolderProcess(){
		stopFolderProcess = true;
	}
	
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
	
	public Image getImageToProcess(){
		return SwingFXUtils.toFXImage(imageToProcess, null);
	}
	
	public void setImageToProcess(Image imageToProcess){
		this.imageToProcess = SwingFXUtils.fromFXImage(imageToProcess, null);
	}

	public void setImageToProcessFromFile(String fileName){
		File imageFile = new File(fileName);
		try {
			this.imageToProcess = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageFileName = imageFile.getName();
		System.out.println("Set image to process: "+fileName);
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
	public BufferedImage processImage(){
		
		int imageWidth = imageToProcess.getWidth();
		int imageHeight = imageToProcess.getHeight();
		
		int exportImageWidth, exportImageHeight;
		
		if (imageWidth>imageHeight) {
			exportImageWidth = IMAGE_SIZE_H;
			exportImageHeight = IMAGE_SIZE_W;
		}else{
			exportImageWidth = IMAGE_SIZE_W;
			exportImageHeight = IMAGE_SIZE_H;
		}
		
		BufferedImage destinationImage = Scalr.resize(
				imageToProcess, 
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
        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI / -4d);
        Shape rotatedText = rotate45.createTransformedShape(textShape);
        //Shape rotatedText = textShape;

        //g2d.setColor(new Color(200,200,200,(int)(opacity*255)));
        g2d.setColor(textColor);
        //g2d.setStroke(new BasicStroke(1f));
        
        double yStep = Math.sqrt(textWidth * textWidth / 2)*1.2;
        double xStep = textHeight * 3;

        if ((yStep == 0)||(xStep == 0)) { 
        	imageProcessed = destinationImage;
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
            //g2d.translate(xStep*2, -(y + yStep));
            g2d.translate(xStep, -(y + yStep));
        }
		System.out.println("image processed");
		imageProcessed = destinationImage;
		return imageProcessed; 
	}
	
	public void processFolderConcurent() throws IOException{
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		System.out.println("######### Folder conversion #########");
		System.out.println(sdf.format(cal.getTime()) +" - Started" );
		FileProcessor fileProcessor = new FileProcessor();
		fileProcessor.setSourceFolderString(sourceFolder);
		fileProcessor.listFiles();
		final SortedSet<File> imageFileSet = fileProcessor.getFileSet();
		System.out.println(imageFileSet.size());
		int progress = 1;
		for (File imageFile:imageFileSet){
			if (!stopFolderProcess){
				setImageToProcessFromFile(imageFile.getAbsolutePath());
				System.out.println("processed: "+progress);
				updateMessage((int)(100*progress/imageFileSet.size())+"% " + progress 
						+ " of "+imageFileSet.size());
				processImage();
				saveImageProcessed();
				progress++;
			}
		}
		cal = Calendar.getInstance();
		System.out.println(sdf.format(cal.getTime()) +" - Finished" );		
	}

    /////////////////////////////////////////////////////////////////////////////
    // Get processed images
    /////////////////////////////////////////////////////////////////////////////
	public Image getImageProcessed(){
		return SwingFXUtils.toFXImage(imageProcessed, null);
	}	
	
	public void saveImageProcessed() throws IOException{
		ImageIO.write(imageProcessed, "jpg", 
				new File(outputFolder+"\\\\"+imageFileName));
	}
	
	@Override
	protected Object call() throws Exception {
		processFolderConcurent();
		return null;
	}
}

