package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.util.Pos;

import static miniplc0java.tokenizer.TokenType.*;

public class Tokenizer {

    private StringIter it;
    private String keyWord[] = {"begin","end","var","const","print"};
    private TokenType keyWordOut[]={Begin,End,Var,Const,Print};
    private TokenType is_keyword(String str){
        for(int i = 0;i < keyWord.length;i++)
        {
            if(keyWord[i].equals(str)) {
                //System.out.println(keyWordOut[i]);
                return keyWordOut[i];
            }
        }
        return null;
    }


    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUInt();//是数字的情况下
        } else if (Character.isAlphabetic(peek)) {
            return lexIdentOrKeyword();//是字母的情况下
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexUInt() throws TokenizeError {
        //it.ptrNext = it.nextPos();
        Pos start_pos=it.ptr;
        char tmp_char=it.nextChar();
        String tmp_arr="";
        while(Character.isDigit(tmp_char)){
            tmp_arr+=tmp_char;
            //it.ptrNext=it.nextPos();
            tmp_char=it.nextChar();
        }
        it.ptr=it.previousPos();
        //it.ptrNext=it.nextPos();
        Pos end_pos=it.ptr;
        int value=Integer.parseInt(tmp_arr);
        return new Token(Uint, value, start_pos, end_pos);
        // 请填空：
        // 直到查看下一个字符不是数字为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 解析存储的字符串为无符号整数
        // 解析成功则返回无符号整数类型的token，否则返回编译错误
        //
        // Token 的 Value 应填写数字的值


        //throw new Error("Not implemented");
    }

    private Token lexIdentOrKeyword() throws TokenizeError {


        //it.ptrNext = it.nextPos();
        Pos start_pos=it.ptr;
        char tmp_char=it.nextChar();
        String tmp_arr="";


        while(Character.isDigit(tmp_char)||Character.isAlphabetic(tmp_char)){
            tmp_arr+=tmp_char;
            //it.ptrNext=it.nextPos();
            tmp_char=it.nextChar();
        }

        it.ptr=it.previousPos();
        //it.ptrNext=it.nextPos();
        Pos end_pos=it.ptr;
        if(is_keyword(tmp_arr)!=null){
            return new Token(is_keyword(tmp_arr), tmp_arr, start_pos, end_pos);
        }
        else{
            return new Token(Ident,tmp_arr,start_pos,end_pos);
        }
        // 请填空：
        // 直到查看下一个字符不是数字或字母为止:
        // -- 前进一个字符，并存储这个字符
        //
        // 尝试将存储的字符串解释为关键字
        // -- 如果是关键字，则返回关键字类型的 token
        // -- 否则，返回标识符
        //
        // Token 的 Value 应填写标识符或关键字的字符串
        //throw new Error("Not implemented");
    }

    private Token lexOperatorOrUnknown() throws TokenizeError {
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.Plus, '+', it.previousPos(), it.currentPos());

            case '-':
                return new Token(TokenType.Minus, '-', it.previousPos(), it.currentPos());
                // 填入返回语句
                //throw new Error("Not implemented");

            case '*':
                // 填入返回语句
                return new Token(Mult, '*', it.previousPos(), it.currentPos());


            case '/':
                // 填入返回语句
                return new Token(TokenType.Div, '/', it.previousPos(), it.currentPos());

            // 填入更多状态和返回语句

            case '=':
                // 填入返回语句
                return new Token(Equal, '=', it.previousPos(), it.currentPos());
            case ';':
                // 填入返回语句
                return new Token(Semicolon, ';', it.previousPos(), it.currentPos());
            case '(':
                // 填入返回语句
                return new Token(LParen, '(', it.previousPos(), it.currentPos());
            case ')':
                // 填入返回语句
                return new Token(RParen, ')', it.previousPos(), it.currentPos());

            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
