package org.glasser.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/**
 * This program will search all jar and zip files in the current directory for
 * a given class file.
 * 
 * @author Dave Glasser
 */
public class JarSearcher {

    /**
     * Returns true if the jar or zip file at jarFilePath contains (with the internal
     * path) the file named by classFilePath. 
     */
    public static boolean searchJarFile(String jarFilePath, String classFilePath) {
        return searchJarFile( new File(jarFilePath), classFilePath);
    }

    public static boolean searchJarFile(File file, String classFilePath) {

        try {
            if(!file.exists()) return false;
    
            ZipFile jarFile = new ZipFile(file);
            if(jarFile.getEntry(classFilePath) != null) {
                jarFile.close();
                return true;
            }
            else {
                jarFile.close();
                return false;
            }
        }
        catch(IOException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }




  static class ArchiveFilter implements FileFilter {

    public boolean accept(File pathName) {
      String upcase = pathName.getName().toUpperCase();
      if(upcase.endsWith(".ZIP") || upcase.endsWith(".JAR")) return true;
      return false;
    }
  }


    public static void main (String[] args)  {
        if(args.length == 0) {
            System.out.println("usage: java ClassFinder <class name>\n\n"
                               + "example: java ClassFinder java.lang.String\n");
            System.exit(0);
        }

        File cwd = new File(".");
        File[] archives = cwd.listFiles(new ArchiveFilter());
    
        String classFileName = args[0].replace('.', '/');
        if(classFileName.endsWith(".class") == false) {
            classFileName += ".class";
        }
    
        System.out.println("Searching for " + classFileName + " ...");
        for(int j=0; j<archives.length; j++) {
    //      System.out.println("Searching " + archives[j].getName());
          if(searchJarFile(archives[j], classFileName)) {
            System.out.println("FOUND IN " + archives[j].getName());
          }
        }
    }
}
