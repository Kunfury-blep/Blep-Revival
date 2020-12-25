package com.kunfury.blepRevival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.List;

import org.bukkit.Bukkit;

public class FileHandler {

	public static String filePath = Setup.dataFolder + "/Dead Players.data";
	
	
    public static void SaveFallen() {
		try {
			
			File tmpDir = new File(Setup.dataFolder + "/");
	    	if(!Files.exists(tmpDir.toPath()))
	    		tmpDir.mkdir();
			
		    ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath));
		    
		    output.writeObject(Revive.DeadPlayers);
		    output.close();
		} catch (IOException ex) {
		    ex.printStackTrace();
		} 
    }
    
    @SuppressWarnings("unchecked")
	public static void LoadFallen() {
        try {
        	ObjectInputStream input = null;
		    File tempFile = new File(filePath);
		    if(tempFile.exists()) {
    		    input = new ObjectInputStream(new FileInputStream (filePath));
    		    
    		    Revive.DeadPlayers.addAll((List<PlayerObj>)input.readObject());	
		    }
		    if(input != null)
		    	input.close();
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}    	            
    }
    
	
	
}
