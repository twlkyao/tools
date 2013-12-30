package com.twlkyao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * File operation including create file,
 * copy file, copy directory, move file,
 * move directory, delete file and delete directory.
 * @author Shiyao Qi
 * @date 2013.12.30
 * @email qishiyao2008@126.com
 */
public class FileOperation {
	
	/**
	 * Create a new file.
	 * @param file The fle to be created.
	 * @return True, if the file create successfully.
	 */
	public boolean createFile(String filePath) {
		
		File file = new File(filePath); // Create a new File instance.
		if(!file.exists()) { // Only create a new file, if the file does not exists.
			try {
				file.createNewFile(); // Create a new, empty file.
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Copy to file specified by srcFilePath to the directory specified by desFilePath.
	 * @param srcFilePath The file path of the file to be copied.
	 * @param desFilePath The file path of the file to be copied to.
	 * @return True, if the copy is succeeded.
	 */
	public boolean copyFile(String srcFilePath, String desFilePath) {
		try {
//			int bytesum = 0; // The size of the file in bytes.
			int byteread = 0; // The bytes read to the buffer.
			String desDirectoryPath = ""; // The new file path of the file.
			File srcFile = new File(srcFilePath); // Create a File instance according to the srcFilePath.
			
			// Return false if the srcFile does not exist.
			if(!srcFile.exists()) {
				return false;
			}
			
			if(desFilePath.endsWith(File.separator)) { // The desFilePath is a directory, copy the file to the directory.
				desDirectoryPath = desFilePath + srcFile.getName();
			} else { // The desFilePath is a file path.
				desDirectoryPath = desFilePath + File.separator + srcFile.getName(); // Construct a directory file path.
			}
			
			// Create the file directory.
			new File(desFilePath).mkdirs(); // Create the directory recursively.
			
			// Create a new, empty file according to the filePath.
			new File(desDirectoryPath).createNewFile();
			
			// The srcFilePath is valid, then copy the content of the srcFile to the file specified by filePath.
			if(srcFile.exists()) {
				
				// Read in the original file.
				FileInputStream fileInputStream = new FileInputStream(srcFilePath); // Create an InputStream to read the srcFile.
				FileOutputStream fileOutputStream = new FileOutputStream(desDirectoryPath); // Create a FileOutputStream to write the content.
				byte[] buffer = new byte[1024]; // A byte array to buffer the content of the file.
				while((byteread = fileInputStream.read(buffer)) != -1) {
					
					// The total bytes of the file.
//					bytesum += byteread; // Calculate the size of the srcFile in bytes.
					fileOutputStream.write(buffer, 0, byteread); // Write the content to the filePath.
				}
				fileInputStream.close(); // Close the fileInputStream.
				fileOutputStream.close(); // Close the fileOutputStream.
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Copy the files under srcDir to desDir.
	 * @param srcDir The src directory.
	 * @param desDir The des directory.
	 * @return True, if the copy is succeeded.
	 */
	public boolean copyDir(String srcDirectory, String desDirectory) {
		
		// The directory to copy.
		File srcDir = new File(srcDirectory);
		String srcFilePath = ""; // The src file path of the file.
		String desDirectoryPath = ""; // The des directory path of the file.
		
		// Return false if the srcDir does not exist.
		if(!srcDir.exists()) {
			return false;
		}
		
		// Construct the file path of the new File directory.
		if(desDirectory.endsWith(File.separator)) {
			desDirectoryPath = desDirectory + srcDir.getName();
		} else {
			desDirectoryPath = desDirectory + File.separator + srcDir.getName();
		}
		
		// Create the directory recursively.
		new File(desDirectoryPath).mkdirs();
		
		File[] files = srcDir.listFiles(); // Get all the files under the srcDirectory.
		
		// Copy all the files under the srcDirectory to the desDirectory.
		for(int i = 0; i < files.length; i++) {
			// The file path to be copied.
			srcFilePath = srcDir + File.separator + files[i].getName();
			
			if(files[i].isFile()) { // The files[i] is a file.
				copyFile(srcFilePath, desDirectoryPath);
			} else { // The files[i] is a directory, recursively call itself.
				copyDir(srcFilePath, desDirectoryPath);
			}
		}
		return true;
	}
	
	/**
	 * Move file specified by srcFilePath to file path specified by desFilepath.
	 * @param srcFilePath The file path of the file to be moved.
	 * @param desFilePath The file path of the file to be moved to.
	 * @return True, if the move is succeeded.
	 */
	public boolean moveFile(String srcFilePath, String desFilePath) {
		try{
			// First copy the file, then delete the src file.
			if(copyFile(srcFilePath, desFilePath)) {
				new File(srcFilePath).delete(); // Delete the file if the copy is succeeded.
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Move files under the directory specified by srcDir to directory specified by desDir.
	 * @param srcDir The file directory of the files to be moved.
	 * @param desDir The file directory of the files to be moved to.
	 * @return True, if the move is succeeded.
	 */
	public boolean moveDir(String srcDir, String desDir) {
		
		// First copy the files under the directory, then delete files under the directory.
		if(copyDir(srcDir, desDir)) {
			delDir(srcDir); // Call the function to delete the files under the directory.
			return true;
		} else { // The copy operation fails.
			return false;
		}
		
	}
	
	/**
	 * Delete the file specified by file path.
	 * @param filePath The file path of the file to be deleted.
	 * @return True, if the file is deleted.
	 */
	public boolean delFile(String filePath) {
		File file = new File(filePath); // Create a new File instance specified by filePath.
		
		// Return false if the file does not exist.
		if(!file.exists()) {
			return false;
		} else { // The file exists.
			file.delete(); // Delete the file.
			return true;
		}
	}
	
	/**
	 * Delete the file directory and all the files under it.
	 * @param filePath The file directory to be deleted.
	 * @return True, if the file directory is deleted.
	 */
	public boolean delDir(String filePath) {
		boolean status = false; // To indicate the status.
		File file = new File(filePath); // Create a new File instance.
		
		// Return false if the file does not exist.
		if(!file.exists()) {
			status = false;
		} else { // Delete the files under the directory.
			File[] files = file.listFiles(); // Get all the files under the file directory.
			for(int i = 0; i < files.length; i++) {
				if(files[i].isDirectory()) { // If the files[i] is directory, recursively call itself.
					if(!delDir(files[i].getPath())) { // Return false, if the delete is failed.
						return false;
					}
				} else { // The files[i] is a file, then delete it.
					files[i].delete(); // Delete the files[i].
				}
			}
			file.delete(); // Delete the file directory.
			status = true; // Set the indicator.
		}
		return status; // Return the status.
	}
}
