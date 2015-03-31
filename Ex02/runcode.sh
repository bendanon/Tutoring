#!/bin/bash

#Find all the sources
find -name "*.java" > sources.txt

#Remove packages
sed -i 's/package.*;//g' `cat sources.txt | grep -v Model` && \

#Set the CLASSPATH
export CLASSPATH="/usr/share/java/junit4.jar"

#Compile the sources
javac @sources.txt -d . && echo -e "\e[1;34mCompiled successfully.\e[0m" && \

#Run the tests
java -cp .:/usr/share/java/junit4.jar org.junit.runner.JUnitCore Tests

#Cleanup
rm `find . -name "*.class"`
rm sources.txt

echo -e "\e[1;34mDone.\e[0m"
