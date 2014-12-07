rm -rf input/Assignment/*.png
rm -rf input/JPEG/*.png
rm -rf input/PDF/*.png
rm -rf input/WORD/*.png

python analysis.py input/JPEG/
python analysis.py input/PDF/
python analysis.py input/WORD/
# python analysis.py input/JPEG/
