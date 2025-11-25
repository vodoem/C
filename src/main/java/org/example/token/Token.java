package org.example.token;

public class Token {
    private final TokenType type;
    private final Object lexeme;
    //private final int pos;
    private final int inputStringPos;

    public Token(TokenType type, Object lexeme, int pos, int inputStringPos) {
        this.type = type;
        this.lexeme = lexeme;//this.pos = pos;
        this.inputStringPos = inputStringPos;
    }

    public TokenType type() { return type; }
    public Object value() { return lexeme; }
  /*  public int symbolPos() { return pos; }*/
    public int inputStringPos() { return inputStringPos; }
}
