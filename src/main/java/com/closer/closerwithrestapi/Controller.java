package com.closer.closerwithrestapi;

import org.springframework.web.bind.annotation.*;

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
    public String convert(@PathVariable String inputFormat, @PathVariable String outputFormat, @RequestBody String textLog) {
        return "Conversion: " + inputFormat + " to " + outputFormat + '\n' + textLog;
    }
}
