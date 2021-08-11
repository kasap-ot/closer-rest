package com.closer.closerwithrestapi;

import closer.exceptions.CloserException;
import closer.main.VCSType;
import closer.models.dsl.Revision;
import closer.parsers.Parser;
import closer.parsers.ParserSelector;
import org.springframework.http.HttpHeaders;
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

        try { inType = VCSType.valueOf(inputFormat); }
        catch (IllegalArgumentException | NullPointerException exception)
        {
            String errorMsg = "Invalid input format in path - " + inputFormat;
            return ResponseEntity.badRequest().body(errorMsg);
        }

        Parser inParser = parserSelector.selectParser(inType);
        List<Revision> revisions = new ArrayList<>();

        revisions.addAll(inParser.parseInputToFormat(textLogLines));
        if (revisions.size() == 0)
        {
            String errorMsg = "Text-log format does not match input format in path - " + inputFormat;
            return ResponseEntity.badRequest().body(errorMsg);
        }

        VCSType outType;

        try { outType = VCSType.valueOf(outputFormat); }
        catch (IllegalArgumentException | NullPointerException e)
        {
            String errorMsg = "Invalid output format in path - " + outputFormat;
            return ResponseEntity.badRequest().body(errorMsg);
        }

        Parser outParser = parserSelector.selectParser(outType);
        String outputString = outParser.parseRevisionsToOutputFormat(revisions);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Response-Message", "Conversion from " + inputFormat + " to " + outputFormat);
        if (outType == VCSType.CLOSER)
            responseHeaders.set("content-type", "application/json");

        return ResponseEntity.ok().headers(responseHeaders).body(outputString);
    }
}
