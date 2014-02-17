#!/bin/bash 

deploymentId="$1"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' dumpFacts $deploymentId
