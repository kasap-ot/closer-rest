package closer.main;

import closer.exceptions.CloserErrorCode;
import closer.exceptions.CloserException;
import closer.models.dsl.Revision;
import closer.parsers.ParserSelector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.*;

public class Main {

    public static final String APPLICATION_VERSION = "0.0.0.1";
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
        @Override
        public void write(JsonWriter out, ZonedDateTime value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public ZonedDateTime read(JsonReader in) throws IOException {
            return ZonedDateTime.parse(in.nextString());
        }
    }).enableComplexMapKeySerialization().create();
    public static final ParserSelector PARSER_SELECTOR = new ParserSelector();
    private static Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args){
        log.info("Starting CLOSER command line tool execution");
        try{
            //Try to run the main method with catch for exception to print appropriate message
            mainWithoutExceptionHandling(args);
        } catch (CloserException ex){
            //Catch all closer exceptions and display appropriately
            log.error("A CLOSER exception has occurred during execution. Details ="+ex.toString());
        } catch (Exception ex){
            //Catch all finally to prevent tool crashing
            log.error(CloserErrorCode.INTERNAL_CLOSER_EXCEPTION.toString());
        }
        log.info("Finished CLOSER command line tool execution");
    }

    public static void mainWithoutExceptionHandling(String[] args){ /*
        Options options = generateCommandLineOptions();

        log.info("Checking for help or version parameters first");
        checkForHelpOrVersionParameters(args, options);

        log.info("Parsing all command line arguments, validating that all required arguments are present");
        CommandLine commandLine = parseAndValidateCommandLineArguments(args, options ,false);

        ProgramArguments programArguments = extractProgramArguments(commandLine);
        File inputFile = getInputFileAndVerifyPermissions(programArguments.getInputFileLocation());
        File outputFile = getOutputFileAndVerifyPermissions(programArguments.getOutputFileLocation(), programArguments.isAppend(), programArguments.isOverwrite());
        List<String> lines = readFile(programArguments.getInputFileLocation(), inputFile);

        if (programArguments.isPrintInput()) {
            log.info("Printing Input to Standard Output");
            for (String s : lines) {
                System.out.println(s);
            }
            System.out.println("\n---END OF INPUT---\n");
        }

        //If the append parameter is sent then the required process depends on the format of the output
        //for the json closer format the output file must be read in full and then have the list of revisions appended to
        //for all other formats as the outputs are plain text files the output can simply be written without any additional file reading

        List<Revision> revs = new ArrayList<>();
        if (outputFile != null && programArguments.isAppend() && programArguments.getOutputVCSType().equals(VCSType.CLOSER)){
            log.info("Appending to JSON file when output type is CLOSER, Reading previous JSON file content");
            List<String> previousOutputLines = readFile(programArguments.getOutputFileLocation(), outputFile);
            log.info("Deleting previous JSON file, so that appended version can be written to a new file");
            outputFile = getOutputFileAndVerifyPermissions(programArguments.getOutputFileLocation(), programArguments.isAppend(), true);
            revs.addAll(PARSER_SELECTOR.selectParser(VCSType.CLOSER).parseInputToFormat(previousOutputLines));
        }

        log.info("Select VCS Parser based on inputType program argument and parse to CLOSER objects");
        revs.addAll(PARSER_SELECTOR.selectParser(programArguments.getInputVCSType()).parseInputToFormat(lines));
        log.info("Select VCS Parser based on outputType program argument and parse to output format");
        String jsonString = PARSER_SELECTOR.selectParser(programArguments.getOutputVCSType()).parseRevisionsToOutputFormat(revs);

        log.info("Write formatted output to file");
        writeOutput(programArguments, outputFile, jsonString); */
    }

    /* private static void checkForHelpOrVersionParameters(String[] args, Options options) {} */

    private static void writeOutput(ProgramArguments programArguments, File outputFile, String jsonString) {
        if (outputFile != null) {
            try {
                FileWriter fileWriter = new FileWriter(outputFile, programArguments.isAppend());
                //If in append mode add a line break to make the file formatting
                if (programArguments.isAppend()){
                    fileWriter.write("\n");
                }
                fileWriter.write(jsonString);
                fileWriter.close();
            } catch (IOException e) {
                Map<String, Object> map = new HashMap<>();
                map.put("fileLocation", programArguments.getOutputFileLocation());
                throw new CloserException(CloserErrorCode.CANNOT_WRITE_OUTPUT_FILE, map);
            }
        } else {
            System.out.println(jsonString);
        }
    }

    /* private static List<String> readFile(String fileLocation, File inputFile) {
        List<String> lines;
        try {
            lines = Files.readAllLines(inputFile.toPath());
        } catch (IOException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("fileLocation", fileLocation);
            throw new CloserException(CloserErrorCode.CLOSER_CANNOT_READ_FILE, map);
        }
        return lines;
    } */

    private static File getInputFileAndVerifyPermissions(String vcsFileLocation) {
        //Check that the file exists and that the user has at least read permission on it
        File inputFile = new File(vcsFileLocation);
        //If the file does not exist or cannot be read then error
        if (!inputFile.isFile() || !inputFile.canRead()) {
            Map<String, Object> map = new HashMap<>();
            map.put("fileLocation", vcsFileLocation);
            throw new CloserException(CloserErrorCode.CLOSER_CANNOT_READ_FILE, map);
        }
        return inputFile;
    }

    private static File getOutputFileAndVerifyPermissions(String outputFileLocation, boolean append, boolean overwrite) {
        if (outputFileLocation == null) {
            //Return a null file that will cause the response to be sent to standard output
            return null;
        }
        File outputFile = new File(outputFileLocation);
        if (overwrite) {
            if (outputFile.exists()) {
                if (!outputFile.delete()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileLocation", outputFileLocation);
                    throw new CloserException(CloserErrorCode.CANNOT_OVERWRITE_OUTPUT_FILE, map);
                }
                outputFile = new File(outputFileLocation);
            }
        } else if (!append){
            //Check that the file exists and if it does error
            if (outputFile.exists()) {
                Map<String, Object> map = new HashMap<>();
                map.put("fileLocation", outputFileLocation);
                throw new CloserException(CloserErrorCode.CANNOT_WRITE_OUTPUT_FILE, map);
            }
        }
        return outputFile;
    }

    /* public static CommandLine parseAndValidateCommandLineArguments(String[] args, Options options, boolean allowForMissingArguments) {} */

    /* private static void printCommandLineHelp(Options options) {} */

    /* public static Options generateCommandLineOptions() {} */

    /* public static ProgramArguments extractProgramArguments(CommandLine commandLine) {} */

    public static VCSType parseVCSTypeFromCommandLineArgument(String argument) {
        try {
            return VCSType.valueOf(argument);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}

