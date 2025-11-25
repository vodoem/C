package org.example.io;

import org.example.symbol.SymbolTable;
import org.example.symbol.SymbolType;
import org.example.token.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TokenWriterUtil {
    public static void writeTokens(Path file, List<Token> tokens, SymbolTable table) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            switch (t.type()) {
                case IDENTIFIER -> {
                    String lex = t.value().toString();
                    sb.append(String.format("<id,%d>\t- идентификатор с именем %s\n",
                            table.get(lex) + 1, lex, table.getSymbol(lex).getType() == SymbolType.FLOAT ? "вещественного типа" : "целого типа"));
                }
                case INTEGER_CONST -> sb.append(String.format("<%s>\t- константа целого типа\n", t.value()));
                case FLOAT_CONST -> sb.append(String.format("<%s>\t- константа вещественного типа\n", t.value()));
                case PLUS -> sb.append("<+>\t- операция сложения\n");
                case MINUS -> sb.append("<->\t- операция вычитания\n");
                case MULTIPLY -> sb.append("<*>\t- операция умножения\n");
                case DIVIDE -> sb.append("</>\t- операция деления\n");
                case LPAREN -> sb.append("<(>\t- открывающая скобка\n");
                case RPAREN -> sb.append("<)>\t- закрывающая скобка\n");
            }
        }
        Files.writeString(file, sb.toString());
    }
}
