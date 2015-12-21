package com.cipres.mrBayesPlugin.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cipres.mrBayesPlugin.models.UserModel;
import com.cipres.mrBayesPlugin.models.UserModel.Job;

public class DataHandlingUtilities {
	
	private static DataHandlingUtilities singletonInstance = null;
	private List<Job> jobs = new ArrayList<Job>();
	private UserModel user = new UserModel();
	
	public static synchronized DataHandlingUtilities getInstance(){
		if(singletonInstance == null)
			singletonInstance = new DataHandlingUtilities();
		return singletonInstance;
	}
	
	public void addJob(Job job){
		synchronized (job){
			jobs.add(job);
		}
	}
	
	public List<Job> getJobs(){
		return jobs;
	}
	
	public void addUser(UserModel user){
		synchronized (user){
			this.user = user;
		}
	}
	
	public String getUserJSON(){
		
		JSONObject obj = new JSONObject();
		obj.put("username", user.getUsername());
		obj.put("password", user.getPassword());
		obj.put("restUrl", user.getRestUrl());
		obj.put("appName", user.getAppName());
		obj.put("appKey", user.getAppName());
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
 
		JSONArray jobJson = new JSONArray();
		JSONObject jobObj = new JSONObject();

		for (Job job : jobs) {
 
			Map<String, String> jobData = new HashMap<String, String>();
			jobData.put("jobName", job.getJobName());
			jobData.put("data", df.format(job.getDate()));
			jobData.put("jobStage", job.getJobStage() + "");
 
			jobJson.add(jobData);
		}
		obj.put("jobs", jobJson);
		return obj.toString();
	}
	
	public boolean saveData(String filename, String jsonData){
		FileWriter file;
		try {
			file = new FileWriter("/Users/rjzheng/Documents/" + filename);
			file.write(jsonData);
			file.flush();
			file.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	//Return a String of JSON ready to be parsed up. may not need to use parser for this method, 
	//should replace it with just read 
	public boolean loadData(String filename){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("/Users/rjzheng/Documents/" + filename));
			JSONObject json = (JSONObject) obj;
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	
}
