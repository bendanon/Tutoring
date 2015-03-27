#!/bin/bash

javac TimeTest.java && echo -e "\e[1;34mCompiled successfully.\e[0m"

echo -e "\e[1;36m========= Good inputs===========\e[0m"

echo -e " \e[97m`md5sum books.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest bla bla books.txt books_copy.txt 128\e[0m"
java TimeTest bla bla books.txt books_copy.txt 128 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest bla bla /force books.txt books_copy.txt 256\e[0m" 
java TimeTest bla bla /force books.txt books_copy.txt 256  
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

