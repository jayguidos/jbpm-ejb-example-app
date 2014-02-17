#!/bin/bash 

. `dirname $0`/pre-process.rc

processId="$1"
deploymentId="$2"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' startProcess $processId $deploymentId
