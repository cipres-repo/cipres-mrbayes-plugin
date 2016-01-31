package com.cipres.mrBayesPlugin.utilities;

import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.ngbw.directclient.CiCipresException;
import org.ngbw.directclient.CiClient;
import org.ngbw.directclient.CiJob;

import com.cipres.mrBayesPlugin.models.DisplayGUIModel;
import com.cipres.mrBayesPlugin.models.UserModel;
import com.cipres.mrBayesPlugin.models.UserModel.Job;

/**
 * Utility class that handles any CIPRES actions such as submit, and list jobs
 * @author rjzheng
 *
 */
public class CipresUtilities {
	
	/**
	 * Fetch and save user's jobs
	 * @param myClient
	 * @param user
	 * @throws CiCipresException
	 */
	public static void listJobs(CiClient myClient, UserModel user) throws CiCipresException
	{
		//Create a handler instance
		DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
		
		//Fetch jobs from CIPRES
		Collection<CiJob> jobs = myClient.listJobs(); 
		
		//Save jobs
		for (CiJob job : jobs)
		{
			Job newJob = user.new Job();
			newJob.setJobName(job.getClientJobName());
			newJob.setJobStage(job.getJobStage());
			newJob.setDate(job.getDateSubmitted());
			
			handler.addJob(newJob);
			
		}
		
		//Set user's jobs
		user.setJobs(handler.getJobs());
	}
	
	public static JSONArray updateList(CiClient myClient, UserModel user){
		
		//Create a handler instance
		DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
				
		try {
			CipresUtilities.listJobs(myClient, user);
		} catch (CiCipresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        handler.saveData("jobs.json", handler.getUserJSON());
        JSONObject retObj = handler.loadData("jobs.json");
        
        JSONArray retJSONArray = handler.getJobs(retObj);
    	
        return retJSONArray;
        
	}
	
	public static void deleteJobs(CiClient myClient, UserModel user) throws CiCipresException
	{
		//Create a handler instance
		DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
		
		//Fetch jobs from CIPRES
		Collection<CiJob> jobs = myClient.listJobs();
	}

}

