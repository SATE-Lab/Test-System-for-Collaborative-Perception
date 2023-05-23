cmake_minimum_required(VERSION 2.6)
project(test_pcl)
 
find_package(PCL 1.9.1 REQUIRED)
 
set(PROJECT_SOURCE_DIR
	/home/chenwei/test_pcl/
)

include_directories(${PCL_INCLUDE_DIRS})
link_directories(${PCL_LIBRARY_DIRS})
add_definitions(${PCL_DEFINITIONS})
 
add_executable(test_pcl pcl_test.cpp)

target_include_directories(test_pcl
    PRIVATE 
        ${PROJECT_SOURCE_DIR}
)

target_link_libraries (test_pcl ${PCL_LIBRARIES})
 
install(TARGETS test_pcl RUNTIME DESTINATION bin)

