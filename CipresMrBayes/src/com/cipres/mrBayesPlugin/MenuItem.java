package com.cipres.mrBayesPlugin;

import java.util.Collection;
import java.util.List;

import org.ngbw.directclient.CiCipresException;
import org.ngbw.directclient.CiClient;
import org.ngbw.directclient.CiJob;

import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.Options;

import jebl.util.ProgressListener;


public class MenuItem extends DocumentOperation{
	private static CiClient myClient;
	
	public String getUniqueId(){
		return "Cipres_MrBayes";
	}
	
	@Override
	public GeneiousActionOptions getActionOptions() {
		return new GeneiousActionOptions("Cipres MrBayes").setMainMenuLocation(GeneiousActionOptions.MainMenu.Tools);
		
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Getting Help";
	}

	@Override
	public DocumentSelectionSignature[] getSelectionSignatures() {
		return new DocumentSelectionSignature[0];
	}
	
	
	//Geneious will display the Options returned from this method as a panel before calling performOperation().
	@Override
	public Options getOptions(final AnnotatedPluginDocument[] docs) throws DocumentOperationException{
        Options options = new LoginGUI();
//        options.restoreDefaults();
        options.canRestoreDefaults();
        return options;
    }

    //This is the method that does all the work.  Geneious passes a list of the documents that were selected when the user
    //started the operation, a progressListener, and the options panel that we returned in the getOptionsPanel() method above.
    public List performOperation(AnnotatedPluginDocument[] docs, ProgressListener progress, Options options) throws DocumentOperationException{

    	
        String username = (String)options.getValue("username");
        String password = (String)options.getValue("password");
        String url = (String)options.getValue("url");
        String appName = (String)options.getValue("appName");
        String appKey = (String)options.getValue("appKey");
        
        System.out.println("Username is " + username);
        System.out.println("Password is " + password);
        System.out.println("REST URL is " + url);
        System.out.println("App Name is " + appName);
        System.out.println("App Key is " + appKey);
        
        myClient = new CiClient(appKey, username, password, url);
        
        try {
			listJobs();
		} catch (CiCipresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
   }
    
    private static void listJobs() throws CiCipresException
	{

		int count = 0;
		Collection<CiJob> jobs = myClient.listJobs(); 
		for (CiJob job : jobs)
		{
			count += 1;
			System.out.print("\n" + count + ". ");
			job.show(false);
		}
	}



}
