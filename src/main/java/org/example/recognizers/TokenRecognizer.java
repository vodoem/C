package org.example.recognizers;

import org.example.symbol.SymbolTable;
import org.example.token.Token;

import java.util.Optional;

public interface TokenRecognizer {
    Optional<Token> recognize(String input, int position, SymbolTable table);
}
