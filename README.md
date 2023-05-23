# Test-System-for-Collaborative-Perception

This system is a test system for the collaborative perception model of the Internet of Vehicles. The system amplifies the point cloud data to make it have extreme weather characteristics such as rain, snow, fog, etc., and then uses it as a test case to test the prediction ability of the Internet of Vehicles collaborative perception model under extreme weather conditions.


## Repository Structure

  + the `server` folder saves server code，Various businesses can be implemented through server codes
  + the `amis-admin-master` folder saves web engineering codes
  + the `test_pcl` folder saves codes related to point cloud data amplification
  + the `opencood` folder saves files and codes related to the collaborative perception model
 
  + README.md


## Environment

+ Ubuntu Env
  + The ubuntu environment is ubuntu 18.04

+ Python Env
  + The version of the python environment is Python3.7.11

+ Mysql Env
  + The database environment is mysql 8.0.32

+ Other software and dependencies
  CUDA11.4 、Pytorch1.110+cu113 、Spring Boot2.0 、JDK1.7.0、
  Cmake 3.26.1 、Spconv 1.2.1 、PCL1.9.1
  



## Code Usage

Before using the system, you need to install the opencood framework, the installation tutorial is shown in the website link

https://opencood.readthedocs.io/en/latest/md_files/installation.html

After the installation is complete, add or replace the opencood files in this project to the generated opencood folder

The download address of the dataset and other models is

https://mobility-lab.seas.ucla.edu/opv2v/

After installing the opencood framework, you need to install the pcl library. The installation tutorial link is as follows

https://blog.csdn.net/qq_42257666/article/details/124574029

After pcl is installed, you need to start the background program server first. Use IntelliJ IDEA to open the project code, first download the maven dependencies, and then start the program directly

After the server startup is complete, enter the front-end project directory, use the `npm install` command related dependencies, and then use `npm start` to start the front-end server, open the browser and enter the given URL

After that, we can use the test system to test the collaborative perception model

