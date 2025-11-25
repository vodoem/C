package org.example;


import org.example.exception.LexicalAnalyzerException;
import org.example.exception.ParserException;
import org.example.io.ExpressionReaderUtil;
import org.example.io.SymbolWriterUtil;
import org.example.io.SyntaxTreeWriterUtil;
import org.example.io.TokenWriterUtil;
import org.example.symbol.SymbolTable;
import org.example.token.Token;
import org.example.tree.Node;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Неверное количество аргументов запуска");
            return;
        }

        String mode = args[0].toUpperCase();
        String inputFile = args[1];
       // String outputFile = args[2];

        try {
            String input = ExpressionReaderUtil.readExpression(inputFile);
            LexicalAnalyzer analyzer = new LexicalAnalyzer();
            SymbolTable symbols = new SymbolTable();
            List<Token> tokens = analyzer.analyze(input, symbols);
            if (mode.equals("LEX")) {
                TokenWriterUtil.writeTokens(Path.of("tokens.txt"), tokens, symbols);
                SymbolWriterUtil.writeSymbols(Path.of("symbols.txt"), symbols);
                System.out.println("Лексический анализ успешно завершён!");
            } else if (mode.equals("SYN")) {
                Parser parser = new Parser(tokens, symbols);
                Node tree = parser.parse();
                SyntaxTreeWriterUtil.writeSyntaxTree(Path.of("syntax_tree.txt"), tree, symbols);
                System.out.println("Синтаксический анализ успешно завершён!");
            } else if (mode.equals("SEM")) {
                Parser parser = new Parser(tokens, symbols);
                Node tree = parser.parse();
                SemanticAnalyzer semantic = new SemanticAnalyzer();
                Node modTree = semantic.analyze(tree);
                SyntaxTreeWriterUtil.writeSyntaxTree(Path.of("syntax_tree_mod.txt"), modTree, symbols);
                System.out.println("Семантический анализ успешно завершён!");
            } else if (mode.equals("GEN1")) {
                Parser parser = new Parser(tokens, symbols);
                Node tree = parser.parse();
                SemanticAnalyzer semantic = new SemanticAnalyzer();
                Node modTree = semantic.analyze(tree);

                ThreeAddressCodeGenerator generator = new ThreeAddressCodeGenerator(symbols);
                List<String> code = generator.generate(modTree);

                Files.write(Path.of("portable_code.txt"), code);
                SymbolWriterUtil.writeSymbols(Path.of("symbols.txt"), symbols);

                System.out.println("Трехадресный код успешно сгенерирован!");
            }
            else if (mode.equals("GEN2")) {
                Parser parser = new Parser(tokens, symbols);
                Node tree = parser.parse();
                SemanticAnalyzer semantic = new SemanticAnalyzer();
                Node modTree = semantic.analyze(tree);

                PostfixGenerator generator = new PostfixGenerator();
                List<String> postfix = generator.generate(modTree);

                Files.writeString(Path.of("postfix.txt"), String.join(" ", postfix));
                SymbolWriterUtil.writeSymbols(Path.of("symbols.txt"), symbols);

                System.out.println("Постфиксная нотация успешно сгенерирована!");
            } else {
                System.err.println("Неверный режим: " + mode + " (должно быть LEX или SYN)");
            }
        } catch (LexicalAnalyzerException e) {
            System.err.println("Лексическая ошибка: " + e.getMessage());
        } catch (ParserException e) {
            System.err.println("Синтаксическая ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
