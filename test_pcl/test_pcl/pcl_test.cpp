#include <pcl/visualization/cloud_viewer.h>
#include <string>
#include <iostream>
#include <pcl/io/io.h>
#include <pcl/io/pcd_io.h>
#include <pcl/io/ply_io.h>
#include <pcl/point_types.h>
#include <fstream>  
#include <vector> 
#include "pcd2bin.h" 
#include <pcl/features/integral_image_normal.h>
#include <pcl/features/normal_3d.h>
#include "batch_bin2pcdR.h"
#include "batch_pcd_augment.h"
#include"batch_pcd2bin.h"

using namespace pcl;
using namespace io;

void extract_intensity(){
	vector<string> car1_ori_pcd_file;// pcd format file to be augmented
	vector<string> car2_ori_pcd_file;// pcd format file to be augmented
	vector<string> car1_ori_bin_file;//bin format file for original dataset
	vector<string> car2_ori_bin_file;//bin format file for original dataset
	vector<string> file_name;
	ifstream in("/home/chenwei/test_pcl/opv2v_origin_data/data.txt");
	string line;
	string type;
	if (in) 
	{
		getline(in, line);
		type = line;//rain,snow,fog,symmetry,translation,scale,rotation
		cout << type << endl;
		while (getline(in, line))
		{
			cout << line << endl;
			car1_ori_bin_file.push_back("/home/chenwei/test_pcl/opv2v_origin_data/car1/" + line + ".bin");//original training dataset
			car2_ori_bin_file.push_back("/home/chenwei/test_pcl/opv2v_origin_data/car2/" + line + ".bin");//original training dataset
			car1_ori_pcd_file.push_back("/home/chenwei/test_pcl/opv2v_origin_data/car1/" + line+".pcd");
			car2_ori_pcd_file.push_back("/home/chenwei/test_pcl/opv2v_origin_data/car2/" + line+".pcd");
			file_name.push_back(line+".pcd");
		}
	}
	else 
	{
		cout << "no such file" << endl;
		system("pause");
	} 
	batch_pcd2bin(car1_ori_pcd_file,1);
	batch_pcd2bin(car2_ori_pcd_file,1);
	batch_bin2pcdR(car1_ori_bin_file,file_name,1);
	batch_bin2pcdR(car2_ori_bin_file,file_name,2);
}

void augment(int weather,double weatherInt,double reduce){
	vector<string> car1_ori_pcd_file;// pcd format file to be augmented
	vector<string> car2_ori_pcd_file;// pcd format file to be augmented
	vector<string> car1_ori_bin_file;
	vector<string> car2_ori_bin_file;
	vector<string> file_name;
	ifstream in("/home/chenwei/test_pcl/opv2v_origin_data/data.txt");
	string line;
	string type;
	if (in) 
	{
		getline(in, line);
		type = line;//rain,snow,fog,symmetry,translation,scale,rotation
		cout << type << endl;
		while (getline(in, line))
		{
			cout << line << endl;
			car1_ori_bin_file.push_back("/home/chenwei/test_pcl/augmentation_data/default/"+type+"/car1/01/" + line + ".bin");
			car2_ori_bin_file.push_back("/home/chenwei/test_pcl/augmentation_data/default/"+type+"/car2/01/" + line + ".bin");
			car1_ori_pcd_file.push_back("/home/chenwei/test_pcl/extract_intensity_data/car1/" + line+".pcd");
			car2_ori_pcd_file.push_back("/home/chenwei/test_pcl/extract_intensity_data/car2/" + line+".pcd");
			file_name.push_back(line+".pcd");
		}
	}
	else 
	{
		cout << "no such file" << endl;
		system("pause");
	}
	int step = 1;
	batch_pcd_augment(car1_ori_pcd_file, car2_ori_pcd_file, type, step, file_name,weather,weatherInt,reduce);
	batch_bin2pcdR(car1_ori_bin_file,file_name,0);
	batch_bin2pcdR(car2_ori_bin_file,file_name,0);
}


int main(int argc,char *argv[])
{
	int weather =atof(argv[1]);
	double weatherInt =atof(argv[2]);
	double reduce = atof(argv[3]);
	cout << "天气"  << weather << endl;
	cout << "天气强度"  << weatherInt << endl;
	cout << "衰减系数"  << reduce << endl;
	extract_intensity();
	augment(weather,weatherInt,reduce);
	return 0;
}

