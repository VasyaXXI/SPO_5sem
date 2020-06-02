package ru.Kirilov.biboran;

import ru.Kirilov.biboran.lexer.Lexer;
import ru.Kirilov.biboran.token.Token;
import ru.Kirilov.biboran.parser.Parser;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BiboranUI {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here
        File file = new File("src/bibus.txt");
        Scanner inp = new Scanner(file);
        Lexer lexer = new Lexer(inp.nextLine());
        List<Token> tokens = lexer.tokens();
        while (inp.hasNext()) {
            lexer = new Lexer(inp.nextLine());
            tokens.addAll(lexer.tokens());
        }

        for (Token token: tokens)
            System.out.println(token);

        Parser parser = new Parser(tokens);
        parser.lang();
    }
}
