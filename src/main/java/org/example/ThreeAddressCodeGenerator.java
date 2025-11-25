package org.example;

import org.example.symbol.Symbol;
import org.example.symbol.SymbolTable;
import org.example.symbol.SymbolType;
import org.example.token.TokenType;
import org.example.tree.Node;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCodeGenerator {
    private final List<String> code = new ArrayList<>();
    private final SymbolTable symbols;
    private int tempCounter = 1;

    public ThreeAddressCodeGenerator(SymbolTable symbols) {
        this.symbols = symbols;
    }

    public List<String> generate(Node root) {
        generateForNode(root);
        return code;
    }

    private Symbol generateForNode(Node node) {
        // Лист дерева → переменная или константа
        if (node.getChildren().isEmpty()) {
            if (node.getToken().type() == TokenType.IDENTIFIER) {
                return node.getSymbol();
            } else {
                // Константа: не создаём временную переменную для GEN1
                return null;
            }
        }

        // Операция Int2Float
        if (node.getToken().value().equals("Int2Float")) {
            Symbol child = generateForNode(node.getChildren().get(0));
            Symbol temp = createTemp(SymbolType.FLOAT);
            code.add("i2f <id," + temp.getIndex() + "> <id," + child.getIndex() + ">");
            return temp;
        }

        // Бинарные операции
        Node leftNode = node.getChildren().get(0);
        Node rightNode = node.getChildren().get(1);

        Symbol left = generateForNode(leftNode);
        Symbol right = generateForNode(rightNode);

        String op;
        switch (node.getToken().type()) {
            case PLUS -> op = "add";
            case MINUS -> op = "sub";
            case MULTIPLY -> op = "mul";
            case DIVIDE -> op = "div";
            default -> throw new RuntimeException("Unknown operator");
        }

        SymbolType resultType = SymbolType.INT;
        if ((left != null && left.getType() == SymbolType.FLOAT) ||
                (right != null && right.getType() == SymbolType.FLOAT)) {
            resultType = SymbolType.FLOAT;
        }

        Symbol temp = createTemp(resultType);

        String leftStr = leftNode.getToken().type() == TokenType.IDENTIFIER
                ? "<id," + left.getIndex() + ">"
                : leftNode.getToken().value().toString();

        String rightStr = rightNode.getToken().type() == TokenType.IDENTIFIER
                ? "<id," + right.getIndex() + ">"
                : rightNode.getToken().value().toString();

        code.add(op + " <id," + temp.getIndex() + "> " + leftStr + " " + rightStr);

        return temp;
    }

    private Symbol createTemp(SymbolType type) {
        String name = "#T" + tempCounter++;
        return symbols.addTemp(name, type);
    }
}
