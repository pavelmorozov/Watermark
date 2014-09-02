package fbp.image.processing;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileProcessor {
	
	private String workDirectory;
	private static final String OUTPUT = "output";
	
	FileProcessor(){
		
	}
	
	public String getOutputFolderName(){
		return OUTPUT;
	}
	
	public Set<File> listFiles(String initialFolder){
		//########## Here change to initialFolder ##########  
		workDirectory = System.getProperty("user.dir");
		
		System.out.println("Working Directory: " + 
				workDirectory);

		System.out.println("Create folder: " + 
				OUTPUT);
		boolean created = (new File(workDirectory+'/'+OUTPUT)).mkdirs();
		if (!created) System.out.println("Created sucessfully");
		else System.out.println("Folder not created. May be exist?");

	    // create a file that is really a directory
	    File aDirectory = new File(workDirectory);
	    
	    FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
               if(name.lastIndexOf('.')>0)
               {
                  // get last index for '.' char
                  int lastIndex = name.lastIndexOf('.');
                  
                  // get extension
                  String str = name.substring(lastIndex);
                  
                  // match path name extension
                  if(str.equals(".jpg"))
                  {
                     return true;
                  }
                  // match path name extension
                  if(str.equals(".jpeg"))
                  {
                     return true;
                  }
                  if(str.equals(".gif"))
                  {
                     return true;
                  }
                  if(str.equals(".png"))
                  {
                     return true;
                  }
                  if(str.equals(".tiff"))
                  {
                     return true;
                  }                  
               }
               return false;
            }
         };
         
         // returns pathnames for files and directory
         File[] filesInDir = aDirectory.listFiles(fileNameFilter);	    
	    
	    // get a listing of all files in the directory	    
		Set<File> fileSet = new HashSet<File> (Arrays.asList(filesInDir));;
//		fileSet.add("1.original.jpg");		
//		fileSet.add("2.original.jpg");
//		fileSet.add("1.jpg");
//		fileSet.add("2.jpg");
		
		System.out.println("found files:"+ fileSet.size());
		
		return fileSet;
	}  

}
