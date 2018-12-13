package org.glasser.qform; 

/**
 * This class stores the version number for the current release of QueryForm.
 */
public class VersionNumber implements java.io.Serializable {

    public final static String VERSION_NUMBER;

    static {
        // initializing VERSION_NUMBER here insures that the class will always be
        // loaded and VERSION_NUMBER will be dynamically, rather than statically 
        // linked. (Hopefully.)
        VERSION_NUMBER = "1.4";
        System.setProperty("qform.version", VERSION_NUMBER);
    }
}





