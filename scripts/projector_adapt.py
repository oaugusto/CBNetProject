#! /usr/bin/env python

import sys
import os
import threading
import numpy

# this file keep all completed experiments
log_path = "./scripts/logs/"
log_file = "projectorAdaptLog.txt"

if not os.path.exists(log_path):
    os.makedirs(log_path)

# read log file    
open(os.path.join(log_path, log_file), 'a').close()
    
log = set(line.rstrip() for line in open(os.path.join(log_path, log_file), 'r'))

# open log file for append and create a lock variable
file = open("scripts/logs/projectorAdaptLog.txt", "a+")
file_lock = threading.Lock()

# projects = ["optnet", "simplenet", "displaynet", "cbnet", "cbnetAdapt"]
projects = ["cbnetAdapt"]
# project = sys.argv[1]

# parameters of simulation
delayInt = [30, 100, 500, 1000]
numData = [1, 2, 4]
numSimulations = 1

#number of threads to simulation
numThreads = 8

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
    for n in numData:
        for i in delayInt:
            input = 'input/adapt_tor/{}_tor_128.txt'.format(n)
            output = 'output/projector_adapt/{}{}/{}'.format(project, i, n)
            cmd = '{} {} -overwrite input={} output={} decayTime={} AutoStart=true > /dev/null'.format(command, project, input, output, i)

            # not executed yet
            if cmd not in log:
                commands.append(cmd)

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
