# closer-rest
CLOSER program with REST API - Dockerized

How to use the program:

This is a program for converting text logs from one of the following input formats: GIT, SVN, HG (mercurial), all of which are software version control systems, to the following output formats: GIT, SVN, HG (mercurial), CLOSER (a custom 'universal' text-log format). To use the program send a HTTP POST request to in the following format: IPADRESS:PORT/convert/INPUTFORMAT/OUTPUTFORMAT. Possible options for INPUTFORMAT are GIT, SVN, HG. Possible options form OUTPUTFORMAT are: GIT, SVN, HG, CLOSER. In the POST request body insert your text-log as one full string. The text-log must match the appropriate input format. Errors will occur if: input path is invalid, output path is invalid, text-log is not in appropriate format.

If source code is run on local machine, route to program will be - localhost:8080/convert/INPUTFORMAT/OUTPUTFORMAT

Contains Dockerfile - ready to be deployed as a docker-container
