package com.cipres.mrBayesPlugin;

import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.DocumentSelectionSignature;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.Icons;
import com.biomatters.geneious.publicapi.utilities.IconUtilities;

public class CipresMrBayesTree extends DocumentOperation{

	@Override
	public GeneiousActionOptions getActionOptions() {
		//saved under cipres-mrbayes-plugin/GeneiousFiles/resources/images/TreeIcon.png for now
		Icons icon= IconUtilities.getIcons("TreeIcon.png");
        return new GeneiousActionOptions("MrBayes_Cipres",
                "Perform posterior tree simulation from an alignment", icon, GeneiousActionOptions.Category.TreeBuilding);
	}

	@Override
	public String getHelp() {
		return "Getting Help";
	}

	@Override
	public DocumentSelectionSignature[] getSelectionSignatures() {
		DocumentSelectionSignature singleAlignmentSignature = new DocumentSelectionSignature(
                SequenceAlignmentDocument.class, 1, 1);
        return new DocumentSelectionSignature[]{singleAlignmentSignature/*, nucleotideSequenceSelectionSignature, proteinSequenceSelectionSignature*/};
	}

}
