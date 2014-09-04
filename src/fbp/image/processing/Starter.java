package fbp.image.processing;

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
	
	private Stage primaryStage;
	
	public static void main(String[] args) throws Exception {
		//new ImageProcessorAWT();
		launch(args);
	}

    @Override
    public void start(final Stage stage) {
    	
		
    	
    	primaryStage = stage;
    	
        primaryStage.setTitle(APP_CAPTION);
        
        InitialSettings initialSettings = new InitialSettings();
        
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
        
        //StackPane rootStackPane = new StackPane();
        GridPane rootGrid = new GridPane();
        rootGrid.setGridLinesVisible(true);
        rootGrid.setHgap(10);
        rootGrid.setVgap(10);
        rootGrid.setPadding(new Insets(5, 5, 5, 5));
        
        Text titleText = new Text(APP_CAPTION);
        titleText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        rootGrid.add(titleText, 0, 0, 2, 1);

        Label sourceLabel = new Label("Source folder:");
        rootGrid.add(sourceLabel, 0, 1);

        TextField sourceField = new TextField();
        rootGrid.add(sourceField, 1, 1);
        
        Button chooseSourceBtn = new Button();
        chooseSourceBtn.setText("...");
        rootGrid.add(chooseSourceBtn, 2, 1);

        Label destinationLabel = new Label("Destination folder:");
        rootGrid.add(destinationLabel, 0, 2);

        TextField outputField = new TextField ();
        rootGrid.add(outputField, 1, 2);
        
        Button chooseDestinationBtn = new Button();
        chooseDestinationBtn.setText("...");
        rootGrid.add(chooseDestinationBtn, 2, 2);

        Label watermarkTextLabel = new Label("Watermark text:");
        rootGrid.add(watermarkTextLabel, 0, 3);

        TextField watermarkTextField = new TextField ();
        rootGrid.add(watermarkTextField, 1, 3);
        
        Label watermarkFontSizeLabel = new Label("Font size:");
        rootGrid.add(watermarkFontSizeLabel, 0, 4);

        TextField watermarkFontSizeField = new TextField ();
        rootGrid.add(watermarkFontSizeField, 1, 4);
        
        Label watermarkOpacityLabel = new Label("Opacity:");
        rootGrid.add(watermarkOpacityLabel, 0, 5);

        TextField watermarkOpacityField = new TextField ();
        rootGrid.add(watermarkOpacityField, 1, 5);
        
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
        //verticalGraphicsContext.drawImage(arg0, arg1, arg2);
//        verticalGraphicsContext.drawImage
//        	(testImage, 0, 0, SCALED_PREVIW_X, SCALED_PREVIW_Y);
        rootGrid.add(verticalCanvas, 0, 6, 2, 1);

        Canvas horizontalCanvas = new Canvas (SCALED_PREVIW_Y, SCALED_PREVIW_X);
        GraphicsContext horizontalGraphicsContext = horizontalCanvas.getGraphicsContext2D();
        horizontalGraphicsContext.setFill(Color.web("#ffffff",1));
        horizontalGraphicsContext.fillRect(0, 0, SCALED_PREVIW_Y, SCALED_PREVIW_X);
        rootGrid.add(horizontalCanvas, 0, 7, 4, 1);

        
        Canvas mainCanvas = new Canvas (IMAGE_SIZE_H, IMAGE_SIZE_H);
        GraphicsContext mainGraphicsContext = mainCanvas.getGraphicsContext2D();
        mainGraphicsContext.setFill(Color.web("#ffffff",1));
        mainGraphicsContext.fillRect(0, 0, IMAGE_SIZE_H, IMAGE_SIZE_H);
        rootGrid.add(mainCanvas, 3, 0, 1, 9);        
        
        
        
        
//        root.getChildren().add(btn);
        Scene primaryScene = new Scene(rootGrid, 1200, 740);
        
        outputField.setText(initialSettings.getOutput());
        sourceField.setText(initialSettings.getSource());
        
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