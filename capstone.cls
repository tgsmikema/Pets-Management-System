\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{capstone}[2023/02/27 ECSE Capstone Project Report Template]

\LoadClass[a4paper]{report}

\RequirePackage{appendix}
\RequirePackage{emptypage}
\RequirePackage[usenames, svgnames, table]{xcolor}
\RequirePackage{import}
\RequirePackage[utf8]{inputenc}
\RequirePackage[T1]{fontenc}
\RequirePackage[a4paper, margin=2.5cm]{geometry}
\RequirePackage{avant}
\RequirePackage{mathptmx}
\RequirePackage{microtype}
\RequirePackage{parskip}
\RequirePackage{graphicx}
\RequirePackage{titlesec}
\graphicspath{{images/}}

\titlespacing*{\chapter}{0pt}{10pt}{10pt}

\titleformat{\chapter}[display]
  {\normalfont\sffamily\bfseries}
  {\chaptertitlename\ \thechapter}{0pt}{\Large}

\definecolor{engineeringPurple}{RGB}{76, 33, 119} 

\renewcommand{\titlepage}[2]{
	\thispagestyle{empty}

	\begin{tikzpicture}[remember picture, overlay]
		\node [inner sep=0pt] at (current page.center) {#1};
		\node [anchor=center, inner sep=1.25cm, rectangle, fill=white, fill opacity=0.6, text opacity=1, minimum height=0.2\paperheight, minimum width=\paperwidth, text width=0.8\paperwidth] at (current page.center) {#2};
	\end{tikzpicture}
	
	\newpage
}

\include{team.tex}

\RequirePackage{tikz}
