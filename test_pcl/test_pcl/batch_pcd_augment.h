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
#include <pcl/compression/octree_pointcloud_compression.h>
#include <string>
#include "one_pcd_augment.h"

using namespace std;
int init[20] = {0,1,2,3,4,5,6,7,8,9,10 };
void batch_pcd_augment(vector<string> filenameList_car1, vector<string> filenameList_car2 , string type,int step ,vector<string> file_name,int weather,double weatherInt,double reduce) {
	int parameter = 0;
	string file_= "00";
	for (int i = 1; i <= step; i++)
	{
		int a1 = i % 10;
		int b1 = i / 10;
		file_[1] = char(a1+'0');
		file_[0] = char(b1+'0');
		parameter = init[i];
		int num = 1;
		
		for (vector<string>::iterator iter_car1 = filenameList_car1.begin(),
		 iter_car2 = filenameList_car2.begin(),
		iter_pcd = file_name.begin(); iter_car1 != filenameList_car1.end(); ++iter_car1 , iter_pcd++ , ++iter_car2){
			cout << "index:";
			cout << num << endl;
			num++;
			const char* filename = iter_car1->c_str();
			string s1_car1 = (*iter_car1);
			string s1_car2 = (*iter_car2);
			//Open a corresponding pcd file according to the pcd file name in the pcd file list
			pcl::PointCloud<pcl::PointXYZI>::Ptr original_pcd_car1(new pcl::PointCloud<pcl::PointXYZI>);
			pcl::PointCloud<pcl::PointXYZI>::Ptr original_pcd_car2(new pcl::PointCloud<pcl::PointXYZI>);
		
			if (pcl::io::loadPCDFile<pcl::PointXYZI>(s1_car1, *original_pcd_car1) == -1)
			{
				PCL_ERROR("Couldn't read file pcd\n");
			}
			if (pcl::io::loadPCDFile<pcl::PointXYZI>(s1_car2, *original_pcd_car2) == -1)
			{
				PCL_ERROR("Couldn't read file pcd\n");
			}		
			//pcl::PointCloud<pcl::PointXYZI>::Ptr augment_pcd = one_pcd_augment(original_pcd_car1,original_pcd_car2 , type, parameter,(*iter_car1));//one pcd augmentation
			pair <pcl::PointCloud<pcl::PointXYZI>::Ptr, pcl::PointCloud<pcl::PointXYZI>::Ptr> augment_pcd_all = one_pcd_augment(original_pcd_car1,original_pcd_car2 , type, parameter,(*iter_car1),weather,weatherInt,reduce);//one pcd augmentation
			pcl::PointCloud<pcl::PointXYZI>::Ptr augment_pcd = augment_pcd_all.first;
			pcl::PointCloud<pcl::PointXYZI>::Ptr augment_pcd_car2 = augment_pcd_all.second;
			string defaultPath = "/home/chenwei/test_pcl/augmentation_data/default/"+type;
			if (0 != access(defaultPath.c_str(), 0))mkdir(defaultPath.c_str(),S_IRWXU);   //One folder for each step
			string car1_Path = defaultPath+"/car1";
			if (0 != access(car1_Path.c_str(), 0))mkdir(car1_Path.c_str(),S_IRWXU);   //One folder for each step
			string car2_Path = defaultPath+"/car2";
			if (0 != access(car2_Path.c_str(), 0))mkdir(car2_Path.c_str(),S_IRWXU);   //One folder for each step
			
			string car1_folderPath = car1_Path +"/"+ file_;
			if (0 != access(car1_folderPath.c_str(), 0))mkdir(car1_folderPath.c_str(),S_IRWXU);  //Modify the augmentation file name
			string car2_folderPath = car2_Path +"/"+ file_;
			if (0 != access(car2_folderPath.c_str(), 0))mkdir(car2_folderPath.c_str(),S_IRWXU);  //Modify the augmentation file name

			string s2= defaultPath + "/car1/" + file_+"/"+(*iter_pcd);
			string s2_car2= defaultPath + "/car2/" + file_+"/"+(*iter_pcd);
			cout << s2 << endl;			
			s2.replace(s2.length() - 4, s2.length(), ".bin");
			s2_car2.replace(s2_car2.length() - 4, s2_car2.length(), ".bin");
			cout << s2 << endl;
			cout << s2.c_str() << endl;	
			/*cout << defaultPath << endl;
			cout << file_ << endl;
			cout << s2.c_str() << endl;
			cout << s2_car2.c_str() << endl;*/
			std::ofstream myFile(s2.c_str(), std::ios::out | std::ios::binary);
			for (int j = 0; j < augment_pcd->size(); j++) {

				myFile.write((char*)& augment_pcd->at(j).x, sizeof(augment_pcd->at(j).x));
				myFile.write((char*)& augment_pcd->at(j).y, sizeof(augment_pcd->at(j).y));
				myFile.write((char*)& augment_pcd->at(j).z, sizeof(augment_pcd->at(j).z));
				myFile.write((char *)& augment_pcd->at(j).intensity, sizeof(augment_pcd->at(j).intensity));
			}
			std::ofstream myFile2(s2_car2.c_str(), std::ios::out | std::ios::binary);
			for (int j = 0; j < augment_pcd_car2->size(); j++) {

				myFile2.write((char*)& augment_pcd_car2->at(j).x, sizeof(augment_pcd_car2->at(j).x));
				myFile2.write((char*)& augment_pcd_car2->at(j).y, sizeof(augment_pcd_car2->at(j).y));
				myFile2.write((char*)& augment_pcd_car2->at(j).z, sizeof(augment_pcd_car2->at(j).z));
				myFile2.write((char *)& augment_pcd_car2->at(j).intensity, sizeof(augment_pcd_car2->at(j).intensity));
			}
			//system("pause");
			cout << "bin has generated" << endl;
			//system("pause");
			
		}
	}
}

