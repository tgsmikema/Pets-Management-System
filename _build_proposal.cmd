@echo off

echo Building full proposal
pdflatex proposal\full
pdflatex proposal\full
pdflatex proposal\full

echo Opening output file
start "" full.pdf

echo Cleaning output files
del full.aux
del full.toc