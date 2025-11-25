package org.example;

import org.example.exception.ParserException;
import org.example.symbol.SymbolType;
import org.example.token.Token;
import org.example.token.TokenType;
import org.example.tree.Node;

import java.util.List;

public class SemanticAnalyzer {

    public Node analyze(Node node) throws ParserException {
        if (node.getChildren().isEmpty()) {
            // Лист дерева (операнд)
            return node;
        }

        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = analyze(node.getChildren().get(i));
            node.getChildren().set(i, child); // обновляем потомка
        }

        // Проверяем текущий оператор
        TokenType type = node.getToken().type();
        if (type == TokenType.DIVIDE) {
            Node right = node.getChildren().get(1);
            if (isZeroConstant(right)) {
                throw new ParserException("Деление на 0", right.getToken().inputStringPos());
            }
        }

        // Преобразование типов
        if (type == TokenType.PLUS || type == TokenType.MINUS ||
                type == TokenType.MULTIPLY || type == TokenType.DIVIDE) {

            Node left = node.getChildren().get(0);
            Node right = node.getChildren().get(1);

            if (getVarType(left) == SymbolType.INT && getVarType(right) == SymbolType.FLOAT) {
                node.getChildren().set(0, wrapInt2Float(left));
            } else if (getVarType(left) == SymbolType.FLOAT && getVarType(right) == SymbolType.INT) {
                node.getChildren().set(1, wrapInt2Float(right));
            }
        }

        return node;
    }

    private boolean isZeroConstant(Node node) {
        Token t = node.getToken();
        return (t.type() == TokenType.INTEGER_CONST && t.value().toString().equals("0")) ||
                (t.type() == TokenType.FLOAT_CONST && t.value().toString().equals("0.0"));
    }

    private SymbolType getVarType(Node node) {
        Token t = node.getToken();
        if (t.type() == TokenType.IDENTIFIER) {
            return node.getSymbol().getType(); // метод getSymbol() нужно добавить в Node
        } else if (t.type() == TokenType.INTEGER_CONST) return SymbolType.INT;
        else if (t.type() == TokenType.FLOAT_CONST) return SymbolType.FLOAT;
        else if (t.value().equals("Int2Float")) return SymbolType.FLOAT;
        else {
            // для операций — тип определяется автоматически после анализа детей
            Node left = node.getChildren().get(0);
            Node right = node.getChildren().get(1);
            return getVarType(left) == SymbolType.FLOAT || getVarType(right) == SymbolType.FLOAT ? SymbolType.FLOAT : SymbolType.INT;
        }
    }

    private Node wrapInt2Float(Node node) {
        Token t = new Token(TokenType.IDENTIFIER, "Int2Float", -1, node.getToken().inputStringPos());
        return new Node(t, List.of(node));
    }
}
