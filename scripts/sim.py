#! /usr/bin/env python

import sys
import os

# sinalgo configuration
java = 'java'
classpath = 'binaries/bin:binaries/jdom.jar'
program = 'sinalgo.Run'
args = ['-batch', "-project"]
command = '{} -cp {} {} {}'.format(java, classpath, program, ' '.join(args))

#input args
inp = sys.argv[1]
out = sys.argv[2]

cmd = '{} {} -overwrite input={} output={} AutoStart=true > /dev/null'.format(command, project, inp, out)

#execute the cmd
os.system(cmd)

print("Simulation completed:\n" + cmd + "\n")