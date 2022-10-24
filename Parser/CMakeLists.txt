cmake_minimum_required(VERSION 3.5)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# set the project name
project(parser)

set(SOURCES
	src/caff.cpp
	src/ciff.cpp
	src/main.cpp
)



add_executable(parser ${SOURCES})


target_include_directories(parser
    PRIVATE 
        ${PROJECT_SOURCE_DIR}/include
)