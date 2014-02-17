#!/bin/bash 

deploymentId="$1"
processId="$2"
useHistory="$3"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' reportProcessStatus $deploymentId $processId $useHistory
