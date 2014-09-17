package fbp.image.processing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.SortedSet;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
	private String fontString;
	private int rotation;
	private String promoMode;
	private double mouseX;
	private double mouseY;
	private GraphicsContext graphicsContext;
	private double resolutionX;
	private double resolutionY;
	private String processPromoToSave;
	
	public ImageProcessorAWT() throws Exception {
		stopFolderProcess = false;
		processPromoToSave = "";
	}
	
    /////////////////////////////////////////////////////////////////////////////
    // Initial settings
    /////////////////////////////////////////////////////////////////////////////
	
	public void setResolutionX(String resolutionX){
		this.resolutionX = Double.parseDouble(resolutionX); 
	}
	public void setResolutionY(String resolutionY){
		this.resolutionY = Double.parseDouble(resolutionY); 
	}
	
	public void setX(double mouseX){
		this.mouseX = mouseX;
	}
	public void setY(double mouseY){
		this.mouseY = mouseY;
	}
	
	public void setGraphicsContext(GraphicsContext graphicsContext){
		this.graphicsContext = graphicsContext;
	}
	
	public void setPromoMode(String promoMode){
		this.promoMode = promoMode;
	}	
	
	public void setRotation(int rotation){
		this.rotation = rotation;
	}	
	
	public void setFontString(String fontString){
		this.fontString = fontString;
	}
	
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
		if (( fontSize > 15 ) && (fontSize < 201)){ 
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
		double scale = 1;
		
		if (processPromoToSave.equals("Use resolution")){
			/**
			 * Passed scale resolutions
			 */
			//if (imageWidth>imageHeight) {
				exportImageWidth = (int)resolutionX;
				exportImageHeight = (int)resolutionY;
//			}else{
//				exportImageWidth = (int)resolutionY;
//				exportImageHeight = (int)resolutionX;
//			}
			//Resize Image
				
			imageProcessed = Scalr.resize(
					imageToProcess, 
					Scalr.Method.QUALITY,
					Scalr.Mode.FIT_TO_WIDTH,
					exportImageWidth, exportImageHeight, 
					Scalr.OP_ANTIALIAS);			
		}else{
			/**
			 * Default scale resolutions
			 */
			if (imageWidth>imageHeight) {
				exportImageWidth = IMAGE_SIZE_H;
				exportImageHeight = IMAGE_SIZE_W;
			}else{
				exportImageWidth = IMAGE_SIZE_W;
				exportImageHeight = IMAGE_SIZE_H;
			}
			//Resize Image
			imageProcessed = Scalr.resize(
					imageToProcess, 
					Scalr.Method.SPEED,
					Scalr.Mode.FIT_TO_WIDTH,
					exportImageWidth, exportImageHeight, 
					Scalr.OP_ANTIALIAS);			
		}
		
		//Create watermark
		Graphics2D g2d = imageProcessed.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Font font;
		if (processPromoToSave.equals("Use resolution")){
			scale = (resolutionX+resolutionY)/
					(IMAGE_SIZE_H+IMAGE_SIZE_W);
			font = new Font(fontString, Font.PLAIN, 
					(int)(fontSize*scale));
		}else{
			font = new Font(fontString, Font.PLAIN, fontSize);
		}
		
        GlyphVector fontGV = font.createGlyphVector(g2d.getFontRenderContext(), watermarkText);
        Rectangle size = fontGV.getPixelBounds(g2d.getFontRenderContext(), 0, 0);
        Shape textShape = fontGV.getOutline();
        double textWidth = size.getWidth();
        double textHeight = size.getHeight();
        AffineTransform rotate45 = AffineTransform.getRotateInstance(Math.PI * rotation/ 180);
        Shape rotatedText = rotate45.createTransformedShape(textShape);
        g2d.setColor(textColor);
        
        if (promoMode.equals("Center")){
        	double x = (imageProcessed.getWidth()-
        			Math.sqrt(Math.pow(textHeight,2)+Math.pow(textWidth,2))*
        			Math.cos(-Math.PI * rotation/180 + Math.atan(textHeight/textWidth)))/2;
        	double y = (imageProcessed.getHeight()+
        			Math.sqrt(Math.pow(textHeight,2)+Math.pow(textWidth,2))*
        			Math.sin(-Math.PI * rotation/180 + Math.atan(textHeight/textWidth)))/2;
        	g2d.translate(x, y);
        	g2d.fill(rotatedText);
        }else if (promoMode.equals("Corner: left bottom")){
        	double x = imageProcessed.getWidth()/12;
        	double y = imageProcessed.getHeight()*11/12 - textHeight; 
        	g2d.translate(x, y);
        	g2d.fill(rotatedText);
        }else if (promoMode.equals("Custom")){
        	//Account imagePromoX, imagePromoY and apply promo
    		double canvasX=graphicsContext.getCanvas().getWidth();
    		double canvasY=graphicsContext.getCanvas().getHeight();
    		
    		double x = 0;
    		double y = 0;
    		x = (canvasX - getImageProcessed().getWidth()/scale) / 2;
    		y = (canvasY - getImageProcessed().getHeight()/scale) / 2;
    		
        	double imagePromoX =  mouseX - x;
        	double imagePromoY =  mouseY - y;
        	
        	if (processPromoToSave.equals("Use resolution")){
        		g2d.translate(imagePromoX*scale, imagePromoY*scale);
        	}else{
        		g2d.translate(imagePromoX, imagePromoY);
        	}
        	g2d.fill(rotatedText);
        }else if (promoMode.equals("None (Tile)")){
            double yStep = textHeight + textWidth*Math.abs(Math.sin(Math.PI * rotation/ 180));
            double xStep = textHeight + textWidth*Math.abs(Math.cos(Math.PI * rotation/ 180));
            if ((yStep <= 0)||(xStep <= 0)) { 
            	//imageProcessed = destinationImage;
            	return imageProcessed;
            }
            
            // step over image rendering watermark text
            for (double x = -xStep ; x < imageProcessed.getWidth(); x += xStep) {
                double y = -yStep;
                for (; y < imageProcessed.getHeight(); y += yStep) {
                    //g2d.draw(rotatedText);
                    g2d.fill(rotatedText);
                    g2d.translate(0, yStep);
                }
                //g2d.translate(xStep*2, -(y + yStep));
                g2d.translate(xStep, -(y + yStep));
            }
        }
        
        System.out.println("image processed");
		return imageProcessed; 
	}
	
	/**
	 * This functions put image processed on mainGraphicsContext.
	 */
	public void fillMainGraphicsContext(){
		/**
		 * If graphicsContext bigger than IMAGE_SIZE_H, IMAGE_SIZE_Y
		 * fill blank area, and put image to center
		 */
		
		double canvasX=graphicsContext.getCanvas().getWidth();
		double canvasY=graphicsContext.getCanvas().getHeight();
		Image imageProcessedFX = getImageProcessed();
		
		graphicsContext.setFill(Color.web("#555555", 1));
		graphicsContext.fillRect(0, 0, canvasX, canvasY);
		//processImage();
		double x = 0;
		double y = 0;
		x = (canvasX - imageProcessedFX.getWidth()) / 2;
		y = (canvasY - imageProcessedFX.getHeight()) / 2;
		
		graphicsContext.drawImage(imageProcessedFX, x, y,
				imageProcessedFX.getWidth(), imageProcessedFX.getHeight());		
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
		BufferedImage imageRGB = new BufferedImage(imageProcessed.getWidth(), imageProcessed.getHeight(), BufferedImage.OPAQUE); // Remove alpha-channel from buffered image.
		Graphics2D graphics = imageRGB.createGraphics();
		graphics.drawImage(imageProcessed, 0, 0, null);
		ImageIO.write(imageRGB, "jpg", 
				new File(outputFolder+"\\\\"+imageFileName));
		graphics.dispose();
		ImageIO.write(imageProcessed, "jpg", 
				new File(outputFolder+"\\\\"+imageFileName));
	}
	
	/**
	 * @throws IOException 
	 */
	public void saveScaledPromoImage() throws IOException{
		processPromoToSave = "Use resolution";
		processImage();
		
		BufferedImage imageRGB = new BufferedImage(imageProcessed.getWidth(), imageProcessed.getHeight(), BufferedImage.OPAQUE); // Remove alpha-channel from buffered image.
		Graphics2D graphics = imageRGB.createGraphics();
		graphics.drawImage(imageProcessed, 0, 0, null);
		ImageIO.write(imageRGB, "jpg", 
				new File(outputFolder+"\\\\"+imageFileName));
		processPromoToSave = "Defaults";
		graphics.dispose();
	}
	
	@Override
	protected Object call() throws Exception {
		processFolderConcurent();
		return null;
	}
}

