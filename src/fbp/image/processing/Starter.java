package fbp.image.processing;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Starter extends Application{

	private static final String APP_CAPTION = "Resize & Watermark";
	private String sourceCatalogPath;
	private String outputCatalogPath;
	
	public static void main(String[] args) throws Exception {
		//new ImageProcessorAWT();
		launch(args);
	}

    @Override
    public void start(final Stage primaryStage) {
    	
        primaryStage.setTitle(APP_CAPTION);
        
        
        
        Button openSourceCatalogBtn = new Button();
        openSourceCatalogBtn.setText("Open source catalog");
        openSourceCatalogBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	chooseFile("Source");
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.showOpenDialog(primaryStage);                
            }
        });
        
        StackPane rootStackPane = new StackPane();
//        root.getChildren().add(btn);
        Scene primaryScene = new Scene(rootStackPane, 1200, 740);
        

        
        
        
        
        
        primaryScene.getStylesheets().add(Starter.class.getResource("ui.css").toExternalForm());
        rootStackPane.getChildren().add(openSourceCatalogBtn);
        
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