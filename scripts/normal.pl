#!/usr/bin/perl

$project="$ARGV[0]";
$input_file="input/normalDS";
$output="output/$project/normal";
$numNodes = 128; # number of rounds to perform per simulation
$numSimulations = 1;

@std = (0.2, 0.8, 1.6, 3.2, 6.4);

for($g = 1; $g <= $numSimulations; $g+=1) {
    for($numNodes = 128; $numNodes <= 1024; $numNodes*=8) {
        for my $i (@std) {
            print "java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
            "-project $project " .                                                      
            "-overwrite input=$input_file/$numNodes-$i-std.txt output=$output/$numNodes/$i/$g " .  
            "AutoStart=true\n";

            system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
            "-project $project " .                                                      
            "-overwrite input=$input_file/$numNodes-$i-std.txt output=$output/$numNodes/$i/$g " .  
            "AutoStart=true > /dev/null");                                              
        }
    }
}