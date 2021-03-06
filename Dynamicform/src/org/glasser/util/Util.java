package org.glasser.util;

import java.util.*;


/**
 * This class provides miscellaneous trivial utility methods.
 */
public class Util {


    /**
     * Returns true if the given object is null, or if it's toString() method
     * returns all whitespace.
     */
    public static boolean isNothing(Object object) {
        return object == null 
            || object.toString().trim().length() == 0;
    }
    
    /**
     * Trims a String without the need to null-check first. If s is null,
     * null is returned, otherwise s.trim() is returned.
     */
    public static String trim(String s) {
        if(s == null) return null;
        return s.trim();
    }

    /**
     * If s is null or all whitespace, null is returned, otherwise
     * s.trim() is returned.
     */
    public static String trimToNull(String s) {
        if(s == null || (s = s.trim()).length() == 0) return null;
        return s;
    }


    /**
     * If s is null or all whitespace, a zero-length String ("") is returned, 
     * otherwise s.trim() is returned.
     */
    public static String trimToString(String s) {
        if(s == null || (s = s.trim()).length() == 0) return "";
        return s;
    }


    private static Class[][] primitiveToWrapperClassMappings =
    {
         {boolean.class,     java.lang.Boolean.class }
        ,{int.class,        java.lang.Integer.class}
        ,{float.class,      java.lang.Float.class}
        ,{double.class,     java.lang.Double.class}
        ,{long.class,       java.lang.Long.class}
        ,{char.class,       java.lang.Character.class}
        ,{short.class,      java.lang.Short.class}
        ,{byte.class,       java.lang.Byte.class}
        ,{void.class,       java.lang.Void.class}

    };

    private static HashMap primitiveToWrapperClassMap = new HashMap();

    static {

        for(int j=0; j<primitiveToWrapperClassMappings.length; j++) {
            primitiveToWrapperClassMap.put(primitiveToWrapperClassMappings[j][0], primitiveToWrapperClassMappings[j][1]);
        }

    }


    /**
     * Given the Class for a primitive type, such as int.class or byte.class,
     * this method will return the corresponding wrapper class, such
     * as Integer.class or Byte.class.
     */
    public static Class getWrapperClass(Class primitiveClass) {
        return (Class) primitiveToWrapperClassMap.get(primitiveClass);
    }


    /**
     * This determines if the currently executing Java version (read from the 
     * System property "java.version" is equal to or later than the version
     * string passed in.
     * 
     * @param minVersion a String representing a Java version, for example "1.3.1". The 
     * String may only contain digits and dots.
     */
    public static boolean isCurrentJavaVersionAtLeast(String minVersion) {

        String version = System.getProperty("java.version");
        ArrayList list = new ArrayList();
        StringTokenizer versionTokenizer = new StringTokenizer(version, ".");
        StringTokenizer minVersionTokenizer = new StringTokenizer(minVersion, ".");

        while(minVersionTokenizer.hasMoreElements()) {

            // if the current version has fewer segments than the minimum
            // version, we'll right-pad it with zeros.
            int ver = 0;
            if(versionTokenizer.hasMoreElements()) { 
                ver = Integer.parseInt( versionTokenizer.nextToken() );
            }

            int min = Integer.parseInt( minVersionTokenizer.nextToken());

            // each segment of the current version must be at least as 
            // much as the corresponding segment of the minimum version.
            if(ver < min) {
                return false;
            }
        }

        return true;

    }


    /**
     * Given a string which may already contain linebreak characters, this method
     * will separate it into its individual lines, and then pass each line to
     * breakLine with the lineLength argument.
     */
    public static String wrapLines(String lines, int lineLength) {

        if(lines == null || lines.length() <= lineLength) return lines;
        StringTokenizer st = new StringTokenizer(lines, "\n", true);
        int j=0;
        ArrayList list = new ArrayList();
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            j++;
            if(j % 2 != 0) { // this should be text
                if(s.equals("\n")) { // blank line
                    list.add("");
                    j++;
                }
                else {
                    list.add(breakLine(s, lineLength));
                }
            }
        }

        StringBuffer buffer = new StringBuffer(lines.length() + list.size() + 10);
        for(int k=0; k<list.size(); k++) {
            buffer.append(list.get(k));
            buffer.append("\n");
        }

        return buffer.toString();

    }

    /**
     * Breaks the given line into segments, separated by newline ('\n')
     * characters. Each segment is at least "segLength" characters long.
     * The break occurs at the first space character after seglength.
     */
    public static String breakLine(String line, int segLength) {
        if(line == null || line.length() < segLength) return line;
        int breakpoint = line.indexOf(" ", segLength);
        if(breakpoint == -1) return line; // can't break it.
        String firstSeg = line.substring(0, breakpoint);
        // make sure the second segment isn't all whitespace
        String check = line.substring(breakpoint);

        // if the second segment is whitespace, discard it and return the first
        // segment.
        if(check.trim().length() == 0) return firstSeg;

        // otherwise, insert the '\n' and recursively call breakLine
        // on the second segment. Return the result. 
        StringBuffer buffer = new StringBuffer(line.length() + 20);
        buffer.append(firstSeg);
        buffer.append("\n");
        buffer.append(breakLine(line.substring(breakpoint + 1), segLength));
        return buffer.toString();
    }


    public static void addMappings(Map map, Object[][] mappings) {
        for(int j=0; j<mappings.length; j++) {
            map.put(mappings[j][0], mappings[j][1]);
        }
    }

    public static HashMap buildMap(Object[][] mappings) {
        HashMap map = new HashMap(mappings.length + 5);
        addMappings(map, mappings);
        return map;
    }


}
