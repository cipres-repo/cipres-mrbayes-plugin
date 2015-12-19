package com.cipres.mrBayesPlugin.models;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.biomatters.geneious.publicapi.plugin.Options;

public class DisplayGUIModel extends JPanel{
	String[] columnNames = 
		{" ",
        "Job Name",
        "Date Submitted",
        "Job Status"
        };
    
    Object[][] data = {
    	    {" ", "Test1", "10/12/15", new Boolean(false)},
    	    {" ", "Test2", "12/01/15", new Boolean(true)}
    	};
    
    public DisplayGUIModel() {
    }
    
    public JPanel createPanel(){

    	JPanel panel = new JPanel();
    	GroupLayout layout = new GroupLayout(panel);
    	JButton updateButton = new JButton("Update List");
	    JButton downloadButton = new JButton("Download Job");
	    JButton deleteButton = new JButton("Delete Job");
	    JTable table = createTable(data, columnNames);
	    JScrollPane scroll = new JScrollPane(table);

    	panel.setLayout(layout);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setAutoCreateGaps(true);
    	
	    layout.setHorizontalGroup(
	    	layout.createSequentialGroup()
	    		.addComponent(scroll)
	    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    			.addComponent(updateButton)
	    			.addComponent(downloadButton)
	    			.addComponent(deleteButton))
		);
	    
	    layout.setVerticalGroup(
	    	layout.createParallelGroup()
			.addComponent(scroll)
    		.addGroup(layout.createSequentialGroup()
    			.addComponent(updateButton)
    			.addComponent(downloadButton)
    			.addComponent(deleteButton))
	    );
	   
	    return panel;
    }

    private JTable createTable(Object[][] data, String[] columnNames){
    	JTable table = new JTable(data, columnNames);
    	table.setEnabled(false);
    	return table;
    }
}
