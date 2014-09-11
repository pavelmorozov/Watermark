package fbp.image.processing;

import java.io.File;
import java.util.List;
import java.util.SortedSet;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Starter extends Application{

	private static final String APP_CAPTION = "Resize & Watermark";
	private final static int IMAGE_SIZE_H = 720;
	private static final Integer SCALED_PREVIW_X = 192;
	private static final Integer SCALED_PREVIW_Y = 288;
	private ImageProcessorAWT imageProcessor;
	private Stage primaryStage;
	private Preferences preferences;
	private FileProcessor fileProcessor;
<<<<<<< HEAD
	private Image lastProcessed;
=======
>>>>>>> branch 'master' of https://github.com/pavelmorozov/Watermark.git
	
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
    	chooseSourceBtn,
    	chooseDestinationBtn,
	    choosePreviewFileBtn,
	    choosePreviewPrevBtn,
	    choosePreviewNextBtn,
	    processFolderStartBtn,
    	processFolderStopBtn,
    	redrawBtn;
    Label
    	statusLabel;
    ColorPicker 
    	colorPicker; 
    
	public static void main(String[] args) throws Exception {
		launch(args);
	}

    @Override
    public void start(final Stage stage) throws NumberFormatException, Exception {
    	preferences = Preferences.userRoot().node(this.getClass().getName());
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
        
        chooseSourceBtn = new Button();
        chooseSourceBtn.setText("...");
        rootGrid.add(chooseSourceBtn, 5, 1);

        Label destinationLabel = new Label("Destination folder:");
        rootGrid.add(destinationLabel, 0, 2);

        outputField = new TextField ();
        outputField.setId("outputField");
        rootGrid.add(outputField, 1, 2, 4, 1);
        
        chooseDestinationBtn = new Button();
        chooseDestinationBtn.setText("...");
        rootGrid.add(chooseDestinationBtn, 5, 2);

        Label watermarkTextLabel = new Label("Watermark text:");
        rootGrid.add(watermarkTextLabel, 0, 3);
        
        watermarkTextField = new TextField ();
        rootGrid.add(watermarkTextField, 1, 3, 4, 1);
        
        redrawBtn = new Button();
        redrawBtn.setText("ReDraw");
        redrawBtn.setTooltip(new Tooltip(" Redraw preview \n images "));
        rootGrid.add(redrawBtn, 4, 5);
        
        Label watermarkFontSizeLabel = new Label("Font size:");
        rootGrid.add(watermarkFontSizeLabel, 0, 4);

        watermarkFontSizeField = new TextField ();
        watermarkFontSizeField.setMaxWidth(40);        
        rootGrid.add(watermarkFontSizeField, 1, 4, 1, 1);
        
        Label watermarkOpacityLabel = new Label("Color:");
        rootGrid.add(watermarkOpacityLabel, 0, 5);

//        watermarkOpacityField = new TextField ();
//        watermarkOpacityField.setMaxWidth(40);
//        rootGrid.add(watermarkOpacityField, 1, 5, 1, 1);
        
        colorPicker = new ColorPicker(Color.web("#ffcce6"));
        rootGrid.add(colorPicker, 1, 5, 2, 1);
        
        Label previewFileLabel = new Label("Preview:");
        rootGrid.add(previewFileLabel, 0, 6);

        previewFileField = new TextField();
        rootGrid.add(previewFileField, 1, 6, 4, 1);
        
        choosePreviewFileBtn = new Button();
        choosePreviewFileBtn.setText("...");
        rootGrid.add(choosePreviewFileBtn, 5, 6);
        
        choosePreviewPrevBtn = new Button();
        choosePreviewPrevBtn.setText("- < -");
        choosePreviewPrevBtn.setTooltip(new Tooltip("Previous image\n preview"));
        
        rootGrid.add(choosePreviewPrevBtn, 1, 7);
        
        choosePreviewNextBtn = new Button();
        choosePreviewNextBtn.setText("- > -");
        choosePreviewNextBtn.setTooltip(new Tooltip("Next image\n preview"));
        rootGrid.add(choosePreviewNextBtn, 2, 7);

        processFolderStartBtn = new Button();
        processFolderStartBtn.setText("Start");
        rootGrid.add(processFolderStartBtn, 3, 7);
        
        processFolderStopBtn = new Button();
        processFolderStopBtn.setText("Stop");
        rootGrid.add(processFolderStopBtn, 4, 7);
        
        statusLabel = new Label("Ready");
        rootGrid.add(statusLabel, 3, 8, 2, 1);
        
        verticalCanvas = new Canvas (SCALED_PREVIW_X, SCALED_PREVIW_Y);
        verticalGraphicsContext = verticalCanvas.getGraphicsContext2D();
        rootGrid.add(verticalCanvas, 0, 8, 5, 2);

        horizontalCanvas = new Canvas (SCALED_PREVIW_Y, SCALED_PREVIW_X);
        horizontalGraphicsContext = horizontalCanvas.getGraphicsContext2D();
        rootGrid.add(horizontalCanvas, 0, 10, 7, 1);
        
        mainCanvas = new Canvas (IMAGE_SIZE_H, IMAGE_SIZE_H);
        mainGraphicsContext = mainCanvas.getGraphicsContext2D();
        rootGrid.add(mainCanvas, 6, 0, 1, 11);
        
        /////////////////////////////////////////////////////////////////////////////
        // Initial fields settings
        /////////////////////////////////////////////////////////////////////////////
        
        getPreferences();

        fileProcessor.setSourceFolderString(sourceField.getText());
        fileProcessor.listFiles();
        
        /////////////////////////////////////////////////////////////////////////////
        // Draw watermark preview
        /////////////////////////////////////////////////////////////////////////////
        
        redraw("From file");
        
        /////////////////////////////////////////////////////////////////////////////
        // Add listeners
        /////////////////////////////////////////////////////////////////////////////
        
        listeners();
        
        /////////////////////////////////////////////////////////////////////////////
        // Create primaryScene and show primaryStage
        /////////////////////////////////////////////////////////////////////////////
        
        Scene primaryScene = new Scene(rootGrid, 1120, 730);
        primaryScene.getStylesheets().add(Starter.class.getResource("ui.css").toExternalForm());
        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryStage.setMaxHeight(primaryStage.getHeight());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMaxWidth(primaryStage.getWidth());
        primaryStage.setMinWidth(primaryStage.getWidth());
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
//		watermarkTextField.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//		        redraw();
//		    }
//		});
		watermarkFontSizeField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw("Current image");
		    }
		});
