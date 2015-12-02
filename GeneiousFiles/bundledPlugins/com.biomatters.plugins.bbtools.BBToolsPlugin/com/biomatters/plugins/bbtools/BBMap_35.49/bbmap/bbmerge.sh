#!/bin/bash
#merge in=<infile> out=<outfile>

function usage(){
echo "
BBMerge v8.81
Written by Brian Bushnell and Jonathan Rood
Last modified September 25, 2015

Description:  Merges paired reads into single reads by overlap detection.
With sufficient coverage, can also merge nonoverlapping reads by kmer extension.

Usage for interleaved files:	bbmerge.sh in=<reads> out=<merged reads> outu=<unmerged reads>
Usage for paired files:     	bbmerge.sh in1=<read1> in2=<read2> out=<merged reads> outu1=<unmerged1> outu2=<unmerged2>

Input may be stdin or a fasta, fastq, or scarf file, raw or gzipped.


Input parameters:
in=null              Primary input. 'in2' will specify a second file.
interleaved=auto     May be set to true or false to override autodetection of
                     whether the input file as interleaved.
reads=-1             Quit after this many read pairs (-1 means all).


Output parameters:
out=<file>           File for merged reads. 'out2' will specify a second file.
outu=<file>          File for unmerged reads. 'outu2' will specify a second file.
outinsert=<file>     (outi) File to write read names and insert sizes.
outadapter=<file>    (outa) File to write consensus adapter sequences.
hist=null            Insert length histogram output file.
nzo=t                Only print histogram bins with nonzero values.
showhiststats=t      Print extra header lines with statistical information.
ziplevel=2           Set to 1 (lowest) through 9 (max) to change compression
                     level; lower compression is faster.
ordered=f            Output reads in same order as input.
mix=f                Output both the merged (or mergable) and unmerged reads
                     in the same file (out=).  Useful for ecco mode.


Trimming/Filtering parameters:
qtrim=f              Trim read ends to remove bases with quality below minq.
                     Trims BEFORE merging.
                     Values: t (trim both ends), 
                             f (neither end), 
                             r (right end only), 
                             l (left end only).
qtrim2=f             May be specified instead of qtrim to perform trimming 
                     only if merging is unsuccessful, then retry merging.
trimq=10             Trim quality threshold.  This may be a comma-delimited
                     list (ascending) to try multiple values.
minlength=1          (ml) Reads shorter than this after trimming, but before
                     merging, will be discarded. Pairs will be discarded only
                     if both are shorter.
maxlength=-1         Reads with longer insert sizes will be discarded.
tbo=f                (trimbyoverlap) Trim overlapping reads to remove 
                     rightmost (3') non-overlapping portion, instead of joining.
minavgquality=0      (maq) Reads with average quality below this, after 
                     trimming, will not be attempted to be merged.
maxexpectederrors=0  (mee) If positive, reads with more combined expected 
                     errors than this will not be attempted to be merged.
forcetrimleft=0      (ftl) If nonzero, trim left bases of the read to 
                     this position (exclusive, 0-based).
forcetrimright=0     (ftr) If nonzero, trim right bases of the read 
                     after this position (exclusive, 0-based).
forcetrimright2=0    (ftr2) If positive, trim this many bases on the right end.
forcetrimmod=5       (ftm) If positive, trim length to be equal to 
                     zero modulo this number.


Processing Parameters:
usejni=f             (jni) Do overlapping in C code, which is faster.  Requires
                     compiling the C code; details are in /jni/README.txt.
merge=t              Create merged reads.  If set to false, you can still 
                     generate an insert histogram.
ecco=f               Error-correct the overlapping part, but don't merge.
useoverlap=t         Attempt find the insert size using read overlap.
mininsert=35         Minimum insert size to merge reads.
mininsert0=35        Insert sizes less than this will not be considered.
                     Must be less than or equal to mininsert.
minoverlap=12        Minimum number of overlapping bases to allow merging.
minoverlap0=8        Overlaps shorter than this will not be considered.
                     Must be less than or equal to minoverlap.
minq=9               Ignore bases with quality below this.
maxq=41              Cap output quality scores at this.
entropy=t            Increase the minimum overlap requirement for low-
                     complexity reads.
efilter=6            Ban overlaps with over this many times the expected 
                     number of errors.  Lower is more strict.
pfilter=0.00002      Ban improbable overlaps.  Higher is more strict. 0 will
                     disable the filter; 1 will allow only perfect overlaps.
kfilter=0            Ban overlaps that create kmers with count below
                     this value (0 disables).  Does not seem to help.
lowercase=f          Expect lowercase letters to signify adapter sequence.
ouq=f                Calculate best overlap using quality values.
owq=t                Calculate best overlap without using quality values.
usequality=t         If disabled, quality values are completely ignored,
                     both for overlap detection and filtering.  May be useful
                     for data with inaccurate quality values.
iupacton=f           (itn) Change ambiguous IUPAC symbols to N.


Normal Mode: 
normalmode=f         Original BBMerge algorithm.  Faster, but lower overall
                     merge rate.
margin=2             The best overlap must have at least 'margin' fewer 
                     mismatches than the second best.
mismatches=3         Do not allow more than this many mismatches.
requireratiomatch=f  (rrm) Require the answer from normal mode and ratio mode
                     to agree, reducing false positives if both are enabled.
trimonfailure=t      (tof) If detecting insert size by overlap fails,
                     the reads will be trimmed and this will be re-attempted.


Ratio Mode: 
ratiomode=t          Newer algorithm.  Slower, but higher merge rate.
                     Much better for long overlaps and high error rates.
maxratio=0.09        Max error rate; higher increases merge rate.
ratiomargin=5.5      Lower increases merge rate; min is 1.
ratiooffset=0.55     Lower increases merge rate; min is 0.
ratiominoverlapreduction=3  This is the difference between minoverlap in 
                     normal mode and minoverlap in ratio mode; generally, 
                     minoverlap should be lower in ratio mode.

*** Ratio Mode and Normal Mode may be used alone or simultaneously ***


Strictness (these are mutually exclusive macros that set other parameters):
strict=f             Decrease false positive rate and merging rate.
verystrict=f         (vstrict) Greatly decrease FP and merging rate.
ultrastrict=f        (ustrict) Decrease FP and merging rate even more.
maxstrict=f          (xstrict) Maximally decrease FP and merging rate.
loose=f              Increase false positive rate and merging rate.
veryloose=f          (vloose) Greatly increase FP and merging rate.
ultraloose=f         (uloose) Increase FP and merging rate even more.
maxloose=f           (xloose) Maximally decrease FP and merging rate.
fast=f               Fastest possible mode; less accurate.


Tadpole Parameters (for read extension and error-correction):
extend=0             Extend reads to the right this much before merging.
                     Requires sufficient (>5x) kmer coverage.
extend2=0            Extend reads only after a failed merge attempt.
iterations=1         (ei) Iteratively attempt to extend by extend2 distance
                     and merge up to this many times.
ecctadpole=f         (ecct) If reads fail to merge, error-correct with Tadpole
                     and try again.  This happens prior to extend2.
removedeadends       (shave) Remove kmers leading to dead ends.
removebubbles        (rinse) Remove kmers in error bubbles.
mindepthseed=3       (mds) Minimum kmer depth to begin extension.
mindepthextend=2     (mde) Minimum kmer depth continue extension.
branchmult1=20       Min ratio of 1st to 2nd-greatest path depth at high depth.
branchmult2=3        Min ratio of 1st to 2nd-greatest path depth at low depth.
branchlower=3        Max value of 2nd-greatest path depth to be considered low.
ibb=t                Ignore backward branches when extending.
extra=<file>         A file or comma-delimited list of files of reads to use
                     for kmer counting, but not for merging or output.
k=31                 Kmer length (1-31 is fastest).
prealloc=f           Pre-allocate memory rather than dynamically growing; 
                     faster and more memory-efficient for large datasets.  
                     A float fraction (0-1) may be specified, default 1.
prefilter=0          If set to a positive integer, use a countmin sketch to 
                     ignore kmers with depth of that value or lower, to
                     reduce memory usage.
minprob=0.5          Ignore kmers with overall probability of correctness 
                     below this, to reduce memory usage.


Java Parameters:
-Xmx                 This will be passed to Java to set memory usage, 
                     overriding the program's automatic memory detection.
                     For example, -Xmx400m will specify 400 MB RAM.

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

z="-Xmx1000m"
EA="-ea"
set=0

if [ -z "$1" ] || [[ $1 == -h ]] || [[ $1 == --help ]]; then
	usage
	exit
fi

calcXmx () {
	source "$DIR""/calcmem.sh"
	parseXmx "$@"
}
calcXmx "$@"

function merge() {
	#module unload oracle-jdk
	#module load oracle-jdk/1.7_64bit
	#module load pigz
	local CMD="java -Djava.library.path=$NATIVELIBDIR $EA $z -cp $CP jgi.BBMerge $@"
	echo $CMD >&2
	eval $CMD
}

merge "$@"
