package ru.Kirilov.biboran.poliz;

import ru.Kirilov.biboran.lexer.Lexem;
import ru.Kirilov.biboran.token.Token;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Poliz {
    private LinkedList<Token> poliz = new LinkedList<>();
    Queue<Token> tokens = new LinkedList<>();

    public Poliz(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }
    public  LinkedList<Token> makePoliz() {
        while (!tokens.isEmpty()) {
            Token token = tokens.peek();
            if (token.getLexem() != Lexem.WHILE_CON && token.getLexem() != Lexem.IF_CON) {
                makePolizFromExpr(tokens);
            }
            else if (token.getLexem() == Lexem.WHILE_CON){
                makePolizFromWhile(tokens);
            } else {
                makePolizFromIf(tokens);
            }
        }

        return poliz;
    }

    private void makePolizFromWhile(Queue<Token> input) {
        Queue<Token> boolExpr = new LinkedList<>();
        input.poll();
        Token token = input.poll();
        int index = poliz.size();
        while (token.getLexem() != Lexem.OPEN_BRACE) {
            boolExpr.add(token);
            token = input.poll();
        }

        makePolizFromExpr(boolExpr);
        poliz.add(new Token(Lexem.MARK_INDEX, Integer.toString(p(poliz.size(), input))));
        poliz.add(new Token(Lexem.MARK, "!F"));

        Queue<Token> expr = new LinkedList<>();
        token = input.poll();
        while (token.getLexem() != Lexem.CLOSE_BRACE) {
            if (token.getLexem() == Lexem.WHILE_CON) {
                makePolizFromExpr(expr);
                makePolizFromWhile(input);
            }
            if (token.getLexem() != Lexem.WHILE_CON)
                expr.add(token);
            token = input.poll();
        }
        makePolizFromExpr(expr);

        poliz.add(new Token(Lexem.MARK_INDEX, Integer.toString(index)));
        poliz.add(new Token(Lexem.MARK, "!"));
    }

    private void makePolizFromIf(Queue<Token> input) {
        Queue<Token> boolExpr = new LinkedList<>();
        input.poll();
        Token token = input.poll();
        int index = poliz.size();
        while (token.getLexem() != Lexem.OPEN_BRACE) {
            boolExpr.add(token);
            token = input.poll();
        }

        makePolizFromExpr(boolExpr);
        poliz.add(new Token(Lexem.MARK_INDEX, Integer.toString(p(poliz.size(), input))));
        Token mark = poliz.getLast();
        poliz.add(new Token(Lexem.MARK, "!F"));

        Queue<Token> expr = new LinkedList<>();
        token = input.poll();
        while (token.getLexem() != Lexem.CLOSE_BRACE) {
            if (token.getLexem() == Lexem.WHILE_CON) {
                makePolizFromExpr(expr);
                makePolizFromWhile(input);
            }
            if (token.getLexem() == Lexem.IF_CON) {
                makePolizFromExpr(expr);
                makePolizFromWhile(input);
            }
            if (token.getLexem() != Lexem.WHILE_CON && token.getLexem() != Lexem.IF_CON)
                expr.add(token);
            token = input.poll();
        }
        makePolizFromExpr(expr);

        poliz.add(mark);
        poliz.add(new Token(Lexem.MARK, "!if"));
    }


    private int p(int size, Queue<Token> tokens) {
        int p = size;
        int i = 1;

        Queue<Token> newtokens = new LinkedList<>(tokens);
        Token newtoken = newtokens.poll();

        while (i > 0){
            assert newtoken != null;
            if (newtoken.getLexem() == Lexem.WHILE_CON) {
                i++;
                p--;
            }
            if (newtoken.getLexem() == Lexem.CLOSE_BRACE) {
                i--;
            }
            newtoken = newtokens.poll();
            assert newtoken != null;
            if (newtoken.getLexem() != Lexem.END_LINE) {
                p++;
            }
        }
        p+=3;

        return p;
    }

    private void makePolizFromExpr(Queue<Token> input) {
        Stack<Token> stack = new Stack<>();

        while (!input.isEmpty()) {
            Token token = input.peek();

            if (token.getLexem() == Lexem.WHILE_CON || token.getLexem() == Lexem.IF_CON) {
                break;
            }

            if (token.getLexem() == Lexem.TYPE) {
                poliz.add(token);
            }

            token = input.poll();

            //Если лексема является числом или переменной, добавляем ее в ПОЛИЗ-массив.
            if (token.getLexem() == Lexem.VAR || token.getLexem() == Lexem.DIGIT) {
                poliz.add(token);
            }

            //Если лексема является бинарной операцией, тогда:
            if (token.getLexem() == Lexem.MATH_OP || token.getLexem() == Lexem.LOGIC_OP
                    || token.getLexem() == Lexem.ASSIGN_OP || token.getLexem() == Lexem.FUN_OP) {
                if (!stack.empty()) {
                    while (!stack.empty() && getPriorOfOp(token.getValue()) >= getPriorOfOp(stack.peek().getValue())) {
                        poliz.add(stack.pop());
                    }
                }
                stack.push(token);
            }

            //Если лексема является открывающей скобкой, помещаем ее в стек.
            if (token.getLexem() == Lexem.OPEN_BRAC) {
                stack.push(token);
            }

            if (token.getLexem() == Lexem.CLOSE_BRAC) {
                if (!stack.empty()) {
                    while (!stack.empty() && stack.peek().getLexem() != Lexem.OPEN_BRAC) {
                        poliz.add(stack.pop());
                    }
                    if (!stack.empty() && stack.peek().getLexem() == Lexem.OPEN_BRAC) {
                        stack.pop();
                    }
                }
            }

            if (token.getLexem() == Lexem.END_LINE) {
                while (!stack.empty()) {
                    poliz.add(stack.pop());
                }
            }
        }

        while (!stack.empty()) {
            poliz.add(stack.pop());
        }
    }

    private int getPriorOfOp(String op) {
        if (op.equals("*") || op.equals("/"))
            return 0;
        else if (op.equals("*") || op.equals("/"))
            return 1;
        else if (op.equals("+") || op.equals("-"))
            return 2;
        else if (op.equals(">") || op.equals(">=") || op.equals("<") || op.equals("<=") || op.equals("==") || op.equals("!="))
            return 3;
        else
            return 4;
    }
}