//		watermarkOpacityField.textProperty().addListener(new ChangeListener<String>() {
//		    @Override
//		    public void changed(ObservableValue<? extends String> observable,
//		            String oldValue, String newValue) {
//		        redraw();
//		    }
//		});
		previewFileField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		            String oldValue, String newValue) {
		        redraw("From file");
		    }
		});
		
		sourceField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable,
		    		String oldValue, String newValue) {
        		fileProcessor.setSourceFolderString(sourceField.getText());
        		fileProcessor.listFiles();
        		SortedSet<File> fileSet = fileProcessor.getFileSet();
        		if ((fileSet!=null)&&(fileSet.size()!=0)){
        			previewFileField.setText(fileSet.first().getPath());
        		}else{
        			previewFileField.setText("");
        		}		    	
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
	        }
		});
		processFolderStartBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @SuppressWarnings("unchecked")
			@Override
	        public void handle(ActionEvent event){
        		try {
					imageProcessor = new ImageProcessorAWT();
<<<<<<< HEAD
					//this redraw to set up processor
					redraw("Current image");
	                final Thread processFolderThread = new Thread(imageProcessor);
	            	statusLabel.textProperty().bind(imageProcessor.messageProperty());
	            	imageProcessor.setOnSucceeded(
	        			new EventHandler<WorkerStateEvent>(){
							@Override
							public void handle(final WorkerStateEvent arg0) {
								System.out.println("thread succeeded");
								sourceField.setDisable(false);
								outputField.setDisable(false);
								watermarkFontSizeField.setDisable(false);
								watermarkTextField.setDisable(false);
								//watermarkOpacityField.setDisable(false);
								colorPicker.setDisable(false);
								redrawBtn.setDisable(false);
								previewFileField.setDisable(false);
						    	chooseSourceBtn.setDisable(false);
						    	chooseDestinationBtn.setDisable(false);
							    choosePreviewFileBtn.setDisable(false);
							    choosePreviewPrevBtn.setDisable(false);
							    choosePreviewNextBtn.setDisable(false);
							    processFolderStartBtn.setDisable(false);
						    	processFolderStopBtn.setDisable(true);
							}
	        				
	        			});
					sourceField.setDisable(true);
					outputField.setDisable(true);
					watermarkFontSizeField.setDisable(true);
					watermarkTextField.setDisable(true);
					//watermarkOpacityField.setDisable(true);
					colorPicker.setDisable(true);
					redrawBtn.setDisable(true);
=======
					redraw();
	                final Thread processFolderThread = new Thread(imageProcessor);
	            	statusLabel.textProperty().bind(imageProcessor.messageProperty());
	            	imageProcessor.setOnSucceeded(
	        			new EventHandler<WorkerStateEvent>(){
							@Override
							public void handle(final WorkerStateEvent arg0) {
								System.out.println("thread succeeded");
								sourceField.setDisable(false);
								outputField.setDisable(false);
								watermarkFontSizeField.setDisable(false);
								watermarkTextField.setDisable(false);
								watermarkOpacityField.setDisable(false);
								previewFileField.setDisable(false);
						    	chooseSourceBtn.setDisable(false);
						    	chooseDestinationBtn.setDisable(false);
							    choosePreviewFileBtn.setDisable(false);
							    choosePreviewPrevBtn.setDisable(false);
							    choosePreviewNextBtn.setDisable(false);
							    processFolderStartBtn.setDisable(false);
						    	processFolderStopBtn.setDisable(true);
							}
	        				
	        			});
					sourceField.setDisable(true);
					outputField.setDisable(true);
					watermarkFontSizeField.setDisable(true);
					watermarkTextField.setDisable(true);
					watermarkOpacityField.setDisable(true);
>>>>>>> branch 'master' of https://github.com/pavelmorozov/Watermark.git
					previewFileField.setDisable(true);
			    	chooseSourceBtn.setDisable(true);
			    	chooseDestinationBtn.setDisable(true);
				    choosePreviewFileBtn.setDisable(true);
				    choosePreviewPrevBtn.setDisable(true);
				    choosePreviewNextBtn.setDisable(true);
				    processFolderStartBtn.setDisable(true);
			    	processFolderStopBtn.setDisable(false);	            	
	        		processFolderThread.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
		});
		
		processFolderStopBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
	        public void handle(ActionEvent event){
				imageProcessor.setStopFolderProcess();
	        }
