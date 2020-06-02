package ru.Kirilov.biboran.lexer;

import java.util.regex.Pattern;

public enum Lexem {

    VAR("^[a-zA-z]*+\\s*$", 0),
    DIGIT("^(0|([1-9][0-9]*))\\s*$", 0),
    ASSIGN_OP("^=\\s*$", 0),
    LOGIC_OP("^(>|<|==|>=|<=)\\s*$", 2),
    MATH_OP("^(\\+|-|\\*|/)\\s*$", 3),
    OPEN_BRACKET("^\\(\\s*$", 1),
    CLOSE_BRACKET("^\\)\\s*$", 1),
    OPEN_SQUARE("^\\[\\s*$", 1),
    CLOSE_SQUARE("^\\]\\s*$", 1),
    OPEN_BRACE("^\\{\\s*$", -1),
    CLOSE_BRACE("^\\}\\s*$", 1),

    SEMICOLON("^;\\s*$", 10),

    IF_KW("^esli\\s*$", 5),
    WHILE_KW("^poka\\s*$", 5),
    FOW_KW("^dlya\\s*$", 5);

    private final Pattern pattern;
    private int priority;

    Lexem(String regexp, int priority) {
        this.pattern = Pattern.compile(regexp);
        this.priority = priority;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getPriority() {
        return priority;
    }
}
