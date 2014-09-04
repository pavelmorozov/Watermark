package fbp.image.processing;

public class InitialSettings {
	
	private String sourceCatalogPath = "d:/java/projects/watermark";
	private String outputCatalogPath = "d:/java/projects/watermark/output";
	
	public InitialSettings(){}
	
	public String getSource(){
		return sourceCatalogPath;
	}
	
	public String getOutput(){
		return outputCatalogPath;
	}	

}
