package com.cipres.mrBayesPlugin.utilities;

import java.text.DecimalFormat;
import java.util.Arrays;

import com.biomatters.geneious.publicapi.documents.sequence.SequenceAlignmentDocument;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.plugin.Options;
import com.cipres.mrBayesPlugin.ui.MrBayesOptions;

/**
 * @author Steven Stones-Havas
 * @version $Id$
 *          <p/>
 *          Created on 18/08/2008 10:38:02
 */
public class MrBayesUtilities {


	private static class NameAndIndexHolder implements Comparable {
        private String name;
        private int index;

        public NameAndIndexHolder(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public int compareTo(Object o) {
            if (o instanceof NameAndIndexHolder) {
                return ((NameAndIndexHolder) o).getName().length() - name.length();
            }
            return Integer.MAX_VALUE;
        }
    }

    public static String getCustomOrGeneratedCommandBlockFromOptions(Options options, SequenceAlignmentDocument alignment) throws DocumentOperationException {
        String block;
        if ((Boolean) options.getValue("useCustomCommandBlock")) {
            block = options.getValue("customCommandBlock").toString();
        } else {
            block = getGeneratedCommandBlockFromOptions(options);
        }
        return renameSequencesInCommandBlock(block, alignment);
    }

    public static String renameSequencesInCommandBlock(String commandBlock, SequenceAlignmentDocument alignment) {
        NameAndIndexHolder[] names = new NameAndIndexHolder[alignment.getNumberOfSequences()];
        //sort the names in reverse order of length, so that if the names contain 'species' and 'species2', they are parsed properly
        for (int i = 0; i < alignment.getNumberOfSequences(); i++) {
            SequenceDocument sequence = alignment.getSequence(i);
            names[i] = new NameAndIndexHolder(sequence.getName(), i);
        }
        Arrays.sort(names);

        for (NameAndIndexHolder name : names) {
            commandBlock = commandBlock.replace(name.getName(), "" + (name.getIndex() + 1));
        }

        //commandBlock = commandBlock.replace(, ""+(i+1));
        return commandBlock;
    }

