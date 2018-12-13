package org.glasser.util;


import java.net.*;
import java.io.*;



/**
 * This is a URLClassLoader with some minor functionality added. It has
 * a Singleton instance which is globally accessible, and it also
 * has a method (addArchivesInDirectory(File directory) that adds all
 * of the jar and zip files in a given directory to the classpath for
 * an instance of this classloader.
 */
public class ExtensionClassLoader extends java.net.URLClassLoader {


    public static boolean debug = System.getProperty("ExtensionClassLoader.debug") != null;

    /**
     * This is not a Singleton class in the strict sense that only one instance
     * can be created, (the constructors are public) however there is one instance
     * that is globally accessible.
     */
    private final static ExtensionClassLoader singleton = new ExtensionClassLoader(new URL[0]);


    static class ArchiveFilter implements java.io.FileFilter {
    
        public boolean accept(File pathName) {
            String upcase = pathName.getName().toUpperCase();
            if(upcase.endsWith(".ZIP") || upcase.endsWith(".JAR")) return true;
            return false;
        }
    }

    private ArchiveFilter archiveFilter = new ArchiveFilter();


    public ExtensionClassLoader(URL[] urls) {
        super(urls);
    }


    public ExtensionClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


    public ExtensionClassLoader(URL[] urls, 
        ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    /**
     * This is not a Singleton instance in the strict sense that only one instance
     * can be created, (the constructors are public) however there is one instance
     * that is globally accessible.
     */
    public static ExtensionClassLoader getSingleton() {
        return singleton;
    }


    public void addArchivesInDirectory(File directory) 
        throws MalformedURLException
    {
        if(directory.isDirectory() == false) {
            throw new IllegalArgumentException("directory argument is not a directory.");
        }
        if(debug) System.out.println("Adding archives in " + directory + " to ExtensionClassLoader classpath." );

        File[] archives = directory.listFiles(archiveFilter);
        for(int j=0; archives != null && j<archives.length; j++) {
            if(debug) System.out.println("Adding " + archives[j] + " to ExtensionClassLoader classpath.");
            this.addURL(archives[j].toURL());
        }
    }

}
