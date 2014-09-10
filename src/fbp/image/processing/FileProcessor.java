package fbp.image.processing;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class FileProcessor{
	private String sourceFolderString;
	private SortedSet<File> fileSet;

	FileProcessor() {
	}

	public void setSourceFolderString(String sourceFolderString) {
		this.sourceFolderString = sourceFolderString;
	}
	
	public SortedSet<File> getFileSet(){
		return fileSet;
	}

	public void listFiles() {
		// create a file that is really a directory
		File sourceDirectoryFile = new File(sourceFolderString);

		FilenameFilter fileNameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.lastIndexOf('.') > 0) {
					// get last index for '.' char
					int lastIndex = name.lastIndexOf('.');

					// get extension
					String str = name.substring(lastIndex);
					str = str.toLowerCase();

					// match path name extension
					if (str.equals(".jpg")) {
						return true;
					}
					// match path name extension
					if (str.equals(".jpeg")) {
						return true;
					}
					if (str.equals(".gif")) {
						return true;
					}
					if (str.equals(".png")) {
						return true;
					}
					if (str.equals(".tiff")) {
						return true;
					}
				}
				return false;
			}
		};

		// returns pathnames for files and directory
		File[] filesInDir = sourceDirectoryFile.listFiles(fileNameFilter);

		// get a listing of all files in the directory
		try{
			fileSet = new TreeSet<File>(Arrays.asList(filesInDir));
			System.out.println("found files:" + fileSet.size());
		}catch(Exception e){
			fileSet = null;
			System.out.println(e);
		}
	}
}