#!/bin/bash
source /home/chenwei/anaconda3/etc/profile.d/conda.sh
conda activate opencood
python opencood/visualization/vis_data_sequence.py
python pcdToGif.py

