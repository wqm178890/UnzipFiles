package com.adrian.extract;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;
import java.io.*;

public class Main {
	public static HashMap<String, String> arguments = new HashMap<String, String>();
	public static String[] validArgs = new String[]{"-delete", "-to"};
    public static void main(String[] args) {
    	File root = new File(System.getProperty("user.dir"));
    	
        if (setValidArguments(args)) {
			recursiveFileFind(root, root);
		}
    }
    public static void recursiveFileFind(File directory, File root){
        File[] files = directory.listFiles();
        for(int a=0; a<files.length; a++){
            if(files[a].isDirectory()){
                recursiveFileFind(files[a], root);
            } else {
                if (arguments.containsKey("-to") && arguments.get("-to").equals("same")) {
                    extract(files[a], directory);
                } else if(arguments.containsKey("-to") && arguments.get("-to").equals("root")){
                    extract(files[a], root);
                }
                
                if(arguments.containsKey("-delete") && arguments.get("-delete").equals("true")){
                    files[a].delete();
                }
            }
        }
    }
    public static void extract(File file, File root){
    	if(file.getName().endsWith(".zip")){
            extractZipFile(file, root);
        }
    }
    public static void extractZipFile(File file, File root){
        byte[] buffer = new byte[1024];
        System.out.println(file.getAbsolutePath());
        try{
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null){
                String fileName = entry.getName();
                if(!fileName.contains("__MACOSX")){

                    File newFile = new File(root.getAbsolutePath() + File.separator + fileName);
                    if(entry.isDirectory()){
                        newFile.mkdirs();
                    } else {
                        newFile.getParentFile().mkdirs();
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                }
            }
            zis.closeEntry();
            zis.close();
        } catch(IOException ex){
            System.out.println(ex);
        }
    }
    public static boolean setValidArguments(String[] args) {
		List<String> argsList = Arrays.asList(validArgs);
		String key, value;
		if (args.length % 2 != 0) {
			System.out.println("Every argument should be followed by a value ex: '-arg value'");
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			key = args[i];
			value = args[++i];
			if (argsList.contains(key)) {
				arguments.put(key, value);
			} else {
				System.out.println(key + " is not a valid argument");
				System.out.println("Valid arguments are " + Arrays.toString(validArgs));
				return false;
			}
		}
		return true;
	}
}
