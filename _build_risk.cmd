@echo off

echo Building risk analysis report
pdflatex risk-analysis\full
pdflatex risk-analysis\full
pdflatex risk-analysis\full

echo Opening output file
start "" full.pdf

echo Cleaning output files
del full.aux
del full.toc