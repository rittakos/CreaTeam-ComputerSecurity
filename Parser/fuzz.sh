AFL=~/projects/AFL
Project=~/projects/KreaTeam-ComputerSecurity
Size=104857600

export CC=$AFL/afl-gcc
export CXX=$AFL/afl-g++

mkdir -p aflbuild \
&& cd aflbuild \
&& cmake .. \
&& make \
&& $AFL/afl-fuzz -m $Size -i $Project/ExampleFiles -o $Project/Parser/findings $Project/Parser/aflbuild/parser @@