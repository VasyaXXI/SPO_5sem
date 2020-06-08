package ru.Kirilov.biboran.parser;

import ru.Kirilov.biboran.exception.LangParseException;
import ru.Kirilov.biboran.exception.EofException;
import ru.Kirilov.biboran.lexer.Lexem;
import ru.Kirilov.biboran.token.Token;

import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class Parser {

    private ListIterator<Token> iterator;

    private Stack<Integer> depth;

    private final List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.depth = new Stack<>();
        this.iterator = tokens.listIterator();
    }

    public void lang() throws LangParseException, EofException {
        while(true) {
            expr();
        }
    }
    //
    private void expr() throws LangParseException, EofException {
        depth.push(0);
        try {
            assignExpr();
        } catch (LangParseException e){
            try{
                back(depth.pop());
                depth.push(0);
                if_cycle();
            } catch (LangParseException e2){
                try {
                    back(depth.pop());
                    depth.push(0);
                    while_cycle();
                } catch (LangParseException e3) {
                    throw new LangParseException(
                            e.getMessage() + " / "
                                    + e2.getMessage() + " / "
                                    + e3.getMessage()
                    );

                }
            }
        }
        depth.pop();
    }
    //
    private void assignExpr() throws LangParseException, EofException {
        var();
        assignOp();
        value();
        math_op();
        value();
        semicolon();
    }
    //
//    private void mathExpr() throws LangParseException, EofException {
//        assignE();
//        math_op();
//        value();
//        semicolon();
//    }
//    private void assignE() throws LangParseException, EofException{
//        var();
//        assignOp();
//        value();
//    }
    //
//    private void assignHardExpr() throws LangParseException, EofException {
//        var();
//        assignOp();
//        value();
//        math_op();
//        value();
//        semicolon();
//    }
    //
    private void value() throws LangParseException, EofException {
        depth.push(0);
        try{
            var();
        } catch (LangParseException e) {
            try {
                back(depth.pop());
                depth.push(0);
                digit();
            } catch (LangParseException e2){
                throw new LangParseException(
                        e.getMessage() + " / "
                                + e2.getMessage()
                );
            }
        }
        depth.pop();
    }
    private void var() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.VAR);
    }
    private void assignOp() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.ASSIGN_OP);
    }
    private void digit() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.DIGIT);
    }
    private void if_cycle() throws LangParseException, EofException {
        if_log();
        log_body();
    }

    private void while_cycle() throws LangParseException, EofException {
        while_log();
        log_body();
    }

    private void if_log() throws LangParseException, EofException {
        if_kw();
        condition();
    }

    private void while_log() throws LangParseException, EofException {
        while_kw();
        condition();
    }

    private void if_kw() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.IF_CON);
    }

    private void while_kw() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.WHILE_CON);
    }

    private void condition() throws LangParseException, EofException {
        open_bracket();
        logic_comparison();
        close_bracket();
    }
    private void open_bracket() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.OPEN_BRACKET);
    }
    private void logic_comparison() throws LangParseException, EofException {
        value();
        logic_op();
        value();
    }
    //
    private void math_op() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.MATH_OP);
    }
    //
    private void logic_op() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.LOGIC_OP);
    }
    //
    private void close_bracket() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.CLOSE_BRACKET);
    }
    //
    private void log_body() throws LangParseException, EofException {
        open_brace();
        expr();
        close_brace();
    }
    //
    private void open_brace() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.OPEN_BRACE);
    }
    //
    private void close_brace() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.CLOSE_BRACE);
    }
    //
    private void semicolon() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.SEMICOLON);
    }

    private void match(Token token, Lexem lexem) throws LangParseException {
        if (!token.getLexem().equals(lexem)) {
            throw new LangParseException( lexem.name() + " expected " +
                    "but " + token.getLexem().name() + " found");
        }
    }

    private void back(int step) {
        for(int i = 0; i < step; i++){
            if(iterator.hasPrevious()){
                iterator.previous();
            }
        }
    }

    private Token getCurrentToken() throws EofException {
        if (iterator.hasNext()) {
            Token token = iterator.next();
            depth.push(depth.pop() + 1);
            return token;
        }
        throw new EofException("EOF");
    }

}