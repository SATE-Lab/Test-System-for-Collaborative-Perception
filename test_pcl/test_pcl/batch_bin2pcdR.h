#include <pcl/visualization/cloud_viewer.h>
#include <iostream>
#include <pcl/io/io.h>
#include <pcl/io/pcd_io.h>
#include <pcl/io/ply_io.h>
#include <pcl/point_types.h>
#include <boost/random.hpp>
#include <fstream>  
#include <string>  
#include <vector> 
#include <pcl/features/integral_image_normal.h>
#include <pcl/features/normal_3d.h>
#include "bin2pcdR.h"
using namespace std;
void batch_bin2pcdR(vector<string> ori_bin_file, vector<string> ori_pcd_file ,int flag) {
	for (auto iter_bin = ori_bin_file.begin(), iter_pcd = ori_pcd_file.begin(); iter_bin != ori_bin_file.end(); iter_bin++, iter_pcd++) {
		const char* filename = iter_bin->c_str();
		cout << filename << endl;
		string tmp = iter_pcd->c_str();
		string outfilename;
		if(flag == 1){
			outfilename = "/home/chenwei/test_pcl/extract_intensity_data/car1/" + tmp;		
		}else if(flag == 2){
			outfilename = "/home/chenwei/test_pcl/extract_intensity_data/car2/" + tmp;
		}else{
			outfilename = iter_bin->c_str();
			outfilename.replace(outfilename.length() - 4, outfilename.length(), ".pcd");	
		}
		bin2pcdR(filename, outfilename.c_str(),flag);
	}
}
