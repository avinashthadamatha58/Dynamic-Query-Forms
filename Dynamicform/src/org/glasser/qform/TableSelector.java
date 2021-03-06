package org.glasser.qform;


import javax.swing.*;
import java.util.*;
import javax.swing.border.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

import org.glasser.swing.*;
import java.sql.*;
import javax.sql.*;
import org.glasser.sql.*;

public class TableSelector extends JDialog implements ActionListener {


    private static boolean debug = false;


    private class DataSourceListItem  implements Comparable {

        private String displayName = null;

        private Integer sourceId = null;

        public DataSourceListItem(Integer sourceId, String displayName) {
            this.sourceId = sourceId;
            this.displayName = "(" + sourceId + ") " + displayName;
        }

        public String toString() {
            return displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Integer getSourceId() {
            return sourceId;
        }

        public boolean equals(Object other) {
            try {
                return((DataSourceListItem) other).getSourceId().equals(sourceId);
            } catch(Exception ex) {
                return this == other;
            }
        }

        public int hashCode() {
            return sourceId.intValue();
        }

        public int compareTo(Object other) {
            try {
                return sourceId.compareTo(((DataSourceListItem) other).getSourceId());
            } catch(Exception ex) {
                return 0;
            }
        }
    }



    protected JComboBox sourceList = new JComboBox();

    protected Vector sourceVector = new Vector();

    protected DefaultComboBoxModel sourceModel = new DefaultComboBoxModel(sourceVector);

    JComboBox schemaList = new JComboBox();

    private ComboBoxModel emptySchemaListModel = schemaList.getModel();

    private Vector emptyVector = new Vector();

    JList tableList = new KeySelectableJList();

    JButton btnOK = new JButton("OK");

    JButton btnCancel = new JButton("Cancel");

//    HashMap tableMap = new HashMap();
//
//    HashMap schemaListMap = new HashMap();

    HashMap schemaModelMap = new HashMap();

    HashMap tableListMap = new HashMap();

    private Object[] selections = null;

    private TableInfo selectedTableInfo = null;

    private Integer selectedSourceId = null;

    


    public void addDataSource(Integer sourceId, String sourceName, HashMap tables) 
    {

        DataSourceListItem item = new DataSourceListItem(sourceId, sourceName);
        sourceVector.add(item);
        Collections.sort(sourceVector);
        sourceList.setModel(sourceModel);

        Vector schemas = new Vector();
        for(Iterator i = tables.keySet().iterator(); i.hasNext(); ) {
            schemas.add(i.next());
        }

        Collections.sort(schemas);

        schemaModelMap.put(sourceId, new DefaultComboBoxModel(schemas));


        tableListMap.put(sourceId, tables);
        sourceList.setSelectedItem(item);

    }

