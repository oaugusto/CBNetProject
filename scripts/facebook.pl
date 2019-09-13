#!/usr/bin/perl

$project="$ARGV[0]";
$input_file="input/facebookDS";
$output="output/$project/facebook";

print "java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
"-project $project " .                                                    
"-overwrite input=$input_file/datasetC_pairs_small.txt output=$output " .   
"AutoStart=true\n";

system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
"-project $project " .                                                           # choose the project
"-overwrite input=$input_file/datasetC_pairs_small.txt output=$output " .        # Overwrite configuration file parameters
"AutoStart=true > /dev/null");                                                   # Automatically start communication protocol