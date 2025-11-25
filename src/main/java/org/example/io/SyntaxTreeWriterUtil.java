package org.example.io;

import org.example.symbol.SymbolTable;
import org.example.token.TokenType;
import org.example.tree.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SyntaxTreeWriterUtil {
    public static void writeSyntaxTree(Path file, Node root, SymbolTable symbolTable) throws IOException {
        StringBuilder sb = new StringBuilder();
        buildTreeString(root, sb, "", symbolTable);
        Files.writeString(file, sb.toString());
    }

    private static void buildTreeString(Node node, StringBuilder sb, String prefix, SymbolTable symbolTable) {
        if (node.getToken().type() == TokenType.IDENTIFIER) {
            sb.append(prefix)
                    .append("|---")
                    .append("<").append("id,").append(symbolTable.getSymbol(node.getToken().value().toString()).getIndex()).append(">")
                    .append("\n");
        } else {
            sb.append(prefix)
                    .append("|---")
                    .append("<").append(node.getToken().value()).append(">")
                    .append("\n");
        }



        List<Node> children = node.getChildren();
        for (Node child : children) {
            buildTreeString(child, sb, prefix + ("    "), symbolTable);
        }
    }
}
