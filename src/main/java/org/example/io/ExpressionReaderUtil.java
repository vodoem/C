package org.example.io;

import java.nio.file.Files;
import java.nio.file.Path;

public class ExpressionReaderUtil {
    public static String readExpression(String filePath) throws Exception {
        return Files.readString(Path.of(filePath));
    }
}
