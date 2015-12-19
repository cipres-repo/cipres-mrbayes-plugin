package com.cipres.mrBayesPlugin;

import java.util.List;

import javax.swing.JPanel;

import org.ngbw.directclient.CiApplication;
import org.ngbw.directclient.CiCipresException;
import org.ngbw.directclient.CiClient;

import com.biomatters.geneious.publicapi.components.Dialogs;
import com.biomatters.geneious.publicapi.components.Dialogs.DialogOptions;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.Options;
import com.cipres.mrBayesPlugin.models.DisplayGUIModel;
import com.cipres.mrBayesPlugin.models.LoginGUIModel;
import com.cipres.mrBayesPlugin.utilities.CipresUtilities;

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
	public Options getOptions(final AnnotatedPluginDocument[] docs) throws DocumentOperationException{
        Options options = new LoginGUIModel();
//        options.restoreDefaults();
        options.canRestoreDefaults();

        return options;
    }

    //This is the method that does all the work.  Geneious passes a list of the documents that were selected when the user
    //started the operation, a progressListener, and the options panel that we returned in the getOptionsPanel() method above.
    public List performOperation(AnnotatedPluginDocument[] docs, ProgressListener progress, Options options) throws DocumentOperationException{

    	CiApplication app = CiApplication.getInstance();
    	
        String username = (String)options.getValue("username");
        String password = (String)options.getValue("password");
        String url = app.getRestUrl();
        String appName = app.getAppname();
        String appKey = app.getAppKey();
        
        System.out.println("Username is " + username);
        System.out.println("Password is " + password);
        System.out.println("REST URL is " + url);
        System.out.println("App Name is " + appName);
        System.out.println("App Key is " + appKey);
        
        myClient = new CiClient(appKey, username, password, url);
        
        try {
			CipresUtilities.listJobs(myClient);
		} catch (CiCipresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JPanel displayGuiModel = new DisplayGUIModel().createPanel();
        DialogOptions dialogOptions = new DialogOptions(Dialogs.OK_CANCEL, "");
        Dialogs.showDialog(dialogOptions, displayGuiModel);
        
        
        return null;
   }
    
    



}
