package org.logtalk.parser;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.UUID;

public class ParserUtil {

    private static File tmpFile() throws IOException {
        return File.createTempFile(UUID.randomUUID().toString(), ".tmp");
    }

    public static File fileWithContent(String text) {
        File tmpFile = null;
        try {
            tmpFile = tmpFile();
            Files.write(tmpFile.toPath(), text.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return tmpFile;
    }

}
