package closer.main;

import closer.exceptions.CloserErrorCode;
import closer.exceptions.CloserException;
import closer.parsers.ParserSelector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.ZonedDateTime;

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
        } catch (CloserException ex){
            //Catch all closer exceptions and display appropriately
            log.error("A CLOSER exception has occurred during execution. Details ="+ex.toString());
        } catch (Exception ex){
            //Catch all finally to prevent tool crashing
            log.error(CloserErrorCode.INTERNAL_CLOSER_EXCEPTION.toString());
        }
        log.info("Finished CLOSER command line tool execution");
    }
}

