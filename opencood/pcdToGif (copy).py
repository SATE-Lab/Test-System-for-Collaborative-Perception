import os
import imageio

frames = []
for image_name in os.listdir("/home/chenwei/test_pcl/visual"): # 读取image下的图片名称
    image_name = "/home/chenwei/test_pcl/visual/" + image_name # 绝对路径
    print(image_name)
    frames.append(imageio.imread(image_name))
imageio.mimsave("/home/chenwei/test_pcl/visual/"+num_png+".gif", frames, 'GIF', duration=2)
