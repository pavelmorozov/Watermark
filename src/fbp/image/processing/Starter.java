package fbp.image.processing;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Starter extends Application{

	private static final String APP_CAPTION = "Resize & Watermark";
	private static final String WORK_CATALOG = System.getProperty("user.dir");
	private static final String VERTICAL_BLANK_NAME = System.getProperty("user.dir")
			+"\\img\\vertical.jpg";
	private static final String HORIZONTAL_BLANK_NAME = System.getProperty("user.dir")
			+"\\img\\horizontal.jpg";
	
	private final static int IMAGE_SIZE_H = 720;
	private final static int IMAGE_SIZE_W = 480;	

	private static final Integer SCALED_PREVIW_X = 192;
	private static final Integer SCALED_PREVIW_Y = 288;

	private ImageProcessorAWT imageProcessor;
	
	private Stage primaryStage;
	
	public static void main(String[] args) throws Exception {
		//new ImageProcessorAWT();
		launch(args);
	}

    @Override
    public void start(final Stage stage) throws NumberFormatException, Exception {
    	
		imageProcessor = new ImageProcessorAWT();
        /////////////////////////////////////////////////////////////////////////////
        // Visual UI controls creation
        /////////////////////////////////////////////////////////////////////////////		
    	
    	primaryStage = stage;
        primaryStage.setTitle(APP_CAPTION);
//        Button openSourceCatalogBtn = new Button();
//        openSourceCatalogBtn.setText("Open source catalog");
//        openSourceCatalogBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//            	chooseFile("Source");
////                FileChooser fileChooser = new FileChooser();
////                fileChooser.setTitle("Open Resource File");
////                fileChooser.showOpenDialog(primaryStage);                
//            }
//        });
        GridPane rootGrid = new GridPane();
        rootGrid.setId("rootGrid");
        rootGrid.setGridLinesVisible(true);
        rootGrid.setHgap(10);
        rootGrid.setVgap(10);
        rootGrid.setPadding(new Insets(5, 5, 5, 5));
        
        Text titleText = new Text(APP_CAPTION);
        titleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        rootGrid.add(titleText, 0, 0, 3, 1);

        Label sourceLabel = new Label("Source folder:");
        rootGrid.add(sourceLabel, 0, 1);

        TextField sourceField = new TextField();
        sourceField.setId("sourceField");
        rootGrid.add(sourceField, 1, 1, 2, 1);
        
        Button chooseSourceBtn = new Button();
        chooseSourceBtn.setText("...");
        rootGrid.add(chooseSourceBtn, 3, 1);

        Label destinationLabel = new Label("Destination folder:");
        rootGrid.add(destinationLabel, 0, 2);

        TextField outputField = new TextField ();
        outputField.setId("outputField");
        rootGrid.add(outputField, 1, 2, 2, 1);
        
        Button chooseDestinationBtn = new Button();
        chooseDestinationBtn.setText("...");
        rootGrid.add(chooseDestinationBtn, 3, 2);

        Label watermarkTextLabel = new Label("Watermark text:");
        rootGrid.add(watermarkTextLabel, 0, 3);

        TextField watermarkTextField = new TextField ();
        rootGrid.add(watermarkTextField, 1, 3, 2, 1);
        
        Label watermarkFontSizeLabel = new Label("Font size:");
        rootGrid.add(watermarkFontSizeLabel, 0, 4);

        TextField watermarkFontSizeField = new TextField ();
        watermarkFontSizeField.setMaxWidth(30);        
        rootGrid.add(watermarkFontSizeField, 1, 4, 1, 1);
        
        Label watermarkOpacityLabel = new Label("Opacity:");
        rootGrid.add(watermarkOpacityLabel, 0, 5);

        TextField watermarkOpacityField = new TextField ();
        watermarkOpacityField.setMaxWidth(30);
        rootGrid.add(watermarkOpacityField, 1, 5, 1, 1);
        
        Label previewFileLabel = new Label("Preview:");
        rootGrid.add(previewFileLabel, 0, 6);

        TextField previewFileField = new TextField();
        rootGrid.add(previewFileField, 1, 6, 2, 1);
        
        Button choosePreviewFileBtn = new Button();
        choosePreviewFileBtn.setText("...");
        rootGrid.add(choosePreviewFileBtn, 3, 6);        
        
        Button choosePreviewPrevBtn = new Button();
        choosePreviewPrevBtn.setText("- < -");
        rootGrid.add(choosePreviewPrevBtn, 1, 7);        
        
        Button choosePreviewNextBtn = new Button();
        choosePreviewNextBtn.setText("- > -");
        rootGrid.add(choosePreviewNextBtn, 2, 7);        
        
//        Image verticalBlankImage = new Image("file:img/vertical.jpg");
        Image testImage = new Image("file:output/1.original.jpg");
        
//        ImageView verticalImageView = new ImageView(); 
//        verticalImageView.setImage(verticalBlankImage);
//        
//        rootGrid.add(verticalImageView, 0, 6);        
        
        Canvas verticalCanvas = new Canvas (SCALED_PREVIW_X, SCALED_PREVIW_Y);
        GraphicsContext verticalGraphicsContext = verticalCanvas.getGraphicsContext2D();
        verticalGraphicsContext.setFill(Color.web("#ffffff",1));
        //verticalGraphicsContext.fillOval(10, 60, 30, 30);
        verticalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_X, SCALED_PREVIW_Y);
        rootGrid.add(verticalCanvas, 0, 8, 3, 1);

        Canvas horizontalCanvas = new Canvas (SCALED_PREVIW_Y, SCALED_PREVIW_X);
        GraphicsContext horizontalGraphicsContext = horizontalCanvas.getGraphicsContext2D();
        horizontalGraphicsContext.setFill(Color.web("#ffffff",1));
        horizontalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_Y, SCALED_PREVIW_X);
        rootGrid.add(horizontalCanvas, 0, 9, 5, 1);
        
        Canvas mainCanvas = new Canvas (IMAGE_SIZE_H, IMAGE_SIZE_H);
        GraphicsContext mainGraphicsContext = mainCanvas.getGraphicsContext2D();
        mainGraphicsContext.setFill(Color.web("#777777",1));
        mainGraphicsContext.fillRect(0, 0, IMAGE_SIZE_H, IMAGE_SIZE_H);
        
        /////////////////////////////////////////////////////////////////////////////
        // Initial fields settings
        /////////////////////////////////////////////////////////////////////////////
        
        sourceField.setText("D:\\java\\projects\\Watermark");
        outputField.setText("d:\\java\\projects\\watermark\\output");
        watermarkTextField.setText("=== <Watermark text> ===");
        watermarkFontSizeField.setText("16");
        watermarkOpacityField.setText("0.4");
        previewFileField.setText("1.original.jpg");
        
        /////////////////////////////////////////////////////////////////////////////
        // Open image from initial settings
        /////////////////////////////////////////////////////////////////////////////		
        
        double x = 0;
        double y = 0;        
        
