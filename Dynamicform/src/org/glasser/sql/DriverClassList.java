
package org.glasser.sql;


import java.sql.*;
import java.util.*;
import org.glasser.util.ExtensionClassLoader;


public class DriverClassList {


    /**
     * This is a list of driver class names that were collected through a google 
     * search for "Class.forName" and "driver".
     */
    private final static String[] driverList =
    {
         "acs.jdbc.Driver"
        ,"com.experlog.universe.Driver"
        ,"com.ibm.db2.jcc.DB2Driver"
        ,"COM.ibm.db2.jdbc.app.DB2Driver"
        ,"com.informix.jdbc.IfxDriver"
        ,"com.intellidimension.jdbc.Driver"
        ,"com.microsoft.jdbc.sqlserver.SQLServerDriver"
        ,"com.mysql.jdbc.Driver"
        ,"com.newatlanta.jturbo.driver.Driver"
        ,"com.novell.sql.LDAPDriver"
        ,"com.sap.dbtech.jdbc.DriverSapDB"
        ,"com.sybase.jdbc.SybDriver"
        ,"gwe.sql.gweMysqlDriver"
        ,"ids.sql.IDSDriver"
        ,"in.co.daffodil.db.jdbc.DaffodilDBDriver"
        ,"in.co.daffodil.db.rmi.RmiDaffodilDBDriver"
        ,"interbase.interclient.Driver"
        ,"jdbc.sqlmx.SQLmxDriver"
        ,"oracle.jdbc.driver.OracleDriver"
        ,"org.enhydra.instantdb.jdbc.idbDriver"
        ,"org.gjt.mm.mysql.Driver"
        ,"org.postgresql.Driver"
        ,"postgres95.pgDriver"
        ,"postgresql.Driver"
        ,"sun.jdbc.odbc.JdbcOdbcDriver"
        ,"weblogic.jdbc.jts.Driver"
        ,"org.hsqldb.jdbcDriver"
        ,"org.firebirdsql.jdbc.FBDriver"
    };


    private static String[] availableDrivers = null;


    public static String[] getDriverClassNames() {
        return (String[]) driverList.clone();
    }


    public static String[] getAvailableDriverClassNames() {

        ClassLoader extensionLoader = ExtensionClassLoader.getSingleton();
        
        if(availableDrivers == null) {
            ArrayList foundDriverClasses = new ArrayList();
            for(int j=0; j<driverList.length; j++) {
                try {
                    extensionLoader.loadClass(driverList[j]);
                    foundDriverClasses.add(driverList[j]);
                }
                catch(ClassNotFoundException ex) {
                    continue;
                }
            }

            availableDrivers = (String[]) foundDriverClasses.toArray(new String[foundDriverClasses.size()]);
        }

        return (String[]) availableDrivers.clone();

    }


    public static void main(String[] args) throws Exception {
        String[] availableDrivers = getAvailableDriverClassNames();
        for(int j=0; j<availableDrivers.length; j++) {
            System.out.println("--" + availableDrivers[j]);
        }
    } 

    private final static String[][] driverClassToEditableTypeMappings = 
    {
        {"oracle.jdbc.driver.OracleDriver", "1111"}
    };

    private final static HashMap driverClassToEditableTypeMap = new HashMap();

    static {

        for(int j=0; j<driverClassToEditableTypeMappings.length; j++) {
            String[] row = driverClassToEditableTypeMappings[j];
            String types = row[1];
            StringTokenizer st = new StringTokenizer(types);
            HashSet setForDriverClass = new HashSet();
            while(st.hasMoreTokens()) {
                setForDriverClass.add(new Integer(st.nextToken()));
            }
            driverClassToEditableTypeMap.put(row[0], setForDriverClass);
        }
    }

    public static Set getEditableTypes(String driverClassName) {
        return (Set) driverClassToEditableTypeMap.get(driverClassName);
    }

}
