#!/usr/bin/perl

$numNodes = 128; # number of rounds to perform per simulation
$numSimulations = 1;
$project="cbnet";
$input="input/projectorDS";
$output="output/cbnet/projectorDS";

for($g=1; $g<=$numSimulations; $g+=1) {
    for($numNodes=128; $numNodes<=1024; $numNodes*=2) {
        print "java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
        "-project $project " .                                                    
        "-overwrite input=$input/tor_$numNodes.txt output=/$numNodes/$g " .   
        "AutoStart=true\n";

        system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
        "-project $project " .                                                      # choose the project
        "-overwrite input=$input/tor_$numNodes.txt output=/$numNodes/$g " .    # Overwrite configuration file parameters
        "AutoStart=true");                                                          # Automatically start communication protocol
    }
}