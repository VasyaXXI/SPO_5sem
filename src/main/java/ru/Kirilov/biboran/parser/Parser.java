package ru.Kirilov.biboran.parser;

import ru.Kirilov.biboran.exception.LangParseException;
import ru.Kirilov.biboran.exception.EofException;
import ru.Kirilov.biboran.lexer.Lexem;
import ru.Kirilov.biboran.token.Token;

import java.util.List;
import java.util.ListIterator;

public class Parser {

    private ListIterator<Token> iterator;

    private int depth;

    private final List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.depth = 0;
        this.iterator = tokens.listIterator();
    }
    //=============================================================================================================
    public void lang() throws LangParseException, EofException {
        while(true) {
            expr();
        }
    }
    //-----------------------------------------------------------------(ветви)
    private void expr() throws LangParseException, EofException {
        try {
            assignExpr();
        } catch (LangParseException e){
            try{
                back(depth);
                depth = 0;
                log_circle();
            } catch (LangParseException ex){
                throw new LangParseException(
                        e.getMessage() + " / "
                                + ex.getMessage()
                );
            }
        }
    }
    //-----------------------------------------------------------------
    private void assignExpr() throws LangParseException, EofException {
        var();
        assignOp();
        value();
        semicolon();
    }
    //-----------------------------------------------------------------(значения)
    private void value() throws LangParseException, EofException {
        try{
            var();
        } catch (LangParseException e) {
            try {
                back(depth);
                depth = 0;
                digit();
            } catch (LangParseException ex){
                throw new LangParseException(
                        e.getMessage() + " / "
                                + ex.getMessage()
                );
            }
        }
    }
    //-----------------------------------------------------------------(буквы)
    private void var() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.VAR);
    }
    //-----------------------------------------------------------------(равно)
    private void assignOp() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.ASSIGN_OP);
    }
    //-----------------------------------------------------------------(числа)
    private void digit() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.DIGIT);
    }
    //----------------------------------------------------------------(ветка циклов)
    private void log_circle() throws LangParseException, EofException {
        if_log();
        log_body();
    }
    //-----------------------------------------------------------------(if - до close if)
    private void if_log() throws LangParseException, EofException {
        if_kw();
        log_bracket();
    }
    //-----------------------------------------------------------------
    private void if_kw() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.IF_KW);
    }
    //-----------------------------------------------------------------
    private void log_bracket() throws LangParseException, EofException {
        open_bracket();
        log_expression();
        close_bracket();
    }
    //-----------------------------------------------------------------
    private void open_bracket() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.OPEN_BRACKET);
    }
    //-----------------------------------------------------------------
    private void log_expression() throws LangParseException, EofException {
        value();
        log_op();
        value();
    }
    //-----------------------------------------------------------------
    private void log_op() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.LOGIC_OP);
    }
    //-----------------------------------------------------------------
    private void close_bracket() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.CLOSE_BRACKET);
    }
    //-----------------------------------------------------------------(close if)
    private void log_body() throws LangParseException, EofException {
        open_brace();
        expr();
        close_brace();
    }
    //-----------------------------------------------------------------
    private void open_brace() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.OPEN_BRACE);
    }
    //-----------------------------------------------------------------
    private void close_brace() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.CLOSE_BRACE);
    }
    //-----------------------------------------------------------------
    private void semicolon() throws LangParseException, EofException {
        match(getCurrentToken(), Lexem.SEMICOLON);
    }
    //=============================================================================================================
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
            depth++;
            return token;
        }
        throw new EofException("EOF");
    }

}