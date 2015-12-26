package com.cipres.mrBayesPlugin;

import java.util.List;

import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import com.cipres.mrBayesPlugin.models.UserModel;
import com.cipres.mrBayesPlugin.utilities.CipresUtilities;
import com.cipres.mrBayesPlugin.utilities.DataHandlingUtilities;

import jebl.util.ProgressListener;

public class MenuItem extends DocumentOperation{
	private static CiClient myClient;
	JPanel displayGuiModel;
	private Boolean first = true;
	
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

    	
    	if(first == true){
    	CiApplication app = CiApplication.getInstance();
    	DataHandlingUtilities handler = DataHandlingUtilities.getInstance();
    	JSONObject data = new JSONObject();
    	
        String username = (String)options.getValue("username");
        String password = (String)options.getValue("password");
        String url = app.getRestUrl();
        String appName = app.getAppname();
        String appKey = app.getAppKey();
        
        UserModel user = new UserModel(username, password, url, appKey, appName);
        handler.addUser(user);
        
        myClient = new CiClient(appKey, username, password, url);
        
        try {
			CipresUtilities.listJobs(myClient, user);
		} catch (CiCipresException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
        handler.saveData("jobs.json", handler.getUserJSON());
        JSONObject retObj = handler.loadData("jobs.json");
        
        JSONArray retJSONArray = handler.getJobs(retObj);
    	
        setPanel(new DisplayGUIModel().createPanel(retJSONArray));
    	}
        DialogOptions dialogOptions = new DialogOptions(Dialogs.OK_CANCEL, "");
        Dialogs.showDialog(dialogOptions, getPanel());
        
        first = false;
        
        return null;
   }
    
    public JPanel getPanel(){
    	return this.displayGuiModel;
    }
    
    public void setPanel(JPanel display){
    	this.displayGuiModel = display;
    }
    
    



}
