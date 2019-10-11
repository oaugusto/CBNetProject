#!/usr/bin/perl

$project="$ARGV[0]";
$input_file="input/randomwalkDS";
$output="output/$project/randomwalk";
$numNodes = 128; # number of rounds to perform per simulation
$numSimulations = 10;

@walks = (4, 16, 32);
@flows = (1, 4, 16);

for($g = 1; $g <= $numSimulations; $g+=1) {
    for($numNodes = 128; $numNodes <= 1024; $numNodes*=8) {
        for my $i (@walks) {
            for my $j (@flows) {
                print "java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
                "-project $project " .                                                      
                "-overwrite input=$input_file/$numNodes-nodes-$i-walkLength-$j-numberOfConcurrentFlows-input.txt output=$output/$numNodes/$i/$j/$g " .  
                "AutoStart=true\n";

                system("java -cp binaries/bin:binaries/jdom.jar sinalgo.Run -batch " .
                "-project $project " .                                                      
                "-overwrite input=$input_file/$numNodes-nodes-$i-walkLength-$j-numberOfConcurrentFlows-input.txt output=$output/$numNodes/$i/$j/$g " .  
                "AutoStart=true > /dev/null");                                              
            }
        }
    }
}
