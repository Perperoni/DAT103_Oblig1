#! /bin/bash

#clone repo
#soke igjennom mappestruktur
#joine og sorte
#printe ut i txt
#git clone --depth 1 https://github.com/git/git.git

cd git
ls > result1.txt 
cd builtin
ls > ../result2.txt && cd ../
join  <(sort result1.txt) <(sort result2.txt) > resulttemp.txt && rm result1.txt result2.txt
#cat resulttemp.txt

#del 2
#3 colums
# filename
# number of lines
# number of lines in builtin
awk '{
    cmd = "wc -l < " $1 
    cmd | getline git_count
    close(cmd)

    cmd = "wc -l < builtin/" $1  
    cmd | getline builtin_count
    close(cmd)

    print $1, git_count, builtin_count
}' resulttemp.txt > ../result.txt

cat ../result.txt && rm resulttemp.txt

