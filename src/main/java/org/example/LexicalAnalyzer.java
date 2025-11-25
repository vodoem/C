package org.example;

import org.example.exception.LexicalAnalyzerException;
import org.example.recognizers.IdentifierRecognizer;
import org.example.recognizers.NumberRecognizer;
import org.example.recognizers.OperatorRecognizer;
import org.example.recognizers.TokenRecognizer;
import org.example.symbol.SymbolTable;
import org.example.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LexicalAnalyzer {
    private final List<TokenRecognizer> recognizers = List.of(
            new IdentifierRecognizer(),
            new NumberRecognizer(),
            new OperatorRecognizer()
    );

    public List<Token> analyze(String input, SymbolTable table) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;

        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            boolean matched = false;
            for (TokenRecognizer r : recognizers) {
                Optional<Token> result = r.recognize(input, pos, table);
                if (result.isPresent()) {
                    Token token = result.get();
                    tokens.add(token);

                    pos = token.inputStringPos();
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                throw new LexicalAnalyzerException("Недопустимый символ '" + c + "'", pos);
            }
        }

        return tokens;
    }
}
