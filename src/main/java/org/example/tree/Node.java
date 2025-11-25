package org.example.tree;

import org.example.symbol.Symbol;
import org.example.token.Token;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Token token;
    private final List<Node> children = new ArrayList<>();
    private Symbol symbol;

    public Node(Token token) {
        this.token = token;
    }

    public Node(Token token, List<Node> children) {
        this.token = token;
        this.children.addAll(children);
    }

    public void setSymbol(Symbol symbol) { this.symbol = symbol; }
    public Symbol getSymbol() { return symbol; }
    public Token getToken() {
        return token;
    }
    public List<Node> getChildren() {
        return children;
    }
}
