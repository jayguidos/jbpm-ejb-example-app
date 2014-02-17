#!/bin/bash 

deploymentId="$1"
signalName="$2"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' signalWorkdDay $deploymentId $signalName
