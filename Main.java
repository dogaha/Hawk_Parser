import java.io.File;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    //Global Declarations
    //Variables
    static int charClass;
    static char nextChar;
    static String nextLexeme;
    static int nextToken;

    //Reserved Words
    static final String[] RESERVED_WORDS = {
        "program", 
        "begin",
        "end", 
        "if", 
        "then", 
        "else", 
        "input", 
        "output", 
        "while", 
        "loop",
        "int",
        "float",
        "double", 
        "call"
    };

    //Symbol Table
    static HashMap<String, String> SYMBOL_TABLE = new HashMap<>();

    // Character Classes
    static final int LETTER = 0;
    static final int DIGIT = 1;
    static final int UNKNOWN = 99;

    // Tokens Code
    static final int INT_LIT = 10;
    static final int FLOAT_LIT = 11;
    static final int DOUBLE_LIT = 12;
    static final int IDENT = 13;
    static final int COLON = 14;
    static final int SEMI = 15;
    static final int COMMA = 16;
    static final int DOT = 17;
    static final int UNDERSCORE = 18;
    static final int ASSIGN_OP = 20; // :=
    static final int ADD_OP = 21; // +
    static final int SUB_OP = 22; // -
    static final int MULT_OP = 23; // *
    static final int DIV_OP = 24; // /
    static final int LEFT_PAREN = 25; // (
    static final int RIGHT_PAREN = 26; // )
    static final int LESS_THAN = 27; // <
    static final int GREATER_THAN = 28; // >
    static final int EQUALS = 29; // =
    static final int NOT_EQUAL = 30; // <>
    static final int EOF = -1;
    
    // Reserved Word Tokens
    static final int RESERVED_PROGRAM = 40;
    static final int RESERVED_BEGIN = 41;
    static final int RESERVED_END = 42;
    static final int RESERVED_IF = 43;
    static final int RESERVED_THEN = 44;
    static final int RESERVED_ELSE = 45;
    static final int RESERVED_INPUT = 46;
    static final int RESERVED_OUTPUT = 47;
    static final int RESERVED_WHILE = 48;
    static final int RESERVED_LOOP = 49;
    static final int RESERVED_INT = 50;
    static final int RESERVED_FLOAT = 51;
    static final int RESERVED_DOUBLE = 52;
    static final int RESERVED_CALL = 53;

    // Inputed Code
    static String program = "";
    static int programIndex = 0;
    static int lineNumber = 1;

    // Add next char to lexeme
    public static void addCurrentChar(){
        nextLexeme += nextChar;
    }

    //Check next Character
    public static char checkCurrentChar(){
        return program.charAt(programIndex);
    }

    //set next character to the next char
    public static void getNextChar(){
        if (programIndex < program.length()){
            nextChar = program.charAt(programIndex++);
            if (nextChar == '\n') {
                lineNumber++; 
                System.out.printf("Line %d\n",lineNumber);
            }
            if (Character.isLetter(nextChar))
                charClass = LETTER;
            else if (Character.isDigit(nextChar))
                charClass = DIGIT;
            else
                charClass = UNKNOWN;
        } else {
            charClass = EOF;
        }
    }

    public static void getNonBlank(){
        while (Character.isWhitespace(nextChar)) {getNextChar();}
    }

    //Look up special characters
    public static void lookup(char c){
        switch (c) {
            case '(': //
                addCurrentChar();
                nextToken = LEFT_PAREN;
                break;
            case  ')':
                addCurrentChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addCurrentChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addCurrentChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addCurrentChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addCurrentChar();
                nextToken = DIV_OP;
                break;
            case ':':
                addCurrentChar();
                if (checkCurrentChar() == '='){
                    getNextChar();
                    addCurrentChar();
                    nextToken = ASSIGN_OP;
                } else {
                    nextToken = COLON;
                }
                break;
            case ';':
                addCurrentChar();
                nextToken = SEMI;
                break;
            case ',':
                addCurrentChar();
                nextToken = COMMA;
                break;
            case '<':
                addCurrentChar();
                if (checkCurrentChar() == '>'){
                    getNextChar();
                    addCurrentChar();
                    nextToken = NOT_EQUAL;
                } else {
                    nextToken = LESS_THAN;
                }
                break;
            case '>':
                addCurrentChar();
                nextToken = GREATER_THAN;
                break;
            case '=':
                addCurrentChar();
                nextToken = EQUALS;
                break;
            case '_':
                addCurrentChar();
                nextToken = UNDERSCORE;
                break;
            case '.':
                addCurrentChar();
                nextToken = DOT;
                break;
            default:
                addCurrentChar();
                nextToken = EOF;
                break;
        }
    }

    // Put the Program into a single line
    public static String parseFile(String file){
        String code = "";
        try {
            Scanner scanner = new Scanner(new File("Input_Files/"+file));
            while (scanner.hasNextLine()){
                code += scanner.nextLine() + " \n";
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("File not found: "+e.getMessage());
        }
        return code;
    }
    
    //validate if lexeme is not reserved words
    public static Boolean checkReserved(String lexeme){
        for (String s : RESERVED_WORDS ){
            if (lexeme!=null && lexeme.equals(s)){
                return true;
            }
        }
        return false;
    }

    public static void setReservedToken(){
        switch (nextLexeme) {
            case "program": 
                nextToken = RESERVED_PROGRAM;
                break;
            case "begin":   
                nextToken = RESERVED_BEGIN;
                break;
            case "end":     
                nextToken = RESERVED_END;
                break;
            case "if":      
                nextToken = RESERVED_IF;
                break;
            case "then":    
                nextToken = RESERVED_THEN;
                break;
            case "else":    
                nextToken = RESERVED_ELSE;
                break;
            case "input":   
                nextToken = RESERVED_INPUT;
                break;
            case "output":  
                nextToken = RESERVED_OUTPUT;
                break;
            case "while":   
                nextToken = RESERVED_WHILE;
                break;
            case "loop":    
                nextToken = RESERVED_LOOP;
                break;
            case "int":     
                nextToken = RESERVED_INT;
                break;
            case "float":   
                nextToken = RESERVED_FLOAT;
                break;
            case "double":  
                nextToken = RESERVED_DOUBLE;
                break;
            case "call":    
                nextToken = RESERVED_CALL;
                break;
        }
    }

    //Lex
    public static int lex(){
        nextLexeme = "";
        getNonBlank(); //
        switch (charClass){
            case UNKNOWN:
                lookup(nextChar);
                if (nextToken != UNDERSCORE){
                    getNextChar();
                    break;
                }
                getNextChar();
            case(LETTER): // Case of Indentifier
                addCurrentChar();
                getNextChar();
                while (charClass == LETTER || charClass == DIGIT || (charClass == UNKNOWN && nextChar=='_')){
                    if (charClass == UNKNOWN){
                        lookup(nextChar);
                        if (nextToken == UNDERSCORE){
                            getNextChar();
                            continue;
                        } else{
                            //THROW ERROR
                        }
                    }
                    addCurrentChar();
                    getNextChar();
                }
                if (checkReserved(nextLexeme)){
                    setReservedToken();
                } else {nextToken = IDENT;}
                break;
            case DIGIT: // Case of Integer Literal (all Digit)
                boolean dot = false;
                addCurrentChar();
                getNextChar();
                while (charClass == DIGIT || (charClass == UNKNOWN && nextChar=='.')){
                    //System.out.println(nextChar);
                    if (charClass == UNKNOWN){
                        lookup(nextChar);
                        if (nextToken == DOT){
                            if (dot == true){
                                //THROW ERROR MULTIPLE DOTS
                            }
                            getNextChar();
                            dot = true;
                            continue;
                        } else {
                            //THROW ERROR INVALID UNKNOWN
                        }
                    }
                    addCurrentChar();
                    getNextChar();
                }
                int digits = nextLexeme.replaceAll("\\D","").length();
                if (digits > 10){
                    //THROW ERROR
                }
                if (nextLexeme.contains(".")){
                    if (digits <= 7){nextToken = FLOAT_LIT;}
                    else {nextToken = DOUBLE_LIT;}
                } else {
                    nextToken = INT_LIT;
                }
                break;
            case EOF:
                nextToken = EOF;
                nextLexeme = "EOF";
                break;
        }
        System.out.printf("Next Token is %d. Next Lexeme is %s\n", nextToken, nextLexeme);
        return nextToken;
    }

    public static void main(String[] args){
        //[File Name].txt
        program = parseFile("input1.txt");
        //program = "__19191_";
        System.out.println(program);
        System.out.printf("Line %d\n",lineNumber);
        if (program.isEmpty()){
            System.out.println("No Code");
        } else{
            getNextChar();
            do{
                lex();
            } while (nextToken != EOF);
        }
    }
}
