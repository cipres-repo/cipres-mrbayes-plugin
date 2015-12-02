package com.cipres.mrBayesPlugin;

import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;

public class MenuItem extends DocumentOperation{

	@Override
	public GeneiousActionOptions getActionOptions() {
		GeneiousActionOptions parent = new GeneiousActionOptions("Cipres MrBayes","Description").setMainMenuLocation(GeneiousActionOptions.MainMenu.Tools);
		GeneiousActionOptions submenuItem = new GeneiousActionOptions("Item1", "Description");
		GeneiousActionOptions sub = parent.createSubmenuActionOptions(parent, submenuItem);
		return sub;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Getting Help";
	}

	@Override
	public DocumentSelectionSignature[] getSelectionSignatures() {
	    DocumentSelectionSignature singleAlignmentSignature = new DocumentSelectionSignature(
                SequenceAlignmentDocument.class, 1, 1);
        return new DocumentSelectionSignature[]{singleAlignmentSignature/*, nucleotideSequenceSelectionSignature, proteinSequenceSelectionSignature*/};
    
	}
	


}
