#include <pcl/visualization/cloud_viewer.h>
#include <iostream>
#include <pcl/io/io.h>
#include <pcl/io/pcd_io.h>
#ifdef __unix
#define fopen_s(pFile,filename,mode) ((*(pFile))=fopen((filename),  (mode)))==NULL
#endif
#include <pcl/io/ply_io.h>
#include <pcl/point_types.h>
#include <boost/random.hpp>
#include <fstream>  
#include <string>  
#include <vector> 
#include <pcl/features/integral_image_normal.h>
#include <pcl/features/normal_3d.h>
using namespace std;
//Batch conversion, filenameList is used to save the file name
void bin2pcdR(const char *filenameInput, const char *filenameOutput ,int flag) {
	size_t num = 1000000;
	float *data = (float*)malloc(num * sizeof(float));
	float *px = data + 0;
	float *py = data + 1;
	float *pz = data + 2;
	float *pr = data + 3;//intensity
	FILE *stream;
	fopen_s(&stream, filenameInput, "rb");
	num = fread(data, sizeof(float), num, stream) / 4;
	fclose(stream);
	FILE *writePCDStream;
	fopen_s(&writePCDStream, filenameOutput, "wb");
	fprintf(writePCDStream, "VERSION 0.7\n");
	if(flag == 0){
		fprintf(writePCDStream, "FIELDS x y z rgb\n");
	}else{
		fprintf(writePCDStream, "FIELDS x y z intensity\n");
	}

	fprintf(writePCDStream, "SIZE 4 4 4 4\n");
	if(flag == 0){
		fprintf(writePCDStream, "TYPE F F F U\n");
	}else{
		fprintf(writePCDStream, "TYPE F F F F\n");
	}
	fprintf(writePCDStream, "WIDTH %d\n", num);
	fprintf(writePCDStream, "HEIGHT 1\n");
	fprintf(writePCDStream, "POINTS %d\n", num);
	fprintf(writePCDStream, "DATA ascii\n");
	for (int i = 0; i < num; i++)
	{
		if(flag == 0){
			if( i == 0){
				cout << "*pr:" << *pr <<endl;
			}
			*pr= (*pr)*255;
			*pr= (*pr)*65536;
			if( i == 0){
				cout << "*pr:" << *pr <<endl;
			}
			fprintf(writePCDStream, "%f %f %f %f\n", *px, *py, *pz, *pr);
			px += 4; py += 4; pz += 4; pr += 4;
		}else{
			fprintf(writePCDStream, "%f %f %f %f\n", *px, *py, *pz, *pr);
			px += 4; py += 4; pz += 4; pr += 4;
		}
	}
	fclose(writePCDStream);
}
