package org.example.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Integer> indexByName = new HashMap<>();
    private final List<Symbol> symbols = new ArrayList<>();

    public int add(String name, SymbolType type) {
        if (indexByName.containsKey(name)) {
            return indexByName.get(name);
        }

        Symbol symbol = new Symbol(name, symbols.size()+1, type);
        symbols.add(symbol);
        int pos = symbols.size() - 1;
        indexByName.put(name, pos);
        return pos;
    }

    public Symbol addTemp(String name, SymbolType type) {
        if (indexByName.containsKey(name)) {
            return symbols.get(indexByName.get(name));
        }
        Symbol symbol = new Symbol(name, symbols.size() + 1, type);
        symbols.add(symbol);
        indexByName.put(name, symbols.size() - 1);
        return symbol;
    }

    public Symbol getSymbol(String name) { return symbols.get(indexByName.get(name)); }


    public Integer get(String lex) {
        return indexByName.get(lex);
    }

    public List<Symbol> getAll() {
        return List.copyOf(symbols);
    }
}
