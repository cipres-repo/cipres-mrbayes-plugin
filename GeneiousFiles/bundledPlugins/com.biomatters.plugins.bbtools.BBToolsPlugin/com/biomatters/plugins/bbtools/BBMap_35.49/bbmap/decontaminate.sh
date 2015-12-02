#!/bin/bash
#decontaminate in=<infile> out=<outfile>

usage(){
echo "
Written by Brian Bushnell.
Last modified May 26, 2015

Description:  Decontaminates multiplexed assemblies via normalization and mapping.

Usage:  decontaminate.sh reads=<file,file> ref=<file,file> out=<directory>

Input Parameters:
reads=<file,file>   Input reads; required parameter.
ref=<file,file>     Input assemblies; required parameter.
interleaved=auto    True forces paired/interleaved input; false forces single-ended mapping.
                    If not specified, interleaved status will be autodetected from read names.
unpigz=t            Spawn a pigz (parallel gzip) process for faster decompression.  Requires pigz to be installed.
touppercase=t       (tuc) Convert lowercase letters in reads to upper case (otherwise they will not match the reference).

Output Parameters:
pigz=f              Spawn a pigz (parallel gzip) process for faster compression.  Requires pigz to be installed.
tmpdir=.            Write temp files here.  By default is uses the system's $TMPDIR or current directory.
outdir=.            Write ouput files here.

Mapping Parameters:
kfilter=55          Set to a positive number N to require minimum N contiguous matches for a mapped read.
ambig=random        Determines how coverage will be calculated for ambiguously-mapped reads.
                        first: Add coverage only at first genomic mapping location.
                        random: Add coverage at a random best-scoring location.
                        all: Add coverage at all best-scoring locations.
                        toss: Discard ambiguously-mapped reads without adding coverage.

Filtering Parameters:
minc=3.5            Min average coverage to retain scaffold.
minp=20             Min percent coverage to retain scaffold.
minr=18             Min mapped reads to retain scaffold.
minl=500            Min length to retain scaffold.
ratio=1.2           Contigs will not be removed by minc unless the coverage changed by at least this factor.  0 disables this filter.
mapraw=t            Set true to map the unnormalized reads.  Required to filter by 'ratio'.
basesundermin=-1    If positive, removes contigs with at least this many bases in low-coverage windows.
window=500          Sliding window size 
windowcov=5         Average coverage below this will be classified as low.


Normalization Parameters:
mindepth=2          Min depth of reads to keep.
target=20           Target normalization depth.
hashes=4            Number of hashes in Bloom filter.
passes=1            Normalization passes.
minprob=0.5         Min probability of correctness to add a kmer.
dp=0.75             (depthpercentile) Percentile to use for depth proxy (0.5 means median).
ecc=f               Error-correction.
aecc=f              Agressive error-correction.
cecc=f              Conservative error-correction.
prefilter=t         Prefilter, for large datasets.
filterbits=32       (fbits) Bits per cell in primary filter.
prefilterbits=2     (pbits) Bits per cell in prefilter.

Java Parameters:
-Xmx                This will be passed to Java to set memory usage, overriding the program's automatic memory detection.
                    -Xmx20g will specify 20 gigs of RAM, and -Xmx800m will specify 800 megs.  The max is typically 85% of physical memory.

Please contact Brian Bushnell at bbushnell@lbl.gov if you encounter any problems.
"
}

pushd . > /dev/null
DIR="${BASH_SOURCE[0]}"
while [ -h "$DIR" ]; do
  cd "$(dirname "$DIR")"
  DIR="$(readlink "$(basename "$DIR")")"
done
cd "$(dirname "$DIR")"
DIR="$(pwd)/"
popd > /dev/null

#DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/"
CP="$DIR""current/"
NATIVELIBDIR="$DIR""jni/"

z="-Xmx1g"
z2="-Xms1g"
EA="-ea"
set=0

if [ -z "$1" ] || [[ $1 == -h ]] || [[ $1 == --help ]]; then
	usage
	exit
fi

calcXmx () {
	source "$DIR""/calcmem.sh"
	parseXmx "$@"
	if [[ $set == 1 ]]; then
		return
	fi
	freeRam 15000m 84
	z="-Xmx${RAM}m"
	z2="-Xms${RAM}m"
}
calcXmx "$@"


decontaminate() {
	#module unload oracle-jdk
	#module load oracle-jdk/1.7_64bit
	#module load pigz
	local CMD="java -Djava.library.path=$NATIVELIBDIR $EA $z $z2 -cp $CP jgi.DecontaminateByNormalization $@"
	echo $CMD >&2
	eval $CMD
}

decontaminate "$@"
