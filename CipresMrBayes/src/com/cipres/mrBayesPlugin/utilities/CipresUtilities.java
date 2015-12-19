package com.cipres.mrBayesPlugin.utilities;

import java.util.Collection;

import org.ngbw.directclient.CiCipresException;
import org.ngbw.directclient.CiClient;
import org.ngbw.directclient.CiJob;

public class CipresUtilities {
	
	public static void listJobs(CiClient myClient) throws CiCipresException
	{
		Collection<CiJob> jobs = myClient.listJobs(); 
		for (CiJob job : jobs)
		{
			System.out.print(
					"\n Job Name: " + job.getClientJobName() + "\n Date Submitted: " 
					+ job.getDateSubmitted() + "\n Job Stage: " + job.getJobStage());
			
		}
	}

}

