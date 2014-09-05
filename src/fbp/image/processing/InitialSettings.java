package fbp.image.processing;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class InitialSettings {
	
	private String sourceCatalogPath = "d:/java/projects/watermark";
	private String outputCatalogPath = "d:/java/projects/watermark/output";
	
	public InitialSettings(GridPane rootGrid){

		ObservableList<Node> nodesList =  rootGrid.getChildren();

		for (Node node:nodesList) {
			System.out.println(node.getId());
//			switch (node.getId()) {
//				case "outputField"
//				case "outputField1"
//			}
//	        outputField.setText(initialSettings.getOutput());
//	        sourceField.setText(initialSettings.getSource());
//	        watermarkTextField
//	        watermarkFontSizeField
//	        watermarkOpacityField
//	        previewFileField
		}
	}
	
	public String getSource(){
		return sourceCatalogPath;
	}
	
	public String getOutput(){
		return outputCatalogPath;
	}	

}
