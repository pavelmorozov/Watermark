package fbp.image.processing;

import java.util.HashSet;
import java.util.Set;

public class FileProcessor {
	FileProcessor(){
		
	}
	
	public Set<String> listFiles(String initialFolder){
		System.out.println("Working Directory: " + 
				System.getProperty("user.dir"));    	
		
		Set<String> fileSet = new HashSet<String>();
		fileSet.add("1.original.jpg");		
		fileSet.add("2.original.jpg");

		return fileSet;
	}  

}
