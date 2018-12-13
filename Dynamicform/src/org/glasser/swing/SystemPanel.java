package org.glasser.swing;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import org.glasser.swing.*;
import org.glasser.swing.table.*;
import org.glasser.util.*;


/**
 * This is a panel that displays the system properties of a
 * running app, along with the memory usage and the program startup time
 * (if a Date instance is passed to the constructor.)
 * 
 * @author Dave Glasser
 */
public class SystemPanel extends JPanel {


    JTable propsTable = new JTable();

    PushButtonTableHeader tableHeader = new PushButtonTableHeader();


    final ListTableModel model = new ListTableModel(new ArrayColumnManager(new String[] {"System Property", "Value"}, 
                                                                           new Class[] {String.class, String.class}),
                                                    null);

    Date runningSince = null;

    JLabel lblRunningSince = new JLabel();
    JLabel lblTotalMem = new JLabel();
    JLabel lblFreeMem = new JLabel();

    NumberFormat bytesFormatter = NumberFormat.getInstance();

    Object[][] formConfig =
    {
         
         {lblTotalMem, "Total program memory (bytes): "}
        ,{lblFreeMem, "Free program memory (bytes): "}
        ,{lblRunningSince, "Up since: "}
    };

    Object[][] formConfig2 =
    {
         {"Total program memory (bytes): ", lblTotalMem}
        ,{"Free program memory (bytes): ", lblFreeMem}
    };


    /**
     * This is the Thread that updates the memory usage stats every half-second.
     */
    private Thread memThread =
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        sleep(2000);
                    }       
                    catch(InterruptedException ex) {}
                    if(isVisible() == false) continue;
                    lblTotalMem.setText(bytesFormatter.format(Runtime.getRuntime().totalMemory()));
                    lblFreeMem.setText(bytesFormatter.format(Runtime.getRuntime().freeMemory()));
                }
            }
        };



    public SystemPanel() {
        this(null);
    }


    /**
     * If a Date is passed to this constructor, it will be displayed
     * on the panel with the label "Up since:". Otherwise it won't be.
     * 
     * @param runningSince a java.util.Date that is used to indicate when the app
     * was started.
     * 
     */
    public SystemPanel(Date runningSince) {

        this.runningSince = runningSince;

        setLayout(new BorderLayout());

        // top subpanel
        JLabel label = new JLabel("System Properties");
        label.setBorder(new EmptyBorder(0, 0, 5, 10));
        add(label, BorderLayout.NORTH);

        // system properties table.
        propsTable.setModel(model);

        refreshSystemPropertiesTable();

        // make the table "push and sort".
        propsTable.setTableHeader(tableHeader);

        // make the sorts case-insensitive
        model.setColumnComparator(0, String.CASE_INSENSITIVE_ORDER);
        model.setColumnComparator(1, String.CASE_INSENSITIVE_ORDER);

        // this code is needed for Java 1.2 and earlier.
        TableColumnModel tcm = propsTable.getColumnModel();
        for(int j=0; j<tcm.getColumnCount(); j++) {
            TableColumn tc = tcm.getColumn(j);
            tc.setHeaderRenderer(tableHeader.getDefaultRenderer());
        }

        tableHeader.addMouseListener(new ListTableModelSorter());

        add(new JScrollPane(propsTable), BorderLayout.CENTER);

        // bottom panel
        if(runningSince != null) {
            lblRunningSince.setText(runningSince.toString());
        }
        else {
            formConfig = formConfig2;
        }

        lblTotalMem.setText(bytesFormatter.format(Runtime.getRuntime().totalMemory()));
        lblFreeMem.setText(bytesFormatter.format(Runtime.getRuntime().freeMemory()));

        lblRunningSince.setHorizontalAlignment(JLabel.RIGHT);
        lblTotalMem.setHorizontalAlignment(JLabel.RIGHT);
        lblFreeMem.setHorizontalAlignment(JLabel.RIGHT);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        bottomPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gc = new GridBagConstraints();

        Font smallFont = new Font("SansSerif", Font.PLAIN,11);
        
        for(int j=0; j<formConfig.length; j++) {
            Object[] row = formConfig[j];
            gc.weightx = 0;
            gc.gridx = 0;
            gc.gridy = j;
            gc.fill = gc.BOTH;
            gc.ipadx = 5;
            bottomPanel.add(new JLabel((String) row[1]), gc);
            gc.gridx = 1;
            ((JComponent) row[0]).setFont(smallFont);
            bottomPanel.add((JComponent) row[0], gc);
            gc.gridx = 2;
            gc.weightx = 1;
            bottomPanel.add(new JLabel(), gc);
        }


        add(bottomPanel, BorderLayout.SOUTH);

        memThread.start();


    }

    public void refreshSystemPropertiesTable() {
        Properties props = System.getProperties();

        ArrayList list = new ArrayList(props.size());

        for(Enumeration enumer = props.propertyNames(); enumer.hasMoreElements(); ) {
            String name = (String) enumer.nextElement();
            String val = props.getProperty(name);
            if(name.equals("line.separator")) {
                if(val != null) {
                    if(val.equals("\r\n")) {
                        val = "\\r\\n";
                    } else if(val.equals("\n")) {
                        val = "\\n";
                    }
                }
            }

            list.add( new Object[] {name, val});

        }


        model.setDataList(list);

        // remove any existing up or down arrow icons from the table header
        tableHeader.setSortedColumn(-1, false);
    }


    /**
     * Returns a String containing all of the displayed information, for logging
     * or other purposes. The platform-specific line-separator is used.
     */
    public String getInfoAsString() {
        return getInfoAsString(null);
    }

    /**
     * Returns a String containing all of the displayed information, for logging
     * or other purposes.
     * 
     * @param newline a String containing the character or characters that will be
     * used as a line separator.
     */
    public String getInfoAsString(String newline) {
        if(newline == null) newline = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer(4096);
        buffer.append("SYSTEM PROPERTIES").append(newline);
        ArrayList propList = (ArrayList) ((ArrayList) model.getDataList()).clone();
        for(int j=0; j<propList.size(); j++) {
            Object[] row = (Object[]) propList.get(j);
            buffer.append(row[0]).append("=").append(trim(row[1])).append(newline);
        }

        buffer.append(newline);
        buffer.append("Total program memory (bytes): ");
        buffer.append(lblTotalMem.getText()).append(newline);
        buffer.append("Free program memory (bytes): ");
        buffer.append(lblFreeMem.getText()).append(newline);
        buffer.append("Up since: ");
        buffer.append(lblRunningSince.getText()).append(newline);

        return buffer.toString();
    }

    private String trim(Object o) {
        if(o == null) return null;
        return o.toString().trim();
    }

    /**
     * Launches a small test program.
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        SystemPanel systemPanel = new SystemPanel(new Date());
        frame.setContentPane(systemPanel);
        frame.pack();
        GUIHelper.centerWindowOnScreen(frame);
        frame.setVisible(true);

        System.out.println(systemPanel.getInfoAsString());
    } 

}




