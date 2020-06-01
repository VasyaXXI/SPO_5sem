package ru.Kirilov.biboran.token;

import ru.Kirilov.biboran.lexer.Lexem;

public class Token {

    private final Lexem lexem;
    private final String value;

    public Token(Lexem type, String value) {
        this.lexem = type;
        this.value = value;
    }

    public Lexem getLexem() {
        return lexem;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexem=" + lexem +
                ", value='" + value + '\'' +
                '}';
    }
}
