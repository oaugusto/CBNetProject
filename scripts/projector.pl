#!/usr/bin/perl

$project="$ARGV[0]";
$input_file="input/projectorDS";
$output="output/$project/projector";
$numNodes = 128; # number of rounds to perform per simulation
$numSimulations = 1;

for($g=1; $g<=$numSimulations; $g+=1) {
    for($numNodes=128; $numNodes<=1024; $numNodes*=2) {
        print "java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
        "-project $project " .                                                    
        "-overwrite input=$input_file/tor_$numNodes.txt output=$output/$numNodes/$g " .   
        "AutoStart=true\n";

        system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
        "-project $project " .                                                           # choose the project
        "-overwrite input=$input_file/tor_$numNodes.txt output=$output/$numNodes/$g " .  # Overwrite configuration file parameters
        "AutoStart=true > /dev/null");                                                   # Automatically start communication protocol
    }
}