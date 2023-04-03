# Capstone Project Template

[![Build Risk Analysis Report](../../actions/workflows/build_risk.yml/badge.svg)](../../actions/workflows/build_risk.yml)
[![Build Project Proposal](../../actions/workflows/build_proposal.yml/badge.svg)](../../actions/workflows/build_proposal.yml)
[![Build Project Final Report](../../actions/workflows/build_final.yml/badge.svg)](../../actions/workflows/build_final.yml)

This repository contains a template for your capstone project. It includes the files for the project project and final reports, plus an area for you to store your code and other artifacts.

## Structure

The repository has the following structure:

- [final-report](final-report): contains the files for the final report ([see Canvas assignment](https://canvas.auckland.ac.nz/courses/91591/assignments/315400)).
- [images](images): stores all the images for the reports (both reports).
- [project](project): a folder for storing all project artifacts, including source code.
- [proposal](proposal): contains the files for the project proposal report ([see Canvas assignment](https://canvas.auckland.ac.nz/courses/91591/assignments/315398)).
- [risk-analysis](risk-analysis): contains the files for the risk analysis report ([see Canvas assignment](https://canvas.auckland.ac.nz/courses/91591/assignments/315395)).
- [capstone.cls](capstone.cls): LaTeX documentation template for the reports.
- [team.tex](team.tex): LaTeX file for storing your team name and members.

## Extra details

Modify [team.tex](team.tex) in this folder to contain the name of your team and the team members. You **MUST** modify this file to include the details of your team.

**DO NOT** modify capstone.cls. This file is automatically replaced as part of the build process.

GitHub will automatically build your reports when you push any changes to the risk-analysis, porposal, or final-report folders.

## Build Scripts

This repository contains some of the scripts that we use in our build process. These can only be used on a Windows machine. They also also require a LaTeX installation on your machine (we use [MiKTeX](https://miktex.org/).)

The three build scripts are:
- [_build_risk.cmd](_build_risk.cmd): this script will build the risk analysis report.
- [_build_proposal.cmd](_build_risk.cmd): this script will build the full version of your project proposal.
- [_build_final.cmd](_build_risk.cmd): this script will build the full version of your report.

We have also added a Visual Studio Code tasks definition file. This file allows you to build the different reports from within Visual Studio Code by pressing _Ctrl-Shift-B_ and selecting the report to generate.

## Included LaTeX libraries

The style file contains the following LaTeX libraries (packages):
- appendix
- avant
- emptypage
- fontenc
- geometry
- graphicx
- import
- inputenc
- mathptmx
- microtype
- parskip
- titlesec
- xcolor

**DO NOT** add any additional packages without checking with the course coordinator first. We use an automated build process for generating the final submissions for your assignments. If you add additional, untested packages, you may break the build process. Typically, we will fix the problem by deleting the offending lines of code, which will give you an unexpected output for your reports!
