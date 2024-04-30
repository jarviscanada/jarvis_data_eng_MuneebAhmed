package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep{

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for(File file : listFiles(rootPath)){
            for(String line: readLines(file)){
                if(containsPattern(line)){
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File root = new File(rootDir);
        Queue<File> fileQueue = new LinkedList<>();
        fileQueue.add(root);

        while(!fileQueue.isEmpty()){
            File currentFile = fileQueue.poll();
            File[] files = currentFile.listFiles();
            if(files != null){
                for(File f : files){
                    if(f.isDirectory()){
                        fileQueue.add(f);
                    } else {
                        fileList.add(f);
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        if(!inputFile.isFile()){
            throw new IllegalArgumentException("The provided file path does not denote a file: " + inputFile);
        }
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while((line = reader.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e){
            logger.error("Failed to read lines from file: " + inputFile, e);
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e){
                    logger.error("Failed to close reader", e);
                }
            }
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(new File(getOutFile()));
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);

            for(String line : lines){
                bw.write(line);
                bw.newLine();
            }
        } finally {
            if(bw != null){
                bw.close();
            }
            if(osw != null){
                osw.close();
            }
            if(fos != null){
                fos.close();
            }
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args) {
        if (args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try{
            javaGrepImp.process();
        }catch (Exception ex){
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }

    }
}
