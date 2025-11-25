package org.example;

import org.example.exception.ParserException;
import org.example.symbol.Symbol;
import org.example.symbol.SymbolTable;
import org.example.token.Token;
import org.example.token.TokenType;
import org.example.tree.Node;

import java.util.List;
import java.util.function.Supplier;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;
    private final SymbolTable symbolTable;

    public Parser(List<Token> tokens, SymbolTable symbolTable) {
        this.tokens = tokens;
        this.symbolTable = symbolTable;
    }

    public Node parse() {
        Node tree = parseExpression();
        if (hasMoreTokens()) {
            Token extra = getCurrentToken();
            throw new ParserException("Лишний токен " + extra.value(), extra.inputStringPos());
        }
        return tree;
    }

    private Node parseExpression() {
        return parseBinaryOperation(this::parseTerm, TokenType.PLUS, TokenType.MINUS);
    }

    private Node parseTerm() {
        return parseBinaryOperation(this::parseFactor, TokenType.MULTIPLY, TokenType.DIVIDE);
    }

    private Node parseBinaryOperation(Supplier<Node> nextLevel, TokenType... ops) {
        Node node = nextLevel.get();

        while (accept(ops)) {
            Token operator = getPreviousToken();
            Node right = nextLevel.get();
            node = new Node(operator, List.of(node, right));
        }

        return node;
    }

    private Node parseFactor() {
        if (!hasMoreTokens()) {
            throw new ParserException("Ожидался операнд, но выражение закончилось", pos);
        }

        Token current = getCurrentToken();
        return switch (current.type()) {
            case IDENTIFIER -> {
                Node node = new Node(getCurrentTokenAndIncPos());
                Symbol symbol = symbolTable.getSymbol(node.getToken().value().toString());
                if (symbol == null) {
                    throw new ParserException("Неизвестная переменная " + node.getToken().value(), node.getToken().inputStringPos());
                }
                node.setSymbol(symbol);
                yield node;
            }
            case INTEGER_CONST, FLOAT_CONST -> new Node(getCurrentTokenAndIncPos());
            case LPAREN -> parseParenthesizedExpression();
            default -> throw new ParserException("Неожиданный токен " + current.value(), current.inputStringPos());
        };
    }

    private Node parseParenthesizedExpression() {
        Token lparen = getCurrentTokenAndIncPos();
        Node node = parseExpression();

        if (!accept(TokenType.RPAREN)) {
            throw new ParserException("Отсутствует закрывающая скобка для скобки на ", lparen.inputStringPos());
        }
        return node;
    }

    private boolean hasMoreTokens() {
        return pos < tokens.size();
    }

    private Token getCurrentToken() {
        return tokens.get(pos);
    }

    private Token getCurrentTokenAndIncPos() {
        return tokens.get(pos++);
    }

    private Token getPreviousToken() {
        return tokens.get(pos - 1);
    }

    private boolean accept(TokenType... types) {
        if (check(types)) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean check(TokenType... types) {
        if (!hasMoreTokens()) return false;
        for (TokenType t : types)
            if (getCurrentToken().type() == t) return true;
        return false;
    }
}

