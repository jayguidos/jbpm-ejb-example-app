#!/bin/bash 

deploymentId="$1"
useHistory="$2"

. `dirname $0`/setup.rc

#
#  Main
#
runRestCmd andi '' reportDeploymentStatus $deploymentId $useHistory
