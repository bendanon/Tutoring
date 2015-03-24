#!/bin/bash
mkdir Submissions
cp $1 subs.zip
mv subs.zip Submissions/
cd Submissions
unzip subs.zip
rm subs.zip
find -name '*.zip' -exec sh -c 'unzip -d "${1%.*}" "$1"' _ {} \;
rm *.zip
find -name '*assignsubmission*' -exec sh -c 'cp ../runcode.sh "$1"' _ {} \;
find -name '*assignsubmission*' -exec sh -c 'cp ../books.txt "$1"' _ {} \;

