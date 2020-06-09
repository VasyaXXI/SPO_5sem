package ru.Kirilov.biboran;

import ru.Kirilov.biboran.lexer.Lexer;
import ru.Kirilov.biboran.token.Token;
import ru.Kirilov.biboran.parser.Parser;
import ru.Kirilov.biboran.poliz.Poliz;
import ru.Kirilov.biboran.poliz.PolizCalc;

import java.util.LinkedList;
import java.io.File;
import java.util.Scanner;

public class BiboranUI {

    public static void main(String[] args) throws Exception {
        File file = new File("src/bibus.txt");
        Scanner inp = new Scanner(file);
        Lexer lexer = new Lexer(inp.nextLine());
        LinkedList<Token> tokens = lexer.tokens();
        while (inp.hasNext()) {
            lexer = new Lexer(inp.nextLine());
            tokens.addAll(lexer.tokens());
        }

        for (Token token: tokens)
            System.out.println(token);

        Parser parser = new Parser(tokens);
        parser.lang();

        System.out.println("\nRPN:");
        Poliz poliz = new Poliz(tokens);
        LinkedList<Token> testPoliz = poliz.makePoliz();
        for (Token token : testPoliz) {
            System.out.println(token.toString());
        }

        PolizCalc.calculate(testPoliz);

    }
}
