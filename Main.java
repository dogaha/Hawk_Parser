import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.print.DocFlavor.STRING;


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
    static ArrayList<String> tempIDs = new ArrayList<>();
    // Classes
    static final int LETTER = 0;
    static final int DIGIT = 1;
    static final int NUMBER = 2;
    static final int UNKNOWN = 99;

    // Tokens Code
    static final int INT_LIT = 10;
    static final int FLOAT_LIT = 11;
    static final int DOUBLE_LIT = 12;
    static final int IDENT = 13;
    static final int COLON = 14;
    static final int SEMICOLON = 15;
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
    static String programOutput = "";

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
        while (Character.isWhitespace(nextChar) && charClass!=EOF) {getNextChar();}
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
                nextToken = SEMICOLON;
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
                throwError("CHAR");
                addCurrentChar();
                nextToken = EOF;
                break;
        }
    }

    //Set Reserved Work Token
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

    // Put the Program into a single line
    public static String parseFile(String filename){
        String code = "";
        try {
            Scanner scanner = new Scanner(new File("Input_Files/"+filename+".txt"));
            while (scanner.hasNextLine()){
                code += scanner.nextLine() + " \n";
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("File not found: "+e.getMessage());
        }
        return code;
    }
    
    public static void writeFile(String content, String filename){
        try (FileWriter writer = new FileWriter("Output_Files/"+"output_"+filename+".txt")) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    //Lex
    public static int lex(){
        nextLexeme = "";
        getNonBlank(); //
        switch (charClass){
            case UNKNOWN:
                lookup(nextChar);
                if (nextToken == UNDERSCORE || nextToken == DOT){
                    getNextChar();
                }else{
                    getNextChar();
                    break;
                }
            case(LETTER): // Case of Indentifier
                if (charClass == EOF){
                    break;
                } else if (nextToken == DOT){
                    //MOVE DOWN TO DIGIT
                }else {
                    addCurrentChar();
                    getNextChar();
                    while ((charClass == LETTER || charClass == DIGIT || nextChar=='_') && charClass!=EOF){
                        if (nextChar=='_'){
                            lookup(nextChar);
                            if (nextToken == UNDERSCORE){
                                getNextChar();
                                continue;
                            }
                        }
                        addCurrentChar();
                        getNextChar();
                    }
                    if (checkReserved(nextLexeme)){
                        setReservedToken();
                    } else {nextToken = IDENT;}
                    break;   
                }
            case DIGIT: // Case of Integer Literal (all Digit)
                addCurrentChar();
                getNextChar();
                while ((charClass == DIGIT || nextChar=='.')&& charClass!=EOF){
                    //System.out.println(nextChar);
                    if (nextChar=='.'){
                        lookup(nextChar);
                        if (nextToken == DOT){
                            getNextChar();
                            continue;
                        }
                    }
                    addCurrentChar();
                    getNextChar();
                }
                int digits = nextLexeme.replaceAll("\\D","").length();
                if (digits > 10){
                    throwError("NUMBER_LEN");
                }
                if (nextLexeme.contains(".")){
                    if (nextLexeme.length() - nextLexeme.replace(".","").length()>1){
                        throwError("NUMBER_DOT");
                    }
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
        //System.out.printf("Next Token is %d. Next Lexeme is %s\n", nextToken, nextLexeme);
        return nextToken;
    }

    //Validate
    public static void validateToken(int... expectedTokens){
        
        for (int expectedToken : expectedTokens){
            if (nextToken == expectedToken){
                lex();
                return;
            }
        }
        if (expectedTokens[0] == RESERVED_INT){
            throwError(NUMBER);
        }
        throwError(expectedTokens[0]);
    }

    public static void validateToken(int expectedToken){
        if (nextToken == expectedToken){
                lex();
        } else {
            throwError(expectedToken);
        }
    }

    public static void validateIDENT(){
        if (nextToken == IDENT){
            //System.out.print(nextLexeme+" | ");
            //System.out.println(SYMBOL_TABLE.containsKey(nextLexeme));
            if (!SYMBOL_TABLE.containsKey(nextLexeme)){
                throwError("UNDECL");
            }
            lex();
        } else {
            throwError(IDENT);
        }
    }
    // Throw Error
    public static void throwError(int expectedToken){
        String errorMessage = "ERROR: LINE " + lineNumber + " : Expected ";
        
        switch (expectedToken){
            case INT_LIT:
                errorMessage += "integer literal";
                break;
            case FLOAT_LIT:
                errorMessage += "float literal";
                break;
            case DOUBLE_LIT:
                errorMessage += "double literal";
                break;
            case NUMBER:
                errorMessage += "'int', 'float', or 'double'";
                break;
            case IDENT:
                errorMessage += "identifier";
                break;
            case COLON:
                errorMessage += "':'";
                break;
            case SEMICOLON:
                errorMessage += "';'";
                break;
            case COMMA:
                errorMessage += "','";
                break;
            case DOT:
                errorMessage += "'.'";
                break;
            case UNDERSCORE:
                errorMessage += "'_'";
                break;
            case ASSIGN_OP:
                errorMessage += "':='";
                break;
            case ADD_OP:
                errorMessage += "'+'";
                break;
            case SUB_OP:
                errorMessage += "'-'";
                break;
            case MULT_OP:
                errorMessage += "'*'";
                break;
            case DIV_OP:
                errorMessage += "'/'";
                break;
            case LEFT_PAREN:
                errorMessage += "'('";
                break;
            case RIGHT_PAREN:
                errorMessage += "')'";
                break;
            case LESS_THAN:
                errorMessage += "'<'";
                break;
            case GREATER_THAN:
                errorMessage += "'>'";
                break;
            case EQUALS:
                errorMessage += "'='";
                break;
            case NOT_EQUAL:
                errorMessage += "'<>'";
                break;
            case RESERVED_PROGRAM:
                errorMessage += "'program'";
                break;
            case RESERVED_BEGIN:
                errorMessage += "'begin'";
                break;
            case RESERVED_END:
                errorMessage += "'end'";
                break;
            case RESERVED_IF:
                errorMessage += "'if'";
                break;
            case RESERVED_THEN:
                errorMessage += "'then'";
                break;
            case RESERVED_ELSE:
                errorMessage += "'else'";
                break;
            case RESERVED_INPUT:
                errorMessage += "'input'";
                break;
            case RESERVED_OUTPUT:
                errorMessage += "'output'";
                break;
            case RESERVED_WHILE:
                errorMessage += "'while'";
                break;
            case RESERVED_LOOP:
                errorMessage += "'loop'";
                break;
            case RESERVED_INT:
                errorMessage += "'int'";
                break;
            case RESERVED_FLOAT:
                errorMessage += "'float'";
                break;
            case RESERVED_DOUBLE:
                errorMessage += "'double'";
                break;
            case RESERVED_CALL:
                errorMessage += "'call'";
                break;
            case EOF:
                errorMessage = "Unexpected end of file";
                break;
            default:
                errorMessage = "Unknown token";
                break;
        }
        errorMessage += " Received '"+nextLexeme+"'";
        throw new RuntimeException(errorMessage);
    }

    public static void throwError(String error){
        String errorMessage = "ERROR: LINE " + lineNumber + " : ";
        switch(error){
            case "NUMBER_LEN":
                errorMessage += "Numbers cannot excede 10 digits, '" +nextLexeme+"'";
                break;
            case "NUMBER_DOT":
                errorMessage += "Numbers can only have one decimal, '"+nextLexeme+"'";
                break;
            case "CHAR":
                errorMessage += "'"+nextChar+" is not a useable character";
                break;
            case "REDECL":
                errorMessage += "'"+nextLexeme+"' has already been declared";
                break;
            case "OPERAND":
                errorMessage += "Expected a number, identifier, ( EXPRESSION ), or function Received '"+nextLexeme+"'";
                break;
            case "STMT":
                errorMessage += "Expected a statement Received '"+nextLexeme+"'";
                break;
            case "UNDECL":
                errorMessage += "'"+nextLexeme+"' is an undeclared identifier";
                break;
            case "DECL":
                errorMessage +=  "'"+nextLexeme+"' is a not a valid identifier name";
                break;
            case "FUNC_PARAM":
                errorMessage +=  "Expected a number or identifier list Received '"+nextLexeme+"'";
                break;
        }
        throw new RuntimeException(errorMessage);
    }

    // Functions
    public static void PROGRAM(){
        System.out.println("PROGRAM");
        programOutput+="PROGRAM\n";
        validateToken(RESERVED_PROGRAM);

        DECL_SEC();

        validateToken(RESERVED_BEGIN);

        // STATEMENT SECTION
        STMT_SEC();

        validateToken(RESERVED_END);

        validateToken(SEMICOLON);
    }

    public static void DECL_SEC(){
        while (nextToken != RESERVED_BEGIN){
            System.out.println("DECL_SEC");
            programOutput+="DECL_SEC\n";
            DECL();
        }
    }

    public static void DECL(){
        System.out.println("DECL");
        programOutput+="DECL\n";
        ID_LIST("DECL");
        validateToken(COLON);
        String type = nextLexeme;
        validateToken(RESERVED_INT, RESERVED_FLOAT, RESERVED_DOUBLE);
        validateToken(SEMICOLON);
        
        for (String id : tempIDs){
            //Check for REDECLARES
            if (SYMBOL_TABLE.containsKey(id)){
                String errorMessage = "ERROR: LINE " + lineNumber + " : Identifier";
                errorMessage += "'"+id+"' has already been declared";
                throw new RuntimeException(errorMessage);
            }
            SYMBOL_TABLE.put(id, type);
        }
        tempIDs.clear();
    }

    public static void ID_LIST(String function){
        System.out.println("ID_LIST");
        programOutput+="ID_LIST\n";
        if (function == "DECL"){
            //Declaring IDENT
            tempIDs.add(nextLexeme);
            validateToken(IDENT);
            if (nextToken != COLON){
                validateToken(COMMA);
                ID_LIST(function);
            }
        } else if (function == "INPUT" || function == "OUTPUT") {
            //Using IDENT
            validateIDENT();
            if (nextToken != SEMICOLON){
                validateToken(COMMA);
                ID_LIST(function);
            }
        } else if (function == "FUNC"){
            validateIDENT();
            if (nextToken != RIGHT_PAREN){
                validateToken(COMMA);
                ID_LIST(function);
            }
        }
    }

    public static void STMT_SEC() {
        while (nextToken != RESERVED_END && nextToken != RESERVED_ELSE){
            System.out.println("STMT_SEC");
            programOutput+="STMT_SEC\n";
            STMT();
        }
    }

    public static void STMT() {
        System.out.println("STMT");
        programOutput+="STMT\n";
        switch (nextToken){
            case(IDENT):
                ASSIGN();
                break;
            case(RESERVED_IF):
                IFSTMT();
                break;
            case(RESERVED_WHILE):
                WHILESTMT();
                break;
            case(RESERVED_INPUT):
                INPUT();
                break;
            case(RESERVED_OUTPUT):
                OUTPUT();
                break;
            case(RESERVED_CALL):
                FUNC();
                validateToken(SEMICOLON);
                break;
            default:
                throwError("STMT");
                break;
        }
    }

    public static void ASSIGN() {
        System.out.println("ASSIGN");
        programOutput+="ASSIGN\n";
        validateIDENT();
        validateToken(ASSIGN_OP);
        EXPR();
        validateToken(SEMICOLON);
    }

    public static void IFSTMT() {
        System.out.println("IF_STMT");
        programOutput+="IF_STMT\n";
        validateToken(RESERVED_IF);
        COMP();
        validateToken(RESERVED_THEN);
        STMT_SEC();
        if (nextToken == RESERVED_ELSE){
            lex();
            STMT_SEC();
        }
        validateToken(RESERVED_END);
        validateToken(RESERVED_IF);
        validateToken(SEMICOLON);
    }

    public static void WHILESTMT() {
        System.out.println("WHILE_STMT");
        programOutput+="WHILE_STMT\n";
        validateToken(RESERVED_WHILE);
        COMP();
        validateToken(RESERVED_LOOP);
        STMT_SEC();
        validateToken(RESERVED_END);
        validateToken(RESERVED_LOOP);
        validateToken(SEMICOLON);
    }

    public static void INPUT() {
        System.out.println("INPUT");
        programOutput+="INPUT\n";
        validateToken(RESERVED_INPUT);
        ID_LIST("INPUT");
        validateToken(SEMICOLON);
    }

    public static void OUTPUT() {
        System.out.println("OUTPUT");
        programOutput+="OUTPUT\n";
        validateToken(RESERVED_OUTPUT);
        if (nextToken == IDENT){
            ID_LIST("OUTPUT");
        } else if (nextToken == INT_LIT || nextToken == FLOAT_LIT || nextToken == DOUBLE_LIT){
            lex();
        } else {
            throwError("FUNC_PARAM"); // nothing valid found
        }
        validateToken(SEMICOLON);
    }

    public static void EXPR() {
        System.out.println("EXPR");
        programOutput+="EXPR\n";
        FACTOR();
        if (nextToken == ADD_OP || nextToken == SUB_OP){
            lex();
            EXPR();
        }
    }

    public static void FACTOR() {
        System.out.println("FACTOR");
        programOutput+="FACTOR\n";
        OPERAND();
        if (nextToken == MULT_OP || nextToken == DIV_OP){
            lex();
            FACTOR();
        }
    }

    public static void OPERAND() {
        System.out.println("OPERAND");
        programOutput+="OPERAND\n";
        if (nextToken == INT_LIT || nextToken == FLOAT_LIT || nextToken == DOUBLE_LIT) {
            lex();
        } else if(nextToken == IDENT){
            validateIDENT();
        }else if (nextToken == LEFT_PAREN) {
            validateToken(LEFT_PAREN);
            EXPR();
            validateToken(RIGHT_PAREN);
        } else if (nextToken == RESERVED_CALL) {
            FUNC();
        } else {
            throwError("OPERAND"); // nothing valid found
        }
        
    }

    public static void COMP() {
        System.out.println("COMP");
        programOutput+="COMP\n";
        validateToken(LEFT_PAREN);
        OPERAND();
        validateToken(EQUALS,NOT_EQUAL,GREATER_THAN,LESS_THAN);
        OPERAND();
        validateToken(RIGHT_PAREN);
    }

    public static void FUNC() {
        System.out.println("FUNC");
        programOutput+="FUNC\n";
        validateToken(RESERVED_CALL);
        validateToken(IDENT);
        validateToken(LEFT_PAREN);
        if (nextToken == IDENT){
            ID_LIST("FUNC");
        } else if (nextToken == INT_LIT || nextToken == FLOAT_LIT || nextToken == DOUBLE_LIT){
            lex();
        } else {
            throwError("FUNC_PARAM"); // nothing valid found
        }
        validateToken(RIGHT_PAREN);
    }

    public static void main(String[] args){
        String filename = "Test";
        program = parseFile(filename);

        System.out.println(program);

        // Lexical Analyzer Teseter
        // if (program.trim().isEmpty()){
        //     System.out.println("No Code");
        // } else{
        //     try{
        //         //program = """.0000""";
        //         getNextChar();
        //         do{
        //             lex();
        //             System.out.printf("Next Token is %d. Next Lexeme is %s\n", nextToken, nextLexeme);
        //         } while (nextToken != EOF);
        //     } catch (RuntimeException e) {
        //         System.err.println(e.getMessage());
        //     }
        // }

        // Parser Tester
        if (program.trim().isEmpty()){
            System.out.println("No Code");
        } else{
            getNextChar();
            lex();
            try{
                PROGRAM();
            } catch (RuntimeException e) {
                programOutput += e.getMessage() + "\n";
                System.err.println(e.getMessage());
            }
            writeFile(programOutput,filename);

        }
    }
}
