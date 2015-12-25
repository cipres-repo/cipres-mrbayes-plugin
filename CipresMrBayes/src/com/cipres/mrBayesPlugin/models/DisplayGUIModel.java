package com.cipres.mrBayesPlugin.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cipres.mrBayesPlugin.models.UserModel.Job;

public class DisplayGUIModel extends JPanel{
	
	class TableModel extends AbstractTableModel {
		private String[] columnNames = 
    		{
            "Job Name",
            "Date Submitted",
            "Job Status"
            };
		
		private List<Job> jobs = new ArrayList<Job>();
		
		public TableModel(){}
		
		public TableModel(List<Job> jobs){
			this.jobs = jobs;
		}
    	

        public int getColumnCount() {
          return columnNames.length;
        }

        public int getRowCount() {
          return jobs.size();
        }

        public String getColumnName(int col) {
          return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
        	Object jobAttribute = null;
        	Job jobObj = jobs.get(row);
        	switch(col){
        		case 0: jobAttribute = jobObj.getJobName(); break;
        		case 1: jobAttribute = jobObj.getDate(); break;
        		case 2: jobAttribute = jobObj.getJobStage(); break;
        	}
          return jobAttribute;
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column
         * would contain text ("true"/"false"), rather than a check box.
         */
        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();
        }

        public void addJob(Job job){
        	jobs.add(job);
        	fireTableDataChanged();
        }
    }
	
    
    public JPanel createPanel(JSONArray json){

    	JPanel panel = new JPanel();
    	GroupLayout layout = new GroupLayout(panel);
    	JButton updateButton = new JButton("Update List");
	    JButton downloadButton = new JButton("Download Job");
	    JButton deleteButton = new JButton("Delete Job");
	    JTable table = createTable(json);
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

    private JTable createTable(JSONArray json){
    	try{
	    	List<Job> jobs = new ArrayList<Job>();
	    	UserModel temp = new UserModel();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    	for(int i = 0; i < json.size(); i++){
	    		Job job = temp.new Job();
	    		JSONObject obj = (JSONObject) json.get(i);
	    		job.setJobName(obj.get("jobName").toString());
	    		job.setDate(df.parse(obj.get("date").toString()));
	    		job.setJobStage(obj.get("jobStage").toString());
	    		jobs.add(job);
	    	}
	    	JTable table = new JTable(new TableModel(jobs));
	    	table.setEnabled(false);
	    	return table;
    	}catch (ParseException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    
}
