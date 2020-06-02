package ru.Kirilov.biboran.parser;

import ru.Kirilov.biboran.exception.LangParseException;
import ru.Kirilov.biboran.lexer.Lexem;
import ru.Kirilov.biboran.token.Token;

import java.util.Iterator;
import java.util.List;

public class Parser {

    private final List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void lang() throws LangParseException {
        while(true) {
            expr();
        }
    }

    private void expr() throws LangParseException {
        var();
        assignOp();
        assignExpr();
    }

    private void var() throws LangParseException {
        match(getCurrentToken(), Lexem.VAR);
    }


    private void assignOp() throws LangParseException {
        match(getCurrentToken(), Lexem.ASSIGN_OP);
    }

    private void assignExpr() throws LangParseException {
        value();
        while (true) {
            op();
            value();
        }
    }

    private void op() throws LangParseException {
        match(getCurrentToken(), Lexem.MATH_OP);

    }

    private void value() throws LangParseException {
        try{
            var();
        } catch (LangParseException e) {
            try {
                digit();
            } catch (LangParseException ex) {
                throw new LangParseException(
                        e.getMessage() + " / "
                                + ex.getMessage()
                );
            }
        }
    }

    private void digit() throws LangParseException {
        match(getCurrentToken(), Lexem.DIGIT);
    }

    private void match(Token token, Lexem lexem) throws LangParseException {
        if (!token.getLexem().equals(lexem)) {
            throw new LangParseException( lexem.name() + " expected " +
                    "but " + token.getLexem().name() + " found");
        }
    }


    private Token getCurrentToken() throws LangParseException {
        Iterator<Token> iterator = tokens.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new LangParseException("EOF");
    }

}