<<<<<<< HEAD
		});
=======
		}
				
		);
>>>>>>> branch 'master' of https://github.com/pavelmorozov/Watermark.git
		
    	choosePreviewFileBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event){
	        	FileChooser fileChooser = new FileChooser();
	        	fileChooser.setTitle("Choose file to preview");
	        	File initialDirectory = new File(sourceField.getText());
	        	if(initialDirectory.exists() && initialDirectory.isDirectory()){
	        		fileChooser.setInitialDirectory(initialDirectory);
	        	}	        	
	        	File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                	previewFileField.setText(file.getPath());
                	//sourceField.setText(file.getParent());
                }
	        }
		});
    	
    	chooseSourceBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event){
	        	DirectoryChooser directoryChooser = new DirectoryChooser();
	        	directoryChooser.setTitle("Choose source folder");
	        	File initialDirectory = new File(sourceField.getText());
	        	if(initialDirectory.exists() && initialDirectory.isDirectory()){
	        		directoryChooser.setInitialDirectory(initialDirectory);
	        	}
	        	File file = directoryChooser.showDialog(primaryStage);
	        	if (file != null) {
	        		sourceField.setText(file.getPath());
	        		SortedSet<File> fileSet = fileProcessor.getFileSet();
	        		if ((fileSet!=null)&&(fileSet.size()!=0)){
	        			previewFileField.setText(fileSet.first().getPath());
		        	} else {
		        		previewFileField.setText("");
	        		}
	        	}

	        }
		});
    	
    	chooseDestinationBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event){
	        	DirectoryChooser directoryChooser = new DirectoryChooser();
	        	directoryChooser.setTitle("Choose output folder");
	        	File initialDirectory = new File(outputField.getText());
	        	if(initialDirectory.exists() && initialDirectory.isDirectory()){
	        		directoryChooser.setInitialDirectory(initialDirectory);
	        	}
	        	File file = directoryChooser.showDialog(primaryStage);
	        	if (file != null) {
	        		outputField.setText(file.getPath());;
	        	}	        	
	        }
		});