    public static String getGeneratedCommandBlockFromOptions(Options options) throws DocumentOperationException {
    	StringBuilder sb = new StringBuilder();
        String modelString = options.getValueAsString(MrBayesOptions.MODEL);
        String rateString = options.getValueAsString(MrBayesOptions.RATE_VAR);
        sb.append("Begin MrBayes;\n");
        String outgroup = options.getValueAsString(MrBayesOptions.OUTGROUP);
        if (outgroup.length() > 0) {
            sb.append("outgroup ").append(outgroup).append(";\n");
        }
        sb.append("lset ");
        if (modelString.equals(MrBayesOptions.JC69)){
            sb.append(" Nst=1");
        	MrBayesOptions.setJson("Nst", 1);
        }
        else if (modelString.equals(MrBayesOptions.HKY85)){
            sb.append(" Nst=2");
        	MrBayesOptions.setJson("Nst", 2);
        }
        else if (modelString.equals(MrBayesOptions.GTR)){
            sb.append(" Nst=6");
            MrBayesOptions.setJson("Nst", 6);
        }
        else if (modelString.equals(MrBayesOptions.CODON)){
            sb.append(" Nucmodel=Codon");
            MrBayesOptions.setJson("Nucmodel", "Codon");
        //else do nothing, we are probably dealing with protein sequences
        }
        if (rateString.equals(MrBayesOptions.EQUAL)){
            sb.append(" rates=equal");
            MrBayesOptions.setJson("Rate", "equal");
        }
        else if (rateString.equals(MrBayesOptions.PROPINV)){
            sb.append(" rates=propinv");
            MrBayesOptions.setJson("Rate", "propinv");
        }
        else if (rateString.equals(MrBayesOptions.GAMMA)) {
            sb.append(" rates=gamma");
            MrBayesOptions.setJson("Rate", "gamma");
            sb.append(" ngammacat=").append(options.getValueAsString(MrBayesOptions.GAMMA_CATS));
            MrBayesOptions.setJson("ngammacat", options.getValueAsString(MrBayesOptions.GAMMA_CATS));
        } else if (rateString.equals(MrBayesOptions.INVGAMMA)) {
            sb.append(" rates=invgamma");
            MrBayesOptions.setJson("Rate", "invgamma");
            sb.append(" ngammacat=").append(options.getValueAsString(MrBayesOptions.GAMMA_CATS));
            MrBayesOptions.setJson("ngammacat", options.getValueAsString(MrBayesOptions.GAMMA_CATS));
        } else throw new DocumentOperationException("Rate model '" + rateString + "' is not implemented.");
        sb.append(";\n");

        double branchPrior = (Double) options.getValue(MrBayesOptions.BRANCH_LENGTH);
        double alphaPrior = (Double) options.getValue(MrBayesOptions.SHAPE_ALPHA);
//            double omegaPrior = mrbayesOptions.codonOmega.getValue();
        double agePriorAlpha = (Double) options.getValue(MrBayesOptions.TREE_AGE_ALPHA);
        double agePriorBeta = (Double) options.getValue(MrBayesOptions.TREE_AGE_BETA);
        if (((MrBayesOptions) options).getPriorType().equals(MrBayesOptions.PriorTask.UNCONSTRAINED)) {
            sb.append("prset brlenspr=unconstrained:exponential(").append(branchPrior).append(")");
        } else {
            sb.append("prset brlenspr=clock:uniform treeagepr=gamma(").append(agePriorAlpha).append(",").append(agePriorBeta).append(")");
        }
        String aminoAcidMatrix = ((MrBayesOptions) options).getAminoAcidRateMatrix();
        if (aminoAcidMatrix != null) {
            sb.append(" aamodelpr=fixed(").append(aminoAcidMatrix).append(")");
            MrBayesOptions.setJson("aamodelpr", "fixed(".concat(aminoAcidMatrix).concat(")"));
        }
        if (rateString.equals(MrBayesOptions.GAMMA) ||
                rateString.equals(MrBayesOptions.INVGAMMA))
            sb.append(" shapepr=exponential(").append(alphaPrior).append(")");
        	MrBayesOptions.setJson("shaper", "exponential(".concat(String.valueOf(alphaPrior)).concat(")"));
        if (modelString.equals(MrBayesOptions.JC69))
            sb.append(" statefreqpr=fixed(equal)");
        sb.append(";\n");

        int numIterations = (Integer) options.getValue(MrBayesOptions.LENGTH);

        sb.append("mcmc ngen=")
                .append(numIterations)
                .append(" samplefreq=")
                .append(options.getValueAsString(MrBayesOptions.SUB_FREQ))
                .append(" printfreq=1000" + " nchains=")
                .append(options.getValueAsString(MrBayesOptions.NUM_CHAINS))
                .append(" temp=")
                .append(new DecimalFormat("0.0").format(options.getValue(MrBayesOptions.TEMP)))
                        // 3.2.1 version
                .append(" savebrlens=yes starttree=random;\nset seed=")
                .append(options.getValue(MrBayesOptions.SEED))
                .append(";\n");
        MrBayesOptions.setJson("mcmc ngen", numIterations);
        MrBayesOptions.setJson("samplefreq", options.getValueAsString(MrBayesOptions.SUB_FREQ));
        MrBayesOptions.setJson("printfreq", options.getValueAsString(MrBayesOptions.NUM_CHAINS));
        MrBayesOptions.setJson("temp", new DecimalFormat("0.0").format(options.getValue(MrBayesOptions.TEMP)));
        MrBayesOptions.setJson("savebrlens", "yes");
        MrBayesOptions.setJson("starttree", "random");
        
        
        int burninvalue = (Integer) options.getValue(MrBayesOptions.BURNIN);
        if (burninvalue >= numIterations) {
            // smaller burnin - no summary
            burninvalue = numIterations / 2;
        }
        sb.append("sumt burnin=")
                .append(burninvalue / (Integer) options.getValue(MrBayesOptions.SUB_FREQ))
                .append(";\n");
        MrBayesOptions.setJson("sumt burnin", burninvalue / (Integer) options.getValue(MrBayesOptions.SUB_FREQ));
        sb.append("End;\n");
        
        return sb.toString();
    
    }

    public static int getNumOfIterationsFromOptions(Options options) {
        if ((Boolean) options.getValue("useCustomCommandBlock")) {
            String commandBlock = options.getValue("customCommandBlock").toString();
            int beginIndex = commandBlock.indexOf("ngen=");
            if (beginIndex >= 0) {
                int endIndex = commandBlock.indexOf(" ", beginIndex);
                if (endIndex >= 0) {
                    String number = commandBlock.substring(beginIndex + 5, endIndex);
                    try {
                        return Integer.parseInt(number);
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                }
            }
            return -1;
        }
        return (Integer) options.getValue(MrBayesOptions.LENGTH);
    }

}
