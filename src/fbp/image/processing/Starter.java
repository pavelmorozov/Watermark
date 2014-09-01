package fbp.image.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;

public class Starter {
	private final static int IMAGE_SIZE_H = 720;
	private final static int IMAGE_SIZE_W = 480;
	
	public static void main(String[] args) throws Exception {
    	Calendar cal = Calendar.getInstance();
    	//cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:ms");
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
			
			ImageIO.write(destinationImage, "jpg", new File(exportFileName));
			
//			inputStream = new FileInputStream(fileName);
//			outputStream = new FileOutputStream(exportFileName);
//			byte[] buffer = new byte[2048];
//			int length = 0;
//			while ((length = inputStream.read(buffer)) != -1) {
//				//System.out.println("Buffer Read of length: " + length);
//				outputStream.write(buffer, 0, length);
//			}
//			inputStream.close();
//			outputStream.close();
			System.out.println("File exported: " + exportFileName);
		}
		cal = Calendar.getInstance();
		System.out.println(sdf.format(cal.getTime()) +" - Finished" );
	}
}