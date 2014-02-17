#!/bin/bash

function usage()
{
    cat<<EOF

    Usage: `basename $0` deployTarget [branchOrTag]

        Where:
                deployTarget - host running JBoss with hot deploy enabled
                 branchOrTag - optional, what code base to push

EOF
    exit 1
}

#
# Process arguments
#

[ $# -lt 1 ] && usage

deployTarget="$1"
deployBranch="$2"

export BPM_DEPLOY_HOST="$deployTarget"

. `dirname $0`/setup.rc

#
# Main
#
cd $BPM_PROJECT_HOME

if [ ! -z "$deployBranch" ]; then
    echo "Switching to $deployBranch branch"
    git checkout $deployBranch || echo "Failed to check out deploy branch" >&2 && exit 1
fi

if mvn clean install -DskipTests=true; then
    cd $BPM_EAR_PROJECT_HOME
    mvn jboss-as:deploy -DskipTests=true -Djboss-as.hostname=$BPM_DEPLOY_HOST -Djboss-as.username=$BPM_DEPLOY_USER -Djboss-as.password=$BPM_DEPLOY_PASSWORD
fi
