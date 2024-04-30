package ca.jrvs.apps.grep;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.*;

public class JavaGrepLambdaImp extends JavaGrepImp{

    @Override
    public List<File> listFiles(String rootDir) {
        try (Stream<Path> fileStream = Files.walk(Paths.get(rootDir))) {
            return fileStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed to list files from directory: " + rootDir, e);
            return null;
        }
    }

    @Override
    public List<String> readLines(File inputFile) {
        if (!inputFile.isFile()) {
            throw new IllegalArgumentException("The provided file path does not denote a file: " + inputFile);
        }

        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed to read lines from file: " + inputFile, e);
            throw new RuntimeException("Failed to read lines from file: " + inputFile, e);
        }
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        Path outputPath = Paths.get(getOutFile());
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            lines.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

}
