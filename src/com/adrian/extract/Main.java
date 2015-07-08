package com.adrian.extract;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;
import java.io.*;

public class Main {
	public static boolean delete;
	public static HashMap<String, String> arguments = new HashMap<String, String>();
	public static String[] validArgs = new String[]{"-delete", "-type"};
    public static void main(String[] args) {
        System.out.println(setValidArguments(args));
        /*
        if (option.equalsIgnoreCase("same") || option.equalsIgnoreCase("samedirec") || option.equalsIgnoreCase("root") 
        || option.equalsIgnoreCase("rootdirec") ){
            recursiveFind(new File(args[0]), option, new File(args[0]));
        } else {
            System.out.println("Option not valid");
        }
        */
    }
    /*
    options are
        null - equals as same
        same - files extracto the same directory as which they come from
        samedirec - files extract to the same directory as which they come, but into it's own folder
        root - all files extract to the root
        rootdirec - all files extract to the root, but into it's own folder
    */
    public static boolean getDelete(String[] args){
        for (int a=0; a<args.length; a++) {
            if(args[a].equals("delete")){
                return true;
            }
        }
        return false;
    }
    public static String getOption(String[] args){
        for (int a=0; a<args.length; a++) {
            if(args[a].equals("same")){
                return "same";
            }
            if(args[a].equals("samedirec")){
                return "samedirec";
            }
            if(args[a].equals("root")){
                return "root";
            }
            if(args[a].equals("rootdirec")){
                return "rootdirec";
            }
        }
        return "same";
    }
    public static void recursiveFind(File directory, String option, File root){
        File[] files = directory.listFiles();
        for(int a=0; a<files.length; a++){
            if(files[a].isDirectory()){
                recursiveFind(files[a], option, root);
            } else {
                if (option.equalsIgnoreCase("same") || option.equalsIgnoreCase("samedirec") ) {
                    extract(files[a], option, directory);
                } else if(option.equalsIgnoreCase("root") || option.equalsIgnoreCase("rootdirec") ){
                    extract(files[a], option, root);
                }
                if(delete){
                    files[a].delete();
                }
            }
        }
    }
    public static void extract(File file, String option, File root){
        if(file.getName().endsWith(".zip")){
            extractZipFile(file, option, root);
        }
    }
    public static void extractZipFile(File file, String option, File root){
        byte[] buffer = new byte[1024];
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
