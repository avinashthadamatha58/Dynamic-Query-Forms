package org.glasser.qform;


/**
 * This exception is thrown inside QueryForm when a table's primary key is unknown 
 * (it was not reported in the metadata, which is the case with Access / ODBC ) and
 * could not be "guessed" heuristically, and an operation is attempted which required
 * knowledge of the primary key (update, delete, etc.)
 */
public class UnknownPrimaryKeyException extends Exception {
}
