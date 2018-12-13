
package org.glasser.sql;

import java.util.*;
import org.glasser.util.*;


public class ForeignKey implements java.io.Serializable {

    private String foreignKeyName = null;

    /**
     * This is the name of the schema (or table owner) to which
     * the table containing this foreign key belongs.
     */
    private String localTableSchema = null;


    /**
     * This is the name of the table to which this foreign key belongs.
     */
    private String localTableName = null;


    /**
     * This is the name of the schema (or table owner) to which the foreign
     * table (the table that is referenced by this foreign key) belongs.
     */
    private String foreignTableSchema = null;

    /**
     * This is the name of the table that is referenced by this foreign key.
     */
    private String foreignTableName = null;

    /**
     * This is a Vector that is used to hold the ForeignKeyColumns that participate
     * in this ForeignKey.
     */
    private Vector foreignKeyColumns = new Vector();

    public ForeignKey(String foreignTableName) {
        if(Util.isNothing(foreignTableName))
            throw new IllegalArgumentException("foreignTableName argument cannot be null.");
        this.foreignTableName = foreignTableName.trim();
    }


    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    /**
     * This is the name of the schema (or table owner) to which
     * the table containing this foreign key belongs.
     */
    public void setLocalTableSchema(String localTableSchema) {
        this.localTableSchema = localTableSchema;
    }


    /**
     * This is the name of the table to which this foreign key belongs.
     */
    public void setLocalTableName(String localTableName) {
        this.localTableName = localTableName;
    }


    /**
     * This is the name of the schema (or table owner) to which the foreign
     * table (the table that is referenced by this foreign key) belongs.
     */
    public void setForeignTableSchema(String foreignTableSchema) {
        this.foreignTableSchema = foreignTableSchema;
    }

    /**
     * This is the name of the table that is referenced by this foreign key.
     */
    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    public void setForeignKeyColumns(Vector foreignKeyColumns) {
        this.foreignKeyColumns = (Vector) foreignKeyColumns.clone();
    }

    public void addForeignKeyColumn(ForeignKeyColumn col) {
        foreignKeyColumns.add(col);
    }

    public String getForeignKeyName() {
        return foreignKeyName;
    }

    /**
     * This is the name of the schema (or table owner) to which
     * the table containing this foreign key belongs.
     */
    public String getLocalTableSchema() {
        return localTableSchema;
    }


    /**
     * This is the name of the table to which this foreign key belongs.
     */
    public String getLocalTableName() {
        return localTableName;
    }


    /**
     * This is the name of the schema (or table owner) to which the foreign
     * table (the table that is referenced by this foreign key) belongs.
     */
    public String getForeignTableSchema() {
        return foreignTableSchema;
    }

    /**
     * This is the name of the table that is referenced by this foreign key.
     */
    public String getForeignTableName() {
        return foreignTableName;
    }
    

    public Vector getForeignKeyColumns() {
        return (Vector) foreignKeyColumns.clone();
    }


    public String toString() {
        StringBuffer buffer = new StringBuffer(200);
        buffer.append("ForeignKeyColumn[");
        buffer.append("localTableSchema=");
        buffer.append(localTableSchema);
        buffer.append(",localTablename=");
        buffer.append(localTableName);
        buffer.append(",foreignTableSchema=");
        buffer.append(foreignTableSchema);
        buffer.append(",foreignTableName=");
        buffer.append(foreignTableName);
        buffer.append(",foreignKeyColumns.size()=");
        buffer.append(foreignKeyColumns);
        buffer.append("]");
        return buffer.toString();
    }

}
