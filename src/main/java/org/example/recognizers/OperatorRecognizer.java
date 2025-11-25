package org.example.recognizers;

import org.example.symbol.SymbolTable;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.Map;
import java.util.Optional;

public class OperatorRecognizer implements TokenRecognizer {
    private final Map<Character, TokenType> OPS = Map.of(
            '+', TokenType.PLUS,
            '-', TokenType.MINUS,
            '*', TokenType.MULTIPLY,
            '/', TokenType.DIVIDE,
            '(', TokenType.LPAREN,
            ')', TokenType.RPAREN
    );

    @Override
    public Optional<Token> recognize(String input, int pos, SymbolTable table) {
        TokenType type = OPS.get(input.charAt(pos));
        if (type == null)
        {
            return Optional.empty();
        }
        return Optional.of(new Token(type, String.valueOf(input.charAt(pos)), -1, pos + 1));
    }
}
