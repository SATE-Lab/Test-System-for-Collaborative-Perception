#!/bin/bash
source /home/chenwei/anaconda3/etc/profile.d/conda.sh
conda activate opencood
python opencood/tools/inference.py --model_dir '/home/chenwei/opencood/OpenCOOD/opencood/models/v2vnet' --fusion_method 'intermediate' --save_vis
