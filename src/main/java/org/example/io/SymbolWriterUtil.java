package org.example.io;

import org.example.symbol.Symbol;
import org.example.symbol.SymbolTable;
import org.example.symbol.SymbolType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SymbolWriterUtil {
    public static void writeSymbols(Path file, SymbolTable table) throws IOException {
        StringBuilder sb = new StringBuilder();
        int id = 1;
        for (Symbol s : table.getAll()) {
            sb.append("<id,").append(id++).append(">").append(" – ").append(s.getName()).append(s.getType() == SymbolType.FLOAT ? ", float" : ", integer").append("\n");
        }
        Files.writeString(file, sb.toString());
    }
}