//        String imagePath = "file:" + sourceField.getText()+"\\"+
//        		previewFileField.getText();
//        Image imageToDraw = new Image(imagePath);
        Image imageToDraw = imageProcessor.processImage(
        		new File(sourceField.getText()+"\\\\"+previewFileField.getText()),
        		watermarkTextField.getText(),
        		Integer.parseInt(watermarkFontSizeField.getText()),
    			Double.parseDouble(watermarkOpacityField.getText().replace(',', '.')));
        
        //System.out.println("height: "+imageToDraw.getHeight());
        //System.out.println("imagePath: "+imagePath);
        
        if (imageToDraw.getWidth()<imageToDraw.getHeight()){
        	//vertical align
        	x = (mainCanvas.getWidth() - imageToDraw.getWidth())/2;
        	y = 0;
        } else {
        	//horizontal align
        	x = 0;
        	y = (mainCanvas.getHeight() - imageToDraw.getHeight())/2;
        }
        
        mainGraphicsContext.drawImage
        	(imageToDraw, x, y, imageToDraw.getWidth(), imageToDraw.getHeight());        
        
        rootGrid.add(mainCanvas, 4, 0, 1, 10);        
        
//        root.getChildren().add(btn);
        Scene primaryScene = new Scene(rootGrid, 1100, 725);
        
        primaryScene.getStylesheets().add(Starter.class.getResource("ui.css").toExternalForm());
        //rootGrid.getChildren().add(openSourceCatalogBtn);
        
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
    
	private void chooseFile(String fileChooseMode){
		if (fileChooseMode.equals("Source")){
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Open Resource File");
		    fileChooser.showOpenDialog(primaryStage);
		}else if (fileChooseMode.equals("Output")){
	     
	    }
	}
	
	
    
		
	
}
