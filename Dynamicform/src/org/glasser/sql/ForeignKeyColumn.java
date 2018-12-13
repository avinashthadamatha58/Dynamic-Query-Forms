
package org.glasser.sql;


import java.util.*;


/**
 * An instance of this class is used to hold the metadata for a table column that serves as
 * all or part of a foreign key.
 */
public class ForeignKeyColumn implements java.io.Serializable {


    private String localTableSchema = null;

    private String localTableName = null;

    private String localColumnName = null;

    private String foreignTableSchema = null;

    private String foreignTableName = null;

    private String foreignColumnName = null;

    private int keySeq = -1;

    private Column localColumn = null;

    
    /**
     * This is the schema (or table owner) for the table to which
     * this column belongs.
     */
    public void setLocalTableSchema(String localTableSchema) {
        this.localTableSchema = localTableSchema;
    }

    /**
     * This is from the column FKTABLE_NAME in the metadata ResultSet.
     */
    public void setLocalTableName(String localTableName) {
        this.localTableName = localTableName;
    }

    /**
     * This is from the column FKCOLUMN_NAME in the metadata ResultSet.
     */
    public void setLocalColumnName(String localColumnName) {
        this.localColumnName = localColumnName;
    }

    /**
     * This is the schema to which the foreign table belongs.
     */
    public void setForeignTableSchema(String foreignTableSchema) {
        this.foreignTableSchema = foreignTableSchema;
    }

    /**
     * This is from the column PKTABLE_NAME in the metadata ResultSet.
     */
    public void setForeignTableName(String foreignTableName) {
        this.foreignTableName = foreignTableName;
    }

    /**
     * This is from the column PKCOLUMN_NAME in the metadata ResultSet.
     */
    public void setForeignColumnName(String foreignColumnName) {
        this.foreignColumnName = foreignColumnName;
    }    

    public void setKeySeq(int keySeq) {
        this.keySeq = keySeq;
    }

    public void setLocalColumn(Column localColumn) {
        this.localColumn = localColumn;
    }

    public String getLocalTableSchema() {
        return localTableSchema;
    }

    public String getLocalTableName() {
        return localTableName;
    }

    public String getLocalColumnName() {
        return localColumnName;
    }

    public String getForeignTableSchema() {
        return foreignTableSchema;
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public String getForeignColumnName() {
        return foreignColumnName;
    }

    public int getKeySeq() {
        return keySeq;
    }

    public Column getLocalColumn() {
        return localColumn;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(150);
        buffer.append("ForeignKeyColumn[");
        buffer.append("localTableSchema=");
        buffer.append(localTableSchema);
        buffer.append(",localTableName=");
        buffer.append(localTableName);
        buffer.append(",localColumnName=");
        buffer.append(localColumnName);
        buffer.append(",foreignTableSchema=");
        buffer.append(foreignTableSchema);
        buffer.append(",foreignColumnName=");
        buffer.append(foreignColumnName);
        buffer.append(",keySeq=");
        buffer.append(keySeq);
        buffer.append(",localColumn=");
        buffer.append(localColumn);
        return buffer.toString();
    }

}