<<<<<<< HEAD
    	
    	redrawBtn.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event){
	        	//test font class
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	List<String> fontNames = Font.getFontNames();
	        	for(String fontName: fontNames){
	        		System.out.println(fontName);
	        		//Font f = new Font(fontName);
	        		Font f = new Font(fontName,10); 
	        	}	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	redraw("Current image");
	        }
		});
    	
//        colorPicker.setOnAction(new  EventHandler() {
//            public void handle(Event event) {
//            	redraw();
//            }
//        });    	
=======
>>>>>>> branch 'master' of https://github.com/pavelmorozov/Watermark.git
		
        
        
//        colorPicker.setOnAction(new EventHandler() {
//            public void handle(Event t) {
//                text.setFill(colorPicker.getValue());               
//            }
//        });        
        
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	        	  setPreferences();
	        	  imageProcessor.setStopFolderProcess();
	          }
		});        		
	}
	
	private void getPreferences(){
		sourceField.setText(preferences.get("sourceField", ""));
		outputField.setText(preferences.get("outputField", ""));
		watermarkFontSizeField.setText(Integer.toString(preferences.getInt("watermarkFontSizeField", 0)));
		//watermarkOpacityField.setText(Double.toString(preferences.getDouble("watermarkOpacityField", 0.0)));
		watermarkTextField.setText(preferences.get("watermarkTextField", ""));
		previewFileField.setText(preferences.get("previewFileField", ""));
		colorPicker.setValue(Color.valueOf(preferences.get("textColor", "")));
//		try {
//			colorPicker = new ColorPicker(
//					Color.valueOf(preferences.get("textColor", ""))
//			);
//		}catch (Exception e){
//			System.out.println(e);
//		}
	}

	private void setPreferences(){
        preferences.put("sourceField", sourceField.getText());
        preferences.put("outputField", outputField.getText());
        preferences.putInt("watermarkFontSizeField", Integer.parseInt(watermarkFontSizeField.getText()));
        //preferences.putDouble("watermarkOpacityField",Double.parseDouble(watermarkOpacityField.getText().replace(',', '.')));
        preferences.put("watermarkTextField", watermarkTextField.getText());
        preferences.put("previewFileField", previewFileField.getText());
        preferences.put("textColor", colorPicker.getValue().toString());
	}
	
	private void redraw(String redrawMode){
        imageProcessor.setWatermarkText(watermarkTextField.getText());
        imageProcessor.setFontSize(Integer.parseInt(watermarkFontSizeField.getText()));
        //imageProcessor.setOpacity(Double.parseDouble(watermarkOpacityField.getText().replace(',', '.')));
        imageProcessor.setTextColor(colorPicker.getValue());
        imageProcessor.setSourceFolder(sourceField.getText());
        imageProcessor.setOutputFolder(outputField.getText());

        verticalGraphicsContext.setFill(Color.web("#ffffff",1));
        verticalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_X, SCALED_PREVIW_Y);
        Image previewImage = verticalCanvas.snapshot(null, null);        
        imageProcessor.setImageToProcess(previewImage);
        imageProcessor.processImage();
        previewImage = imageProcessor.getImageProcessed();
        verticalGraphicsContext.drawImage
    		(previewImage, 0, 0, verticalCanvas.getWidth(), verticalCanvas.getHeight());

        horizontalGraphicsContext.setFill(Color.web("#ffffff",1));
        horizontalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_Y, SCALED_PREVIW_X);
        previewImage = horizontalCanvas.snapshot(null, null);
        imageProcessor.setImageToProcess(previewImage);
        imageProcessor.processImage();
        previewImage = imageProcessor.getImageProcessed();
        horizontalGraphicsContext.drawImage
    		(previewImage, 0, 0, horizontalCanvas.getWidth(), horizontalCanvas.getHeight());
        
        mainGraphicsContext.setFill(Color.web("#555555",1));
        mainGraphicsContext.fillRect(0, 0, IMAGE_SIZE_H, IMAGE_SIZE_H);
        
        if (redrawMode.equals("Current image")) {
        	//Image already set, so nothing to do
        	imageProcessor.setImageToProcess(lastProcessed);
        }else if (redrawMode.equals("From file")){
        	imageProcessor.setImageToProcessFromFile(previewFileField.getText());
        	lastProcessed = imageProcessor.getImageToProcess(); 
        }

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
