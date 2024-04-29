# Java Grep App

## Introduction
The Java Grep App is a text search utility that searches for a regex pattern in a directory and outputs matched lines to a file. Designed with modularity and scalability in mind, it provides an interface `JavaGrep` and two implementations: `JavaGrepImp` uses traditional Java IO, and `JavaGrepLambdaImp` leverages **Java 8's lambda and Stream APIs**. The app is built with **Maven**, packaged with **Docker** for easy distribution, version controlled with **Git**, and developed using **IntelliJ IDEA**.

## Quick Start
To use the app, compile the source code and run the Java program with three arguments: regex, the path to search, and the output file path. For example:
```sh
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepLambdaImp ".*Exception.*" "./data" "./out/grep.out"
```

## Implementation
### Pseudocode
```dtd
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

### Performance Issue
The application may face memory issues when dealing with large files, as it tries to hold all lines in memory. To resolve this, Stream API can be used to process lines on-the-fly, thereby reducing the memory footprint.

## Test
Testing was done manually by creating a sample data directory with text files, running the app to search for specific patterns, and verifying the output file for correct matching lines.

## Deployment
The Java Grep App can be easily deployed using Docker. Here's how to do it:

1. **Build the Docker Image**:
```sh
docker build -t your_docker_id/grep-app .
```
2. **Run the App in a Docker Container**:
```sh
docker run --rm -v $(pwd)/data:/data -v $(pwd)/log:/log your_docker_id/grep-app .*Exception.* /data /log/grep.out
```
Ensure that you have the data directory with the files to search through and an empty log directory in your current directory before running the command above.

Replace `your_docker_id` with your Docker Hub username, and adjust the regex pattern, input data path, and output file path as needed.

## Improvement
- **Memory Optimization:** Implement a more memory-efficient approach for processing large files, possibly by handling lines in a streamed manner.
- **Refactoring for Resources Management:** Apply try-with-resources across the app to ensure that all resources are properly closed and to reduce the risk of resource leaks.
- **Enhanced Lambda and Stream Utilization:** Continue refactoring to exploit lambda expressions and streams more extensively for cleaner and more efficient code.
