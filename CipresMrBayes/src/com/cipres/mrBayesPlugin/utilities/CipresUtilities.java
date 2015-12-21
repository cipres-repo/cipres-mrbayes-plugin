package com.cipres.mrBayesPlugin.utilities;

import java.util.Collection;

import org.ngbw.directclient.CiCipresException;
import org.ngbw.directclient.CiClient;
import org.ngbw.directclient.CiJob;

import com.cipres.mrBayesPlugin.models.UserModel;
import com.cipres.mrBayesPlugin.models.UserModel.Job;

public class CipresUtilities {
	
	public static void listJobs(CiClient myClient, UserModel user) throws CiCipresException
	{
		DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
		Job newJob = user.new Job();
		Collection<CiJob> jobs = myClient.listJobs(); 
		for (CiJob job : jobs)
		{
			newJob.setJobName(job.getClientJobName());
			newJob.setJobStage(job.getJobStage());
			newJob.setDate(job.getDateSubmitted());
			
			handler.addJob(newJob);
			
//			System.out.println("Results: "
//					+ newJob.getJobName() + "\n" 
//					+ newJob.getJobStage() + "\n" 
//					+ newJob.getDate());
			
		}
		
		user.setJobs(handler.getJobs());
	}

}

