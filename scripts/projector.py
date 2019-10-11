#! /usr/bin/env python

import sys
import os
import threading

# this file keep all completed experiments
log_file = "scripts/logs/projectorLog.txt"

if not os.path.exists(log_file):
    os.mknod(log_file)
    
experiments = set(line.rstrip() for line in open(log_file))

file = open("scripts/logs/projectorLog.txt", "a+")

global_lock = threading.Lock()

project = sys.argv[1]
input_dir = "input/projectorDS"
output_dir = "output/projector/" + project

numNodes = [128, 256, 512, 1024]
numSimulations = 3
numThreads = 10

if numThreads > numSimulations:
    numThreads = numSimulations

size = numSimulations // numThreads
interval = range(1, numSimulations + 1)
chunks = [interval[x:x+size] for x in range(0, len(interval), size)]

command = "java -cp binaries/bin:binaries/jdom.jar "
command += "sinalgo.Run -batch -project "
command += project + " "

#extends thread class
class myThread (threading.Thread):
    def __init__(self, threadID, command, interval):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.command = command
        self.interval = interval
    def run(self):
        execute(self.command, self.interval)


def execute(command, interval):
        for n in numNodes:
            for i in interval:
                
                input_file = input_dir + "/" + str(n) + "/" + \
                            str(i) + "_tor_" + str(n) + ".txt"
                output_file = output_dir + "/" + str(n) + "/" + str(i)
                            
                if (input_file + "/" + project) not in experiments:
                    print(command + \
                            "-overwrite input=" + input_file + \
                            " output=" + output_file + \
                            " AutoStart=true")
                    
                    os.system(command + \
                            "-overwrite input=" + input_file + \
                            " output=" + output_file + \
                            " AutoStart=true > /dev/null")
                    
                    while global_lock.locked():
                        continue
                    
                    global_lock.acquire()
                    
                    file.write(input_file + "/" + project)
                    file.write("\n")
                    
                    global_lock.release()
                


threads = []
threadID = 1


# Create new threads
for idx in range(0, numThreads):
    thread = myThread(threadID, command, chunks[idx])
    thread.start()
    threads.append(thread)
    threadID += 1


# Wait for all threads to complete
for t in threads:
    t.join()

file.close()

print("\nExiting Main Thread\n")