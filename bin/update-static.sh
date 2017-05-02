#!/bin/bash
#
# Utility script to help speed up development cycle when working on
# html/jsp pages. Pulls any changed files in the WEB-INF folder and
# copies them directly to the tomcat directory on the development vm.
#
# To use, just add the files you want to upload to the git index
# (git add) and run this script with no options


container=emonocot_portal_1
template_dir=emonocot-portal/src/main/frontend/src/templates/
remote_prefix=/usr/local/tomcat/webapps/ROOT/

for changed in `git diff --name-only $template_dir`
do
  target=${changed#$template_dir}
  cp_cmd="docker cp $changed $container:$remote_prefix$target"

  echo "running $cp_cmd"
  $cp_cmd
done
