#! /usr/bin/env python

import sys
import os
import threading
import numpy

# this file keep all completed experiments
log_file = "scripts/logs/log.txt"

if not os.path.exists(log_file):
    os.mknod(log_file)

# read log file
log = set(line.rstrip() for line in open(log_file))

# open log file for append and create a lock variable
file = open("scripts/logs/log.txt", "a+")
file_lock = threading.Lock()

projects = ["cbnet", "seqcbnet", "splaynet", "displaynet", "semisplaynet", "seqsemisplaynet", "simplenet", "optnet"]

# sinalgo configuration
java = 'java'
classpath = 'binaries/bin:binaries/jdom.jar'
program = 'sinalgo.Run'
args = ['-batch', "-project"]
command = '{} -cp {} {} {}'.format(java, classpath, program, ' '.join(args))

#input args
inp = sys.argv[1]
out = sys.argv[2]

#extends thread class
class myThread (threading.Thread):
    def __init__(self, threadID, command):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.command = command
    def run(self):
        execute(self.command)


def execute(command):
    os.system(command)

    file_lock.acquire()
    file.write(command + "\n")
    file_lock.release()

threads = []
threadID = 1

#for each project executed
for project in projects:
    
    cmd = '{} {} -overwrite input={} output={} AutoStart=true > /dev/null'.format(command, project, inp, project + "/" + out)
    
    # not executed yet
    if cmd not in log:
        thread = myThread(threadID, cmd)
        thread.start()
        threads.append(thread)
        threadID += 1

# Wait for all threads to complete
for t in threads:
    t.join()

print("Simulation completed\n")