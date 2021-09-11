#! /usr/bin/env python

import sys
import os
import threading
import numpy

# this file keep all completed experiments
log_path = "./scripts/logs/"
log_file = "normalLog.txt"

if not os.path.exists(log_path):
    os.makedirs(log_path)

# read log file    
open(os.path.join(log_path, log_file), 'a').close()
    
log = set(line.rstrip() for line in open(os.path.join(log_path, log_file), 'r'))

# open log file for append and create a lock variable
file = open("scripts/logs/normalLog.txt", "a+")
file_lock = threading.Lock()

#projects = ["optnet", "flattening", "flatnet", "splaynet", "displaynet", "semisplaynet", "seqsemisplaynet", "simplenet"]
projects = ["splaynet", "displaynet", "flattening", "flatnet", "semisplaynet", "seqsemisplaynet"]
#projects = ["simplenet", "semisplaynet", "seqsemisplaynet"]
#projects = ["flattening", "flatnet", "cbnet", "seqcbnet", "splaynet", "displaynet", "semisplaynet", "seqsemisplaynet", "simplenet"]
#projects = ["optnet", "splaynet", "displaynet", "simplenet"]
# project = sys.argv[1]

# parameters of simulation
numSimulations = 10
numNodes = [128, 1024]
std = [0.2, 0.8, 1.6, 3.2, 6.4]

#number of threads to simulation
numThreads = 10

java = 'java'
classpath = 'binaries/bin:binaries/jdom.jar'
program = 'sinalgo.Run'
args = ['-batch', "-project"]
command = '{} -cp {} {} {}'.format(java, classpath, program, ' '.join(args))

#extends thread class
class myThread (threading.Thread):
    def __init__(self, threadID, commands):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.commands = commands
    def run(self):
        execute(self.commands)


def execute(commands):
    for command in commands:
        print(command)
        os.system(command)

        file_lock.acquire()
        file.write(command + "\n")
        file_lock.release()


#for each project executed
for project in projects:

    #list of commands to be executed
    commands = []

    # generate all possibles inputs for simulation
    for n in numNodes:
        for s in std:
            for i in range(1, numSimulations + 1):
                input = 'input/normalDS/{}/{}-{}-std.txt'.format(n, n, s)
                output = 'output/normal/{}/{}/{}/{}'.format(project, n, s, i)
                cmd = '{} {} -overwrite input={} output={} AutoStart=true > /dev/null'.format(command, project, input, output)

                # not executed yet
                if cmd not in log:
                    commands.append(cmd)
                    #print(cmd)

    numCommands = len(commands)

    # if number of threads is greater than pairsLenght
    # just makes number of threads equals to pairsLenght
    if numCommands == 0:
        print("No experiment to be executed for project {}".format(project))
        exit
    elif numThreads > numCommands:
        numThreads = numCommands

    # split task for threads
    size = numCommands // numThreads
    chunks =  numpy.array_split(commands, numThreads)

    threads = []
    threadID = 1

    # Create new threads
    for idx in range(0, numThreads):
        thread = myThread(threadID, chunks[idx])
        thread.start()
        threads.append(thread)
        threadID += 1


    # Wait for all threads to complete
    for t in threads:
        t.join()


print("Simulation Completed")
                    
