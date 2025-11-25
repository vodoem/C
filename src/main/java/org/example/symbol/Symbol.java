package org.example.symbol;

public class Symbol {
    private final String name;
    private final int index;
    private final SymbolType type;

    public Symbol(String name, int index, SymbolType type) {
        this.name = name;
        this.index = index;
        this.type = type;
    }

    public int getIndex() {return index;}

    public String getName() { return name; }

    public SymbolType getType() { return type; }

    @Override
    public String toString() {
        return name;
    }
}
