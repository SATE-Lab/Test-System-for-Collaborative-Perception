#pragma once

#include <omp.h>
#include <ctime>
#include <vector>
#include <string>
#include <algorithm>

#include <pcl/io/pcd_io.h>
#include <pcl/common/common_headers.h>

#include <sys/stat.h>

#include <pcl/io/boost.h>
#include <boost/program_options.hpp>

#include <fstream>



void convertPCDtoBin(std::string &in_file, std::string& out_file ,int flag)
{
	pcl::PointCloud<pcl::PointXYZRGB>::Ptr cloud(new pcl::PointCloud<pcl::PointXYZRGB>);

	if (pcl::io::loadPCDFile<pcl::PointXYZRGB>(in_file, *cloud) == -1) //* load the file
	{
		std::string err = "Couldn't read file " + in_file;
		PCL_ERROR(err.c_str());
		return;// (-1);
	}
	std::cout << "Loaded "
		<< cloud->width * cloud->height
		<< " data points from "
		<< in_file
		<< " with the following fields: "
		<< std::endl;

	std::ofstream myFile(out_file.c_str(), std::ios::out | std::ios::binary);
	for (int j = 0; j < cloud->size(); j++) {

		myFile.write((char*)& cloud->at(j).x, sizeof(cloud->at(j).x));
		myFile.write((char*)& cloud->at(j).y, sizeof(cloud->at(j).y));
		myFile.write((char*)& cloud->at(j).z, sizeof(cloud->at(j).z));
		if(flag == 0){
			myFile.write((char*)& cloud->at(j).rgb, sizeof(cloud->at(j).rgb));	
		}else{
			unsigned long rgb = *reinterpret_cast<int*>(&cloud->at(j).rgb);
			//unsigned long rgb =15161693;
			long r = (rgb>>16)&0x0000ff;
			float intensity =(static_cast<float>(r))/255;
			
			long g = (rgb>>8)&0x0000ff;
			long b = (rgb)&0x0000ff;
			long re = r*65536;
			long rw =(re>>16)&0x0000ff;

			long rr = intensity*65536*255;
			//float rr = (rr>>16)&0x0000ff;
			if(j == 0){
				std::cout << "r:" << r << std::endl;
				std::cout << "b:" << b << std::endl;
				std::cout << "g:" << g << std::endl;

				std::cout << "rgb:" << rgb << std::endl;
				std::cout << "intensity:" << intensity << std::endl;
				std::cout << "r*65536:" << r*65536 << std::endl;
				std::cout << "rgb>>16:" << (rgb>>16) << std::endl;
				std::cout << "rw:" << rw << std::endl;
				std::cout << "rr:" << rr << std::endl;
				std::cout << "x:" << cloud->at(j).x << std::endl;
				std::cout << "y:" << cloud->at(j).y << std::endl;
				std::cout << "z:" << cloud->at(j).z << std::endl;
				std::cout << "i:" << cloud->at(j).rgb << std::endl;
				//std::cout << "rr:" << rr << std::endl;
			}
			myFile.write((char*)& intensity, sizeof(intensity));		
		}
		/*		
		if(j <= 10){
			

			unsigned long rgb = *reinterpret_cast<int*>(&cloud->at(j).rgb);
			int r = (rgb>>16)&0x0000ff;
			int g = (rgb>>8)&0x0000ff;
			int b = (rgb)&0x0000ff;
			float intensity =(static_cast<float>(r))/255;
			std::cout << r << std::endl;
			std::cout << g << std::endl;
			std::cout << b << std::endl;
			std::cout << intensity << std::endl;
			//std::cout << cloud->at(j).rgb << std::endl;
			//myFile << cloud->at(j).x << " " << cloud->at(j).y << " " << cloud->at(j).z << endl;
		}
		*/
	}
	std::cout << flag << std::endl;
	myFile.close();
}