    public void removeDataSource(Integer sourceId) {

        for(int j=sourceVector.size()-1; j>=0; j--) {
            DataSourceListItem li = (DataSourceListItem) sourceVector.get(j);
            if(sourceId.equals(li.getSourceId())) {
                if(debug) System.out.println("GOT IT.");
                sourceVector.remove(j);
            }
        }

        if(debug) System.out.println("new sourceModel size is " + sourceModel.getSize());

        Object o = schemaModelMap.remove(sourceId);
        if(debug) System.out.println("schemaModel removed: " + o);
        o = tableListMap.remove(sourceId);
        if(debug) System.out.println("tableList removed : " + (o != null));

        sourceList.setModel(sourceModel);
        try {
            sourceList.setSelectedIndex(-1);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        schemaList.setModel(this.emptySchemaListModel);
        tableList.setListData(emptyVector);
        if(sourceList.getSelectedIndex() < 0 
           && sourceModel.getSize() > 0) {
            sourceList.setSelectedIndex(0);
        }
    }


    public TableSelector() {
        this(null);
    }


    public TableSelector(Frame parent) {

        super(parent);


        setTitle("Select a table");

        sourceList.setModel(sourceModel);



        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(new JLabel("Data Source"), BorderLayout.NORTH);
        p1.add(sourceList, BorderLayout.CENTER);


        JPanel panel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        topPanel.setBorder(new EmptyBorder(10,10,10, 10));
        centerPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        panel.setLayout(new BorderLayout());
        topPanel.setLayout(new BorderLayout());
        centerPanel.setLayout(new BorderLayout());

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(new JLabel("Table Owner"), BorderLayout.NORTH);
        p2.add(schemaList, BorderLayout.CENTER);
        p2.setBorder(new EmptyBorder(10, 0, 0, 0));

        topPanel.add(p1, BorderLayout.NORTH);
        topPanel.add(p2, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(tableList), BorderLayout.CENTER);
        centerPanel.add(new JLabel("Tables/Views (Views in ITALICS)"), BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        GUIHelper.enterPressesWhenFocused(btnOK);
        GUIHelper.enterPressesWhenFocused(btnCancel);

        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        btnOK.setPreferredSize(btnCancel.getPreferredSize());
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        this.addKeyListener(new EnterEscapeKeyHandler(btnOK, btnCancel));

        sourceList.addActionListener(this);
        schemaList.addActionListener(this);
        tableList.addMouseListener(new MouseAdapter() {
                                       public void mouseClicked(MouseEvent e) {
                                           if(e.getClickCount() == 2) {
                                               setSelections();
                                               if(selections != null) {
                                                   setVisible(false);
                                               }
                                           }
                                       }
                                   });
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableList.setCellRenderer(new TableInfoListCellRenderer());

        setModal(true);
        this.addComponentListener(new ComponentAdapter() {
                                      public void componentShown(ComponentEvent e) {
                                          tableList.requestFocus();
                                          if(tableList.getSelectedIndex() < 0 && tableList.getModel().getSize() > 0) {
                                              try {
                                                  tableList.setSelectedIndex(0);
                                              } catch(Exception ex) {
                                              }
                                          }
                                      }

                                      public void componentResized(ComponentEvent e) {
                                          int index = tableList.getSelectedIndex();
                                          if(index > -1) {
                                              tableList.ensureIndexIsVisible(index);
                                          }
                                      }
                                  });


        // make the OK button close the default so ENTER invokes it
        this.getRootPane().setDefaultButton(this.btnOK);

        // make the Escape key click the cancel button.
        KeyStroke esc = KeyStroke.getKeyStroke("ESCAPE");
        Action closer = new ButtonClicker(btnCancel);
        panel.getInputMap(panel.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(esc, "_ESCAPE_");
        panel.getInputMap(panel.WHEN_IN_FOCUSED_WINDOW).put(esc, "_ESCAPE_");
        panel.getActionMap().put("_ESCAPE_", closer);
        
        

        setContentPane(panel);
        pack();


    }


//    protected void handleSchemaChange

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if(source == sourceList) {

            DataSourceListItem sourceItem = (DataSourceListItem) sourceList.getSelectedItem();
            if(sourceItem == null) return;
            Integer sourceId = sourceItem.getSourceId();
            DefaultComboBoxModel schemaModel = (DefaultComboBoxModel) schemaModelMap.get(sourceId);
            schemaList.setModel(schemaModel);
            try {
                schemaList.setSelectedIndex(0);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
            


        if(source == schemaList) {
            DataSourceListItem sourceItem = (DataSourceListItem) sourceList.getSelectedItem();
            if(sourceItem == null) return; // all lists are empty.
            Integer sourceId = sourceItem.getSourceId();
            String owner = (String) schemaList.getSelectedItem(); 
            HashMap schemaTables = (HashMap) tableListMap.get(sourceId);

            if(owner != null) {
                Vector v = (Vector) schemaTables.get(owner);
                tableList.setListData(v);
                tableList.requestFocus();
                try {
                    if(v.size() > 0) {
                        tableList.setSelectedIndex(0);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        } 
        else if(source == btnCancel) {
            selections = null;
            setVisible(false);
        } 
        else if(source == btnOK) {
            setSelections();
            setVisible(false);
        }

    }

    /**
     * If a selection was made, returns an Object[3] array. The first element
     * is an Integer representing the DataSource id, the second element is
     * a String representing the schema, or table owner, and the third
     * element is the name of the table.
     */
    protected void setSelections() {

        selections = null;
        DataSourceListItem source = (DataSourceListItem) sourceList.getSelectedItem();
        if(source == null) return;

        selectedTableInfo = (TableInfo) tableList.getSelectedValue();
        selectedSourceId = (Integer) source.getSourceId();
        if(selectedTableInfo == null) {
            return;
        }
            

        selections = new Object[3];
        selections[0] = source.getSourceId();
        selections[1] = schemaList.getSelectedItem();
        selections[2] = selectedTableInfo;

    }



    public void setVisible(boolean b) {
        if(b) {
            selections = null;
            selectedTableInfo = null;
            selectedSourceId = null;
        }
        super.setVisible(b);
    }

    public Object[] getSelection() {
        return selections;
    }

    public TableInfo getSelectedTableInfo() {
        return selectedTableInfo;
    }

    public Integer getSelectedSourceId() {
        return selectedSourceId;
    }


    public static void main(String[] args) throws Exception {
        TableSelector ts = new TableSelector();
//        ts.setDefaultCloseOperation(ts.EXIT_ON_CLOSE);
        Config config = new Config(new java.io.File("C:/0/qform.xml"));
        LocalDataSourceConfig[] lds = config.getLocalDataSourceConfigs();
        for(int j=0; j<lds.length; j++) {
            try {
                DataSource ds = DataSourceManager.getLocalDataSource(lds[j], null, null);
                Connection c = ds.getConnection();
                DatabaseMetaData dbmd = c.getMetaData();
                ResultSet rs = dbmd.getTables(null, null, "%", new String[] {"VIEW", "TABLE"});
                TableInfo[] tis = DBUtil.getTableInfos(rs);
                rs.close();
                c.close();
                HashMap map = DBUtil.getTableInfoLists(tis, "<DEFAULT SCHEMA>");
                ts.addDataSource(new Integer(j+1), lds[j].getDisplayName(), map);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        ts.setModal(true);
        ts.setVisible(true);

        Object[] sels = ts.getSelection();
        if(debug) {
            for(int j=0; sels != null && j<sels.length; j++) {
                System.out.println("---" + sels[j]);
            }
        }

        ts.removeDataSource(new Integer(1));
        ts.setVisible(true);

        sels = ts.getSelection();
        if(debug) {
            for(int j=0; sels != null && j<sels.length; j++) {
                System.out.println("---" + sels[j]);
            }
        }


        ts.removeDataSource(new Integer(2));
        ts.setVisible(true);

        sels = ts.getSelection();
        if(debug) {
            for(int j=0; sels != null && j<sels.length; j++) {
                System.out.println("---" + sels[j]);
            }
        }

    } 
    
}
