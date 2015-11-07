#!/bin/bash
clear
ARRAY=()
for entry in `ls $search_dir`; do
if [[ $entry = stageone* ]]; then
    #echo $entry
    ARRAY+=($entry)
fi
done
#echo ${ARRAY[@]}
#echo ${#ARRAY[@]}
for (( i = 0 ; i < ${#ARRAY[@]} - 1 ; i++ )) do
echo ${ARRAY[$i]}
rm -rf ${ARRAY[$i]}
# yadda yadda
echo RUN SHOD!!!
echo RUN SHOD!!!
echo RUN SHOD!!!
echo RUN SHOD!!!
done
