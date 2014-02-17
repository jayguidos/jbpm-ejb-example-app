#!/bin/bash 

processId="$1"
version="$2"
workDay="$3"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' startDay $processId $version $workDay
