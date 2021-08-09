package com.closer.closerwithrestapi;

import closer.exceptions.CloserException;
import closer.main.VCSType;
import closer.models.dsl.Revision;
import closer.parsers.Parser;
import closer.parsers.ParserSelector;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class Controller {
    @RequestMapping("/hello")
    public String helloPage() {
        return "Hello, this is Kasap!";
    }

    @RequestMapping("/")
    public String startPage() {
        return "START PAGE - KASAP";
    }

    @RequestMapping(method = RequestMethod.POST, value = "convert/{inputFormat}/{outputFormat}")
    public ResponseEntity<String> convert(@PathVariable String inputFormat, @PathVariable String outputFormat, @RequestBody String textLog)
    {
        ArrayList<String> textLogLines = new ArrayList<>(Arrays.asList(textLog.split("\\r?\\n|\\r")));
        ParserSelector parserSelector = new ParserSelector();

        VCSType inType;
        try {
            inType = VCSType.valueOf(inputFormat);
        } catch (IllegalArgumentException | NullPointerException exception) {
            String exceptionMessage = "Invalid input format in path - " + inputFormat;
            return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
        }

        Parser inParser = parserSelector.selectParser(inType);
        List<Revision> revisions = new ArrayList<>();
        revisions.addAll(inParser.parseInputToFormat(textLogLines));

        VCSType outType;
        try {
            outType = VCSType.valueOf(outputFormat);
        } catch (IllegalArgumentException | NullPointerException exception) {
            String exceptionMessage = "Invalid output format in path - " + outputFormat;
            return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
        }

        Parser outParser = parserSelector.selectParser(outType);
        String outputString = outParser.parseRevisionsToOutputFormat(revisions);

        String returnMessage = "Conversion: " + inputFormat + " to " + outputFormat + '\n' +
                                "---------------------------------------------------" + '\n' +
                                outputString;

        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }
}
