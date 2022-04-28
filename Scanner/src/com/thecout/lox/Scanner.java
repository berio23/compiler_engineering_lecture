package com.thecout.lox;


import java.util.ArrayList;
import java.util.List;

import static com.thecout.lox.TokenType.EOF;

public class Scanner {


    private final String source;
    private final List<Token> tokens = new ArrayList<>();


    private boolean isComment;

    public Scanner(String source) {
        this.source = source;
    }


    public List<Token> scanLine(String line, int lineNumber) {
        List<Token> returnToken = new ArrayList<>();

        String text = "";
        Token oldToken = null;
        Token newToken = null;
        char[] lineCharArr = line.toCharArray();

        for (int i = 0; i < lineCharArr.length; i++)
        {
            text += lineCharArr[i];
            oldToken = newToken;
            newToken = getToken(text, lineNumber);


            if(oldToken != null && oldToken.type == TokenType.NUMBER && newToken == null && lineCharArr[i] == '.'){
                line += lineCharArr[++i];
                newToken = getToken(line, lineNumber);
            }

            if(newToken == null && oldToken != null){
                returnToken.add(oldToken);
                line = "";


                if(lineCharArr[i] != ' ')
                    i--;
            }

            if(i == lineCharArr.length - 1 && newToken != null){
                returnToken.add(newToken);
            }

        }

        isComment = false;

        return returnToken;

        }



    private Token getToken(String temp, int lineNumber)
    {
        Token token;

        if(temp == "//") {
            isComment = true;
            return null;
        }

        //Nummer erkennen

        if(temp.matches("\\d+([.]\\d+)?"))
        {
            return new Token(TokenType.NUMBER, temp, Double.parseDouble(temp), lineNumber);
        }
        if(temp.matches("\".*\""))
        {
            return new Token(TokenType.STRING, temp, temp.substring(1, temp.length() - 1), lineNumber);
        }

        token = getKeywordToken(temp, lineNumber);

        if(token != null)
        {
            return token;
        }
        if(temp.matches("([a-z]|_)([a-z]|[A-Z]|_|\\d)*"))
        {
            return new Token(TokenType.IDENTIFIER, temp, temp, lineNumber);
        }

        token = getSingleCharToken(temp, lineNumber);

        if(token != null)
        {
            return token;
        }
        token = getOneOrTwoCharToken(temp, lineNumber);

        if(token != null)
        {
            return token;
        }

        return null;
    }

    private Token getSingleCharToken(String temp, int lineNum) {

        //look for all cases of every possible single Character
        switch (temp) {
            // Single-character tokens.
            case "-":
                return new Token(TokenType.MINUS, temp, temp, lineNum);
            case ";":
                return new Token(TokenType.SEMICOLON, temp, temp, lineNum);
            case "*":
                return new Token(TokenType.STAR, temp, temp, lineNum);
            case "+":
                return new Token(TokenType.PLUS, temp, temp, lineNum);
            case "(":
                return new Token(TokenType.LEFT_PAREN, temp, temp, lineNum);
            case ")":
                return new Token(TokenType.RIGHT_PAREN, temp, temp, lineNum);
            case ",":
                return new Token(TokenType.COMMA, temp, temp, lineNum);
            case "{":
                return new Token(TokenType.LEFT_BRACE, temp, temp, lineNum);
            case "}":
                return new Token(TokenType.RIGHT_BRACE, temp, temp, lineNum);
            case "/":
                return new Token(TokenType.SLASH, temp, temp, lineNum);
            case ".":
                return new Token(TokenType.DOT, temp, temp, lineNum);
            default:
                return null;
        }
    }

    private Token getOneOrTwoCharToken(String temp, int lineNum)
    {
        //look thorugh all possible one or Two characters
        switch (temp){
            // One or two character tokens.
            case "!=":
                return new Token(TokenType.BANG_EQUAL, temp, temp, lineNum);
            case "<=":
                return new Token(TokenType.LESS_EQUAL, temp, temp, lineNum);
            case "!":
                return new Token(TokenType.BANG, temp, temp, lineNum);
            case "=":
                return new Token(TokenType.EQUAL, temp, temp, lineNum);
            case "==":
                return new Token(TokenType.EQUAL_EQUAL, temp, temp, lineNum);
            case ">":
                return new Token(TokenType.GREATER, temp, temp, lineNum);
            case "<":
                return new Token(TokenType.LESS, temp, temp, lineNum);
            case ">=":
                return new Token(TokenType.GREATER_EQUAL, temp, temp, lineNum);
            default:
                return null;
        }
    }

    private Token getKeywordToken(String temp, int lineNum)
    {
        //go through all possible Keywords to find the right one
        switch (temp){
            // One or two character tokens.
            case "true":
                return new Token(TokenType.TRUE, temp, temp, lineNum);
            case "and":
                return new Token(TokenType.AND, temp, temp, lineNum);
            case "var":
                return new Token(TokenType.VAR, temp, temp, lineNum);
            case "if":
                return new Token(TokenType.IF, temp, temp, lineNum);
            case "else":
                return new Token(TokenType.ELSE, temp, temp, lineNum);
            case "false":
                return new Token(TokenType.FALSE, temp, temp, lineNum);
            case "return":
                return new Token(TokenType.RETURN, temp, temp, lineNum);
            case "print":
                return new Token(TokenType.PRINT, temp, temp, lineNum);
            case "while":
                return new Token(TokenType.WHILE, temp, temp, lineNum);
            case "fun":
                return new Token(TokenType.FUN, temp, temp, lineNum);
            case "for":
                return new Token(TokenType.FOR, temp, temp, lineNum);
            case "nil":
                return new Token(TokenType.NIL, temp, temp, lineNum);
            case "or":
                return new Token(TokenType.OR, temp, temp, lineNum);
            default:
                return null;
        }
    }
    public List<Token> scan() {
        String[] lines = source.split("\n");
        for (int i = 0; i < lines.length; i++) {
            tokens.addAll(scanLine(lines[i], i));
        }
        tokens.add(new Token(EOF, "", "", lines.length));
        return tokens;
    }

}
