package com.cipres.mrBayesPlugin.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.json.simple.parser.JSONParser;
import org.ngbw.directclient.CiClient;

import com.cipres.mrBayesPlugin.CipresMrBayes;
import com.cipres.mrBayesPlugin.models.UserModel.Job;
import com.cipres.mrBayesPlugin.utilities.CipresUtilities;
import com.cipres.mrBayesPlugin.utilities.DataHandlingUtilities;

/**
 * Display user's jobs and allow users to submit/modify jobs
 * @author rjzheng
 *
 */
@SuppressWarnings("serial")
public class DisplayGUIModel extends JPanel{
	
	public static JTable table;

	public static JTable getTable() {
		return table;
	}

	public static void setTable(JTable table) {
		DisplayGUIModel.table = table;
	}

	/**
	 * Create and layout the job list table and buttons
	 * @param json
	 * @return panel
	 */
	public static JPanel createPanel(JSONArray json){

		//Create the Panel
    	JPanel panel = new JPanel();
    	//Add layout to panel
    	GroupLayout layout = new GroupLayout(panel);
    	//Create the buttons
    	JButton submitButton = new JButton("Submit Job");
    	JButton updateButton = new JButton("Update List");
	    JButton downloadButton = new JButton("Download Job");
	    JButton deleteButton = new JButton("Delete Job");
	    updateButton.addActionListener(new updateListener());
	    //Create the table
	    table = createTable(json);
	    //Add the table to a scroll panel
	    JScrollPane scroll = new JScrollPane(table);
	    //Set the layout
    	panel.setLayout(layout);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setAutoCreateGaps(true);
    	
	    //Set the horizontal grouping
	    layout.setHorizontalGroup(
	    	layout.createSequentialGroup()
	    		.addComponent(scroll)
	    		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	    			.addComponent(updateButton)
	    			.addComponent(submitButton)
	    			.addComponent(downloadButton)
	    			.addComponent(deleteButton))
		);
	    
	    //Set the vertical grouping
	    layout.setVerticalGroup(
	    	layout.createParallelGroup()
			.addComponent(scroll)
    		.addGroup(layout.createSequentialGroup()
    			.addComponent(updateButton)
    			.addComponent(submitButton)
    			.addComponent(downloadButton)
    			.addComponent(deleteButton))
	    );
	    return panel;
    }

	
	/**
	 * Create the table with user's job list
	 * @param json
	 * @return table
	 */
    public static JTable createTable(JSONArray json){
    	try{
	    	List<Job> jobs = new ArrayList<Job>();
	    	UserModel temp = new UserModel();
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	    	for(int i = 0; i < json.size(); i++){
	    		Job job = temp.new Job();
	    		JSONObject obj = (JSONObject) json.get(i);
	    		job.setSelected(false);
	    		job.setJobName(obj.get("jobName").toString());
	    		job.setDate(df.parse(obj.get("date").toString()));
	    		job.setJobStage(obj.get("jobStage").toString());
	    		jobs.add(job);
	    	}
	    	JTable new_table = new JTable(new TableModel(jobs));
	    	return new_table;
    	}catch (ParseException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * Customized table model
     * @author rjzheng
     *
     */
	static class TableModel extends AbstractTableModel {
		
		//Default table column names
		private String[] columnNames = 
    		{
			"Select",
            "Job Name",
            "Date Submitted",
            "Job Status"
            };
		
		private List<Job> jobs = new ArrayList<Job>();
		
		/**
		 * Empty constructor
		 */
		public TableModel(){}
		
		/**
		 * Constructor with job list input
		 * @param jobs
		 */
		public TableModel(List<Job> jobs){
			this.jobs = jobs;
		}

		/**
		 * Get the column count
		 * @return column count
		 */
        public int getColumnCount() {
          return columnNames.length;
        }

        /**
         * Get the row count
         * @return job list size
         */
        public int getRowCount() {
          return jobs.size();
        }

        /**
         * Get the column name at a given column
         * @param col
         * @return column name
         */
        public String getColumnName(int col) {
          return columnNames[col];
        }
        
        /**
         * Get and set the value of a given row and column
         * @param row
         * @param col
         * @return job object
         */
        public Object getValueAt(int row, int col) {
        	Object jobAttribute = null;
        	Job jobObj = jobs.get(row);
        	switch(col){
        		case 0: jobAttribute = jobObj.getSelected();break;
        		case 1: jobAttribute = jobObj.getJobName(); break;
        		case 2: jobAttribute = jobObj.getDate(); break;
        		case 3: jobAttribute = jobObj.getJobStage(); break;
        	}
        	return jobAttribute;
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            Job jobObj = jobs.get(rowIndex);
            if(columnIndex == 0) {
                jobObj.setSelected((Boolean)aValue);
            }
            
        }

        /**
         * Display checkbox instead of true/false
         */
		public Class getColumnClass(int c) {
	    	if(c == 0){
	            return Boolean.class;
	        }else{
	        	return String.class;
	        }
        }

        /**
         * Add job and update changes to table
         * @param job
         */
        public void addJob(Job job){
        	jobs.add(job);
        }
        
        public boolean isCellEditable(int row, int column) {
            return column==0;
        }
    }
    
	static class updateListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
			System.out.println("JSON:" + handler.getUserJSON());
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			try {
				json = (JSONObject) parser.parse(handler.getUserJSON());
			} catch (org.json.simple.parser.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("JSON: " + json.toJSONString());
			System.out.println((String)json.get("restUrl"));
			UserModel user = new UserModel((String)json.get("username"), 
					(String)json.get("password"), (String)json.get("url"),
					(String)json.get("appKey"), (String)json.get("appName"));
			JSONArray retJSONArray = CipresUtilities.updateList(CipresMrBayes.myClient, user);
			DisplayGUIModel.setTable(DisplayGUIModel.createTable(retJSONArray));
			//Update table
	        
	        System.out.println("done");
		}
		
	}
}
