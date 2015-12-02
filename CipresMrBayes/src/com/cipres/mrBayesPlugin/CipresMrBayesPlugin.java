package com.cipres.mrBayesPlugin;

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.GeneiousActionOptions;
import com.biomatters.geneious.publicapi.plugin.GeneiousPlugin;

public class CipresMrBayesPlugin extends GeneiousPlugin{
	public String getName() {
        return "Cipres MrBayes Plugin";
    }

    public String getHelp() {
        return "Getting Help";
    }

    public String getDescription() {
        return "Descriptiton Here";
    }

    public String getAuthors() {
        return "Richard Zheng";
    }

    public String getVersion() {
        return "0.1";
    }

    public String getMinimumApiVersion() {
        return "4.0";
    }

    public int getMaximumApiVersion() {
        return 4;
    }
    
    @Override
	public DocumentOperation[] getDocumentOperations() {
		return new DocumentOperation[]{new MenuItem()};
	}
}