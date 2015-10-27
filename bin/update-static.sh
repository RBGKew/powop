#!/bin/bash
#
# Utility script to help speed up development cycle when working on
# html/jsp pages. Pulls any changed files in the WEB-INF folder and
# copies them directly to the tomcat directory on the development vm.
#
# To use, just add the files you want to upload to the git index
# (git add) and run this script with no options


deployment=emonocot
local_prefix=*/WEB-INF/
remote_prefix=/var/lib/tomcat7/webapps/ROOT/WEB-INF/

ssh $deployment "sudo chmod -R a+w $remote_prefix"

for changed in `git diff --cached --name-only | grep WEB-INF/`
do
  target=${changed#$local_prefix}
  scp_cmd="scp $changed $deployment:$remote_prefix$target"

  echo "running $scp_cmd"
  $scp_cmd
done
