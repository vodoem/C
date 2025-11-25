package org.example.recognizers;

import org.example.exception.LexicalAnalyzerException;
import org.example.symbol.Symbol;
import org.example.symbol.SymbolTable;
import org.example.symbol.SymbolType;
import org.example.token.Token;
import org.example.token.TokenType;

import java.util.Optional;

public class IdentifierRecognizer implements TokenRecognizer {

    @Override
    public Optional<Token> recognize(String input, int pos, SymbolTable table) {
        char first = input.charAt(pos);
        if (!Character.isLetter(first) && first != '_') return Optional.empty();

        int start = pos;
        while (pos < input.length() &&
                (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
            pos++;
        }

        String name = input.substring(start, pos);
        SymbolType type = SymbolType.INT; // по умолчанию

        if (pos < input.length() && input.charAt(pos) == '[') {
            if (pos + 2 >= input.length() || input.charAt(pos + 2) != ']') {
                throw new LexicalAnalyzerException(
                        "Неправильный синтаксис типа для переменной " + name, pos
                );
            }
            char t = Character.toLowerCase(input.charAt(pos + 1));
            type = (t == 'f') ? SymbolType.FLOAT : SymbolType.INT;
            pos += 3; // пропускаем [f] или [i]
        }

        int symbolPos = table.add(name, type);
        return Optional.of(new Token(TokenType.IDENTIFIER, name, symbolPos, pos));
    }

}
