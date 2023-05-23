# Test-System-for-Collaborative-Perception
车联网协同感知测试系统
amis-admin-master 是前端工程代码，进入工程目录，使用npm start可以直接启动前端工程。(使用之前需要先使用npm install安装组件，然后使用npm start启动前端工程)

server是后端代码，启动前端代码之前，需要先启动后台程序，否则前端无法请求到数据，也无法执行业务。

test_pcl师数据扩增代码，使用之前需要先通过Cmakelists对程序代码进行编译。server后台会自动调用test_pcl的编译结果。

最后安装opencood框架，安装教程参考 https://opencood.readthedocs.io/en/latest/md_files/installation.html 网站。安装完毕后会生成opencood文件夹。server会自动调用该文件夹中的模型进行模型检测。
