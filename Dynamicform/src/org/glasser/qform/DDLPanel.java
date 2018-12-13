package org.glasser.qform;


import java.sql.*;
import org.glasser.sql.*;
import org.glasser.swing.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This panel displays the CREATE TABLE statement that can be used to create
 * a table, or a table based on the selected view.
 */
public class DDLPanel extends JPanel implements ActionListener {

	JTextArea textArea = new JTextArea();

	ConfigPanel configPanel = new ConfigPanel();

	TableInfo tableInfo = null;

	public DDLPanel(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
		setBorder(new EmptyBorder(10,10,10,10));
		setLayout(new BorderLayout());
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
		add(configPanel, BorderLayout.EAST);
		updateSQL();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
	}


	void updateSQL() {

		textArea.setText(buildSQL(tableInfo, 
								  true, 
								  '"', 
								  '"',
								  configPanel.getUpperCaseKeyword(),
								  configPanel.getTableNameCase(),
								  configPanel.getColumnCase(),
								  configPanel.getDataTypeCase()
								  ));

        textArea.setCaretPosition(0);
	}




	public void actionPerformed(ActionEvent e) {
		updateSQL();
	}


	static String ENDL = "\n"; //System.getProperty("line.separator");



	public static String buildSQL(TableInfo tableInfo, 
							boolean qualifyTableName,
							char openQuote, 
							char closeQuote,
							boolean keywordCase,
							int tableNameCase,
							int columnNameCase,
							int dataTypeCase) {

		StringBuffer buffer = new StringBuffer(1024);
		buffer.append(shift("CREATE TABLE ", keywordCase));
		if(qualifyTableName) {
			buffer.append(shift(tableInfo.getQualifiedTableName(openQuote,closeQuote), tableNameCase));
		}
		else {
			buffer.append(shift(tableInfo.maybeQuoteTableName(openQuote,closeQuote), tableNameCase));
		}
		buffer.append(" (").append(ENDL);

		Column[] columns = tableInfo.getColumns();
		ArrayList pks = new ArrayList();
		for(int j=0; columns != null && j<columns.length; j++) { 
			Column col = columns[j];
			boolean isPK = col.getPkComponent();
			if(isPK) pks.add(shift(col.maybeQuoteColumnName(openQuote,closeQuote), columnNameCase));
			buffer.append("    ");
			buffer.append(shift(col.maybeQuoteColumnName(openQuote,closeQuote), columnNameCase)).append(" ");
			String colType = shift( col.getTypeName(), dataTypeCase);
			buffer.append(colType);
			int sqlType = col.getDataType();
			if(DBUtil.isCharType(sqlType)) {
				buffer.append("(").append(col.getColumnSize()).append(")");
			}
			else if(DBUtil.isNumericType(sqlType)) {
				if(sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
					buffer.append("(").append(col.getColumnSize()).append(", ");
					buffer.append(col.getDecimalDigits()).append(")");
				}
			}


			if(col.getNullable() == false) {
				buffer.append(shift(" NOT NULL", keywordCase));
			}

			String def = col.getColumnDefaultValue();
			if(def != null && (def = def.trim()).length() > 0) {
				buffer.append(shift(" DEFAULT ", keywordCase));
				if(DBUtil.isCharType(sqlType)) {
					buffer.append("'");
					buffer.append(DBUtil.escape(def));
					buffer.append("'");
				}
				else {
					buffer.append(def);
				}
			}

			if(j == columns.length - 1) { // last column
				if(pks.size() > 0) {
					buffer.append(",");
				}
			}
			else {
				buffer.append(",");
			}

			buffer.append(ENDL);
		}

		if(pks.size() > 0) {
			buffer.append(shift("    PRIMARY KEY (", keywordCase));
			for(int j=0; j<pks.size(); j++) { 
				if(j > 0) {
					buffer.append(", ");
				}
				buffer.append(pks.get(j));
			}
			buffer.append(")").append(ENDL);
		}

		buffer.append(");");

		return buffer.toString();
	}


	static String shift(String s, boolean b) {
		if(s == null) {
			return s;
		}
		else if(b) {
			return s.toUpperCase();
		}
		else {
			return s.toLowerCase();
		}
	}


	static String shift(String s, int n) {
		if(s == null || n == 0) {
			return s;
		}
		else if(n > 0) {
			return s.toUpperCase();
		}
		else {
			return s.toLowerCase();
		}
	}

    class ConfigPanel extends JPanel {
    
    	String[] caseChoices = {"Preserve", "Force to lower", "Force to UPPER"};
    	int[] caseStrategies = {0, -1, 1};
    
    	JComboBox cmbKeywordCase = new JComboBox(new String[] {"UPPER", "lower"});
    	JComboBox cmbColumnCase = new JComboBox(caseChoices);
    	JComboBox cmbTableNameCase = new JComboBox(caseChoices);
    	JComboBox cmbDataTypeCase = new JComboBox(caseChoices);
    
    	boolean getUpperCaseKeyword() {
    		return cmbKeywordCase.getSelectedIndex() == 0;
    	}
    
    	int getColumnCase() {
    		return caseStrategies[cmbColumnCase.getSelectedIndex()];
    	}
    
    	int getTableNameCase() {
    		return caseStrategies[cmbTableNameCase.getSelectedIndex()];
    	}
    
    	int getDataTypeCase() {
    		return caseStrategies[cmbDataTypeCase.getSelectedIndex()];
    	}
    
    
    	Object[][] config = 
    	{
    		 {cmbKeywordCase, "Keyword case", "The case (upper or lower) to be used for SQL keywords.", "KEYWORD_CASE"}
    		,{cmbTableNameCase, "Table name case", "The case to be used for table names.", "TABLE_NAME_CASE"}
    		,{cmbColumnCase, "Column name case", "The case to be used for column names.", "COLUMN_NAME_CASE"}
    		,{cmbDataTypeCase, "Data type case", "The case to be used for data type names.", "DATA_TYPE_CASE"}
    		
    	};
    	
    
    	public ConfigPanel() {
    		setBorder(new EmptyBorder(0, 10, 0, 0));
    		GUIHelper.buildFormPanel(this, config, 50, 5, null, null, false, -1);
    
    		for(int j=0; j<config.length; j++) { 
    			JComboBox cmb =(JComboBox) config[j][0];
    			cmb.setActionCommand((String) config[j][3]);
    			cmb.addActionListener(DDLPanel.this);
    		}
    	}
    
    	public String toString() {
    		return "keyword=" + this.cmbKeywordCase.getSelectedItem()
    			+ ", column=" + this.cmbColumnCase.getSelectedItem()
    			+ ", tableName=" + this.cmbTableNameCase.getSelectedItem();
    	}
    
    }


}



