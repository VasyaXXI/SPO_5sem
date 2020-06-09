package ru.Kirilov.biboran.lexer;

import java.util.regex.Pattern;

public enum Lexem {
    VAR("^[a-zA-Z][a-zA-Z0-9]*+\\s*$"),

    DIGIT("^(0|([1-9][0-9]*))\\s*$"),

    ASSIGN_OP("^=\\s*$"),

    MATH_OP("^(\\+|-|\\*|/)\\s*$"),
    LOGIC_OP("^(>|>=|==|<=|<)\\s*$"),

    IF_CON("^esli\\s*$"),
    WHILE_CON("^poka +\\s*$"),

    OPEN_BRAC("^(\\()\\s*$"),
    CLOSE_BRAC("^(\\))\\s*$"),

    OPEN_BRACE("^(\\{)\\s*$"),
    CLOSE_BRACE("^(\\})\\s*$"),

    TYPE("^list\\s*$"),
    FUN_OP("^(add|remove|get|)\\s*$"),

    BOOL("true|false"),

    END_LINE("^\n$"),
    MARK("^$"),
    MARK_INDEX("^$");

    private final Pattern pattern;

    Lexem(String regexp) {
        this.pattern = Pattern.compile(regexp);
    }

    public Pattern getPattern() {
        return pattern;
    }
}