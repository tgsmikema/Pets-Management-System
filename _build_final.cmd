@echo off

echo Building full final report
pdflatex final-report\full
pdflatex final-report\full
pdflatex final-report\full

echo Opening output file
start "" full.pdf

echo Cleaning output files
del full.aux
del full.toc