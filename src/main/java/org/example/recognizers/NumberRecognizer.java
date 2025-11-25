package org.example.recognizers;

import org.example.exception.LexicalAnalyzerException;
import org.example.symbol.SymbolTable;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.Optional;

public class NumberRecognizer implements TokenRecognizer {

    @Override
    public Optional<Token> recognize(String input, int pos, SymbolTable table) {
        if (!Character.isDigit(input.charAt(pos))) return Optional.empty();

        int start = pos;
        boolean hasDot = false;

        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isDigit(c)) {
                pos++;
            }
            else if (c == '.' && !hasDot) {
                hasDot = true; pos++;
            }
            else if (c == '.') {
                throw new LexicalAnalyzerException(
                        "Неправильно задана константа '" + input.substring(start, pos + 1) + "'", start);
            } else {
                break;
            }
        }

        String number = input.substring(start, pos);

        if (pos < input.length()) {
            char next = input.charAt(pos);
            if (Character.isLetter(next) || next == '_') {
                throw new LexicalAnalyzerException(
                        "Идентификатор '" + input.substring(start, pos + 1) + "' не может начинаться с цифры", start);
            }
        }

        TokenType type = hasDot ? TokenType.FLOAT_CONST : TokenType.INTEGER_CONST;
        return Optional.of(new Token(type, number, -1, pos + 1));
    }
}
