/*
 * FrequencyDistribution.java
 *
 * Copyright (C) 2002-2006 Alexei Drummond and Andrew Rambaut
 *
 * This file is part of BEAST.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *  BEAST is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package dr.util;

/**
 * @author Alexei Drummond
 * @version $Id: FrequencyDistribution.java 43609 2011-07-28 03:19:13Z matthew $
 */
public class FrequencyDistribution {

	/** The size of the bin */
	private double binSize;
	
	/** the lower boundary of first bin */
	private double start = 0.0;
	
	/** the number of values lower than first bin */
	private double smaller;

	/** the number of values greater than last bin */
	private double larger;

	/** the frequncy counts of each bin */
	private int[] bins;

	public FrequencyDistribution(double start, int numBins, double binSize) {
		init(start, numBins, binSize);
	}

	public FrequencyDistribution(int numBins, double binSize) {
		init(0.0, numBins, binSize);
	}

	public FrequencyDistribution(double[] stats, int numBins, double binSize) {
		init(0.0, numBins, binSize);
		for (int i = 0; i < stats.length; i++) {
			addValue(stats[i]);
		}
	}

	/**
	 * Returns the number of bins.
	 */
	public int getBinCount() {
		return bins.length;
	}

	public double getBinSize() {
		return binSize;
	}

	/** Returns lower bound of first bin. */
	public double getLowerBound() {
		return start;
	}

	/**
	 * @return the number of values falling in this bin.
	 */
	public int getFrequency(int bin) {
		return bins[bin];
	}

	public void addValue(double value) {
	
		double diff = value - start;

		int index = (int)(diff / binSize);
		if (index < 0) {
			smaller += 1;
		} else if (index >= bins.length) {
			larger += 1;
		} else bins[index] += 1;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("< " + start + "\t" + smaller + "\n");
		double mean;
		for (int i = 0; i < bins.length; i++) {
			mean = start + (binSize * ((double)i + 0.5));
			buffer.append(mean + "\t" + bins[i] + "\n");
		}
		double end = start + (binSize * bins.length);
		buffer.append(">= " + end + "\t" + larger + "\n");
		return new String(buffer);
	}

	private void init(double start, int numBins, double binSize) {
		bins = new int[numBins];
		this.binSize = binSize; 
		smaller = 0;
		larger = 0;
		this.start = start;
	}
}
