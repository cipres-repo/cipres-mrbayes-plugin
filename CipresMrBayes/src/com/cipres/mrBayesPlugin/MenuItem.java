package com.cipres.mrBayesPlugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.DocumentUtilities;
import com.biomatters.geneious.publicapi.documents.sequence.AminoAcidSequenceDocument;
import com.biomatters.geneious.publicapi.implementations.sequence.DefaultAminoAcidSequence;
import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.Options;

import jebl.util.ProgressListener;

public class MenuItem extends DocumentOperation{
	
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
    public Options getOptions(AnnotatedPluginDocument[] docs){
        Options options = new LoginGUI();
        return options;
    }

    //This is the method that does all the work.  Geneious passes a list of the documents that were selected when the user
    //started the operation, a progressListener, and the options panel that we returned in the getOptionsPanel() method above.
//    public List performOperation(AnnotatedPluginDocument[] docs, ProgressListener progress, Options options){
//        //lets create the list that we're going to return...
//        ArrayList sequenceList = new ArrayList();
//
//        //The options that we created in the getOptions() method above has been passed to us, hopefuly the user has filled in their sequence.
//        //We get the option we added by using its name.  MultiLineStringOption has a String ValueType, so we can safely cast to a String object.
//        String residues = (String)options.getValue("residues");
//
//        //lets construct a new sequence document from the residues that the user entered
//        AminoAcidSequenceDocument sequence = new DefaultAminoAcidSequence("New Sequence","A new Sequence",residues,new Date());
//
//        //and add it to the list
//        sequenceList.add(DocumentUtilities.createAnnotatedPluginDocument(sequence));
//
//        //normaly we would set the progress incrimentaly as we went, but this operation is quick so we just set it to finished when we're done.
//        progress.setProgress(1.0);
//
//        //return the list containing the sequence we just created, and we're done!
//        return sequenceList;
//   }



}
