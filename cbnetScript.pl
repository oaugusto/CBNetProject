#!/usr/bin/perl

$numNodes = 128; # number of rounds to perform per simulation
$numSimulations = 5;
$path="cbnet/";

for($g=1; $g<=$numSimulations; $g+=1) {
    for($numNodes=128; $numNodes<=1024; $numNodes*=2) {
        print "Simulation splaynet size:$numNodes execution:$g\n";
        system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
        "-project cbnet " .                             # choose the project
        "-overwrite path=cbnet/$numNodes/$g size=$numNodes " .    # Overwrite configuration file parameters
        "AutoStart=true");                             # Automatically start communication protocol
    }
}