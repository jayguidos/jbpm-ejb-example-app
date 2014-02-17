#!/bin/bash 

processId="$1"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' killProcess $processId
