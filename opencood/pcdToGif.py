import cv2
import glob
import os
import sys
import math
import random
import csv
import shutil
import imageio
import time
from PIL import Image

def find_files(path, extension):
    file_paths = []
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(extension):
                file_paths.append(os.path.join(root, file))
    return file_paths

def convertToGif(input_path):
    images = []
    png_files = find_files(input_path, '.png')
    png_files.sort()
    i = 0
    # 按顺序打开一系列图片
    for filename in png_files:
        img = Image.open(filename)
        images.append(img)
        print(filename)
        # i += 1
        # if i >= 115:
        #     break
    
    files = os.listdir("/home/chenwei/amis/amis-admin-master/pages/resource/visual")   # 读入文件夹
    num_png = len(files)
    # 将图片转换为GIF动图并保存
    images[0].save('/home/chenwei/amis/amis-admin-master/pages/resource/visual/' + str(num_png)+".gif", save_all=True, append_images=images[1:], duration=1500, loop=0)
    print('/home/chenwei/amis/amis-admin-master/pages/resource/visual/' + str(num_png)+".gif")

if __name__ == '__main__':
    convertToGif('/home/chenwei/test_pcl/visual/')
