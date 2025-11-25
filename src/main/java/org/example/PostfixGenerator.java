package org.example;

import org.example.symbol.Symbol;
import org.example.symbol.SymbolTable;
import org.example.token.TokenType;
import org.example.tree.Node;

import java.util.ArrayList;
import java.util.List;

public class PostfixGenerator {
    private final List<String> output = new ArrayList<>();

    public PostfixGenerator() {
    }

    public List<String> generate(Node root) {
        traverse(root);
        return output;
    }

    private void traverse(Node node) {

        if (node.getChildren().isEmpty()) {
            if (node.getToken().type() == TokenType.IDENTIFIER) {
                Symbol s = node.getSymbol();
                output.add("<id," + s.getIndex() + ">");
            } else {
                output.add("<" + node.getToken().value() + ">");
            }
            return;
        }

        // Рекурсивный обход
        for (Node child : node.getChildren()) {
            traverse(child);
        }

        // Операции
        if (node.getToken().value().equals("Int2Float")) {
            output.add("<i2f>");
            return;
        }

        switch (node.getToken().type()) {
            case PLUS -> output.add("<+>");
            case MINUS -> output.add("<->");
            case MULTIPLY -> output.add("<*>");
            case DIVIDE -> output.add("</>");
        }
    }
}
