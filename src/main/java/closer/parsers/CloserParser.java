package closer.parsers;

import closer.models.dsl.Author;
import closer.models.dsl.FileChange;
import closer.models.dsl.Revision;
import closer.main.Main;

import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author : Jordan
 * @created : 12/01/2021, Tuesday
 * @description : Parser to convert CLOSER json back to in memory objects
 **/
public class CloserParser implements Parser {

    @Override
    public List<Revision> parseInputToFormat(List<String> linesOfInput) {
        String input = String.join("", linesOfInput);
        return Main.GSON.fromJson(input, new TypeToken<List<Revision>>(){}.getType());
    }

    @Override
    public Revision extractDetailsAtRevision(List<String> linesOfRevision, int revisionStart) {
        return null;
    }

    @Override
    public FileChange extractFileChange(String fileChange) {
        return null;
    }

    @Override
    public Author extractAuthorDetails(String authorString) {
        return null;
    }

    @Override
    public String parseRevisionsToOutputFormat(List<Revision> revisions) {
        return Main.GSON.toJson(revisions);
    }

    @Override
    public List<String> parseFileChangesToOutputFormat(List<FileChange> fileChanges) {
        return null;
    }
}

