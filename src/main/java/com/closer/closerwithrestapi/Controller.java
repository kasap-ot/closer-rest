package com.closer.closerwithrestapi;

import closer.main.VCSType;
import closer.models.dsl.Revision;
import closer.parsers.Parser;
import closer.parsers.ParserSelector;
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
    public String convert(@PathVariable String inputFormat, @PathVariable String outputFormat, @RequestBody String textLog)
    {
        List<String> textLogLines = Arrays.asList(textLog.split("\\n"));
        ParserSelector parserSelector = new ParserSelector();

        VCSType inType = VCSType.valueOf(inputFormat);
        Parser inParser = parserSelector.selectParser(inType);
        List<Revision> revisions = new ArrayList<>();
        revisions.addAll(inParser.parseInputToFormat(textLogLines)); // Here there was a problem, had to remove one line from source code

        VCSType outType = VCSType.valueOf(outputFormat);
        Parser outParser = parserSelector.selectParser(outType);
        String outputString = outParser.parseRevisionsToOutputFormat(revisions);

        return "Conversion: " + inputFormat + " to " + outputFormat + '\n' +
                "---------------------------------------------------" + '\n' +
                outputString;
    }
}
