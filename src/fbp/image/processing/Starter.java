package fbp.image.processing;

import java.io.File;
import java.util.SortedSet;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	
	FileProcessor fileProcessor;
	
	TextField 
		sourceField,
		outputField,
		watermarkFontSizeField, 
		watermarkTextField,
		watermarkOpacityField,
		previewFileField;
	
    GraphicsContext 
    	verticalGraphicsContext,
    	horizontalGraphicsContext,
    	mainGraphicsContext;
    
    Canvas 
    	verticalCanvas,
    	horizontalCanvas,
    	mainCanvas;
    Button
	    choosePreviewFileBtn,
	    choosePreviewPrevBtn,
	    choosePreviewNextBtn,
	    processFolderStartBtn,
    	processFolderStopBtn;
	
	public static void main(String[] args) throws Exception {
		//new ImageProcessorAWT();
		launch(args);
	}

    @Override
    public void start(final Stage stage) throws NumberFormatException, Exception {
    	
		imageProcessor = new ImageProcessorAWT();
		fileProcessor = new FileProcessor();
        /////////////////////////////////////////////////////////////////////////////
        // Visual UI controls creation
        /////////////////////////////////////////////////////////////////////////////		
    	
    	primaryStage = stage;
        primaryStage.setTitle(APP_CAPTION);

        GridPane rootGrid = new GridPane();
        rootGrid.setId("rootGrid");
        rootGrid.setGridLinesVisible(true);
        rootGrid.setHgap(10);
        rootGrid.setVgap(10);
        rootGrid.setPadding(new Insets(5, 5, 5, 5));
        
        Text titleText = new Text(APP_CAPTION);
        titleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        rootGrid.add(titleText, 0, 0, 5, 1);

        Label sourceLabel = new Label("Source folder:");
        rootGrid.add(sourceLabel, 0, 1);

        sourceField = new TextField();
        sourceField.setId("sourceField");
        rootGrid.add(sourceField, 1, 1, 4, 1);
        
        Button chooseSourceBtn = new Button();
        chooseSourceBtn.setText("...");
        rootGrid.add(chooseSourceBtn, 5, 1);

        Label destinationLabel = new Label("Destination folder:");
        rootGrid.add(destinationLabel, 0, 2);

        outputField = new TextField ();
        outputField.setId("outputField");
        rootGrid.add(outputField, 1, 2, 4, 1);
        
        Button chooseDestinationBtn = new Button();
        chooseDestinationBtn.setText("...");
        rootGrid.add(chooseDestinationBtn, 5, 2);

        Label watermarkTextLabel = new Label("Watermark text:");
        rootGrid.add(watermarkTextLabel, 0, 3);

        watermarkTextField = new TextField ();
        rootGrid.add(watermarkTextField, 1, 3, 4, 1);
        
        Label watermarkFontSizeLabel = new Label("Font size:");
        rootGrid.add(watermarkFontSizeLabel, 0, 4);

        watermarkFontSizeField = new TextField ();
        watermarkFontSizeField.setMaxWidth(40);        
        rootGrid.add(watermarkFontSizeField, 1, 4, 1, 1);
        
        Label watermarkOpacityLabel = new Label("Opacity:");
        rootGrid.add(watermarkOpacityLabel, 0, 5);

        watermarkOpacityField = new TextField ();
        watermarkOpacityField.setMaxWidth(40);
        rootGrid.add(watermarkOpacityField, 1, 5, 1, 1);
        
        Label previewFileLabel = new Label("Preview:");
        rootGrid.add(previewFileLabel, 0, 6);

        previewFileField = new TextField();
        rootGrid.add(previewFileField, 1, 6, 4, 1);
        
        choosePreviewFileBtn = new Button();
        choosePreviewFileBtn.setText("...");
        rootGrid.add(choosePreviewFileBtn, 5, 6);        
        
        choosePreviewPrevBtn = new Button();
        choosePreviewPrevBtn.setText("- < -");
        rootGrid.add(choosePreviewPrevBtn, 1, 7);        
        
        choosePreviewNextBtn = new Button();
        choosePreviewNextBtn.setText("- > -");
        rootGrid.add(choosePreviewNextBtn, 2, 7);

        processFolderStartBtn = new Button();
        processFolderStartBtn.setText("Start");
        rootGrid.add(processFolderStartBtn, 3, 7);
        
        processFolderStopBtn = new Button();
        processFolderStopBtn.setText("Stop");
        rootGrid.add(processFolderStopBtn, 4, 7);        
        
//        Image verticalBlankImage = new Image("file:img/vertical.jpg");
        //Image testImage = new Image("file:output/1.original.jpg");
        
//        ImageView verticalImageView = new ImageView(); 
//        verticalImageView.setImage(verticalBlankImage);
//        
//        rootGrid.add(verticalImageView, 0, 6);        
        
        verticalCanvas = new Canvas (SCALED_PREVIW_X, SCALED_PREVIW_Y);
        verticalGraphicsContext = verticalCanvas.getGraphicsContext2D();
        rootGrid.add(verticalCanvas, 0, 8, 5, 1);

        horizontalCanvas = new Canvas (SCALED_PREVIW_Y, SCALED_PREVIW_X);
        horizontalGraphicsContext = horizontalCanvas.getGraphicsContext2D();
        rootGrid.add(horizontalCanvas, 0, 9, 7, 1);
        
        mainCanvas = new Canvas (IMAGE_SIZE_H, IMAGE_SIZE_H);
        mainGraphicsContext = mainCanvas.getGraphicsContext2D();
        rootGrid.add(mainCanvas, 6, 0, 1, 10);
        
        /////////////////////////////////////////////////////////////////////////////
        // Initial fields settings
        /////////////////////////////////////////////////////////////////////////////
        
        sourceField.setText("D:\\java\\projects\\Watermark");
        outputField.setText("d:\\java\\projects\\watermark\\output");
        watermarkTextField.setText("=== <Watermark text> ===");
        watermarkFontSizeField.setText("50");
        watermarkOpacityField.setText("0.2");
        previewFileField.setText("D:\\java\\projects\\Watermark\\1.original.jpg");
        String sourceFolder = sourceField.getText();
        fileProcessor.setSourceFolderString(sourceFolder);
        fileProcessor.listFiles();
        
        /////////////////////////////////////////////////////////////////////////////
        // Draw watermark preview
        /////////////////////////////////////////////////////////////////////////////
        
        redraw();
        
        /////////////////////////////////////////////////////////////////////////////
        // Add listeners
        /////////////////////////////////////////////////////////////////////////////
        
        listeners();
        
        /////////////////////////////////////////////////////////////////////////////
        // Create primaryScene and show primaryStage
        /////////////////////////////////////////////////////////////////////////////
        
        Scene primaryScene = new Scene(rootGrid, 1100, 730);
        primaryScene.getStylesheets().add(Starter.class.getResource("ui.css").toExternalForm());
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
	
	private void listeners(){
		watermarkTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw();
		    }
		});
		watermarkFontSizeField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw();
		    }
		});
		watermarkOpacityField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw();
		    }
		});
		previewFileField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw();
		    }
		});
		choosePreviewPrevBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	SortedSet<File> fileSet = fileProcessor.getFileSet();
	        	File previousFile = null;
	        	for (File imageFile: fileSet){
	        		if (imageFile.getPath().equals(previewFileField.getText())
	        				&&(previousFile!=null)){
	        			previewFileField.setText(previousFile.getPath());
	        			break;
	        		}
	        		previousFile = imageFile;
	        	}
	        	redraw();
	        }
		});
		choosePreviewNextBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	SortedSet<File> fileSet = fileProcessor.getFileSet();
	        	File currentFile = null;
	        	for (File imageFile: fileSet){
	        		if (currentFile!=null){
	        			previewFileField.setText(imageFile.getPath());
	        			break;
	        		}
	        		if (imageFile.getPath().equals(previewFileField.getText())){
	        			currentFile = imageFile;
	        		}
	        	}
	        	redraw();
	        }
		});
		processFolderStartBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event){
	        	imageProcessor.setSourceFolder(sourceField.getText());
	        	imageProcessor.setOutputFolder(outputField.getText());
	        	try {
					imageProcessor.processFolder();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
		});
	}
	
	private void redraw(){
        imageProcessor.setWatermarkText(watermarkTextField.getText());
        imageProcessor.setFontSize(Integer.parseInt(watermarkFontSizeField.getText()));
        imageProcessor.setOpacity(Double.parseDouble(watermarkOpacityField.getText().replace(',', '.')));

        verticalGraphicsContext.setFill(Color.web("#F1DFC7",1));
        verticalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_X, SCALED_PREVIW_Y);
        Image previewImage = verticalCanvas.snapshot(null, null);        
        imageProcessor.setImageToProcess(previewImage);
        imageProcessor.processImage();
        previewImage = imageProcessor.getImageProcessed();
        verticalGraphicsContext.drawImage
    		(previewImage, 0, 0, verticalCanvas.getWidth(), verticalCanvas.getHeight());

        horizontalGraphicsContext.setFill(Color.web("#F1DFC7",1));
        horizontalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_Y, SCALED_PREVIW_X);
        previewImage = horizontalCanvas.snapshot(null, null);
        imageProcessor.setImageToProcess(previewImage);
        imageProcessor.processImage();
        previewImage = imageProcessor.getImageProcessed();
        horizontalGraphicsContext.drawImage
    		(previewImage, 0, 0, horizontalCanvas.getWidth(), horizontalCanvas.getHeight());
        
        mainGraphicsContext.setFill(Color.web("#F1DFC7",1));
        mainGraphicsContext.fillRect(0, 0, IMAGE_SIZE_H, IMAGE_SIZE_H);
        imageProcessor.setImageToProcessFromFile(previewFileField.getText());
        imageProcessor.processImage();
        previewImage = imageProcessor.getImageProcessed();
        
        double x = 0; double y = 0;
        
        if (previewImage.getWidth()<previewImage.getHeight()){
        	//vertical align
        	x = (mainCanvas.getWidth() - previewImage.getWidth())/2;
        	y = 0;
        } else {
        	//horizontal align
        	x = 0;
        	y = (mainCanvas.getHeight() - previewImage.getHeight())/2;
        }
        
        mainGraphicsContext.drawImage
        	(previewImage, x, y, previewImage.getWidth(), previewImage.getHeight());        
	}
}
