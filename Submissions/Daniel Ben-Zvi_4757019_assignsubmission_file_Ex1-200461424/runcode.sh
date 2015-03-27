#!/bin/bash

javac -d . TimeTest.java && echo -e "\e[1;34mCompiled successfully.\e[0m"

echo -e "\e[1;36m========= Good inputs===========\e[0m"

echo -e " \e[97m`md5sum books.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest books.txt books_copy.txt 128\e[0m"
java TimeTest books.txt books_copy.txt 128 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest /verbose books.txt books_copy.txt 256\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 256  
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest /verbose books.txt books_copy.txt 512\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 512 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest /verbose books.txt books_copy.txt 1024\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 1024 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;36mjava TimeTest /verbose books.txt books_copy.txt 100000000\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 100000000 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[1;31m========= Bad inputs===========\e[0m"

echo -e "\e[0;31mjava TimeTest /verbose books.txt books_copy.txt \e[0m\e[1;33m(No blocksize)\e[0m"
java TimeTest /verbose books.txt books_copy.txt
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;31mjava TimeTest /verbose books.txt books_copy.txt 100000000000000 \e[0m\e[1;33m(Too big)\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 100000000000000 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;31mjava TimeTest /verbose books.txt books_copy.txt 0 \e[0m\e[1;33m(Too small)\e[0m" 
java TimeTest /verbose books.txt books_copy.txt 0 
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;31mjava TimeTest books.txt books_copy.txt 128 \e[0m\e[1;33m(No verbose) \e[0m"
java TimeTest books.txt books_copy.txt 128
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[0;31mjava TimeTest \e[0m\e[1;33m(No params) \e[0m" 
java TimeTest
echo -e " \e[97m`md5sum books_copy.txt`\e[0m"

echo -e "\e[1;34mDone.\e[0m"
rm books_copy.txt
