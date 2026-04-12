# Hawk_Parser
### CSCI 4355 - Programming Language Concepts | Spring 2026

A scanner and recursive descent parser called Hawk Program, implemnented in java. See the last section for an explaination and or application of the rules.

---
## Project Structure
```
TERMPROJECT/
|── Main.java
├── Input_Files/
│   ├── input1.txt
│   └── ...
├── Output_Files/
│   ├── output_input1.txt
│   └── ...
└── README.md
```
---
## How to Run

1. Within the Input_Files folder create or edit the source code following the Hawk Program Syntax. The files within this folder must be a .txt file. **THE `parseFile(filename)` FUNCTION ONLY WORKS FOR FILES WITHIN THE `Input_Folder`**.

2. Within the main function of the main file set the variable "filename" equal to the the name of the desired file to run. Alternatively you can hard code the source code to the variable "program".

3. After running the output of will be sent out as a .txt file within the Output_Files folder, this is done through the writeFile function which automatically runs after the parser finishes / breaks out. The name of the output file is this: "output_\[filename].txt"

---
## Example
### Main Function
```
    public static void main(String[] args){
        String filename = "input1"; // <-------- EDIT THE FILE NAME
        program = parseFile(filename); // <-------- OR HARDCODE PROGRAM

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
        //             System.out.printf
    //("Next Token is %d. Next Lexeme is %s\n", nextToken, nextLexeme);
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
```
### input1.txt
```


program    x, y: int;

begin
    input x,y;
    y:=x+y;
     output y;
end;


```
### output_input1.txt
```
PROGRAM
DECL_SEC
DECL
ID_LIST
ID_LIST
STMT_SEC
STMT
INPUT
ID_LIST
ID_LIST
STMT_SEC
STMTa
ASSIGN
EXPR
FACTOR
OPERAND
EXPR
FACTOR
OPERAND
STMT_SEC
STMT
OUTPUT
ID_LIST

```
---

## Hawk Language
```
Rule 01: PROGRAM → program DECL_SEC begin STMT_SEC end; | 
                   program begin STMT_SEC end;
Rule 02: DECL_SEC → DECL | 
                    DECL DECL_SEC
Rule 03: DECL → ID_LIST : TYPE ;
Rule 04: ID_LIST → ID | 
                   ID , ID_LIST
Rule 05: ID → (_ | a | … | z | A | … | Z)...
              (_ | a | … | z | A | … | Z | 0 | … | 9)*
Rule 06: STMT_SEC → STMT |
                    STMT STMT_SEC
Rule 07: STMT → ASSIGN | 
                IFSTMT | 
                WHILESTMT | 
                INPUT | 
                OUTPUT | 
                FUNC;
Rule 08: ASSIGN → ID := EXPR ;
Rule 09: IFSTMT → if COMP then STMT_SEC end if ; |
                  if COMP then STMT_SEC else STMT_SEC end if ;
Rule 10: WHILESTMT → while COMP loop STMT_SEC end loop ;
Rule 11: INPUT → input ID_LIST;
Rule 12: OUTPUT → output ID_LIST | 
                  output NUM;
Rule 13: EXPR → FACTOR | 
                FACTOR + EXPR | 
                FACTOR - EXPR
Rule 14: FACTOR → OPERAND | 
                  OPERAND * FACTOR | 
                  OPERAND / FACTOR
Rule 15: OPERAND → NUM | ID | ( EXPR ) | FUNC
Rule 16: NUM → (0 | 1 | ... | 9)+[.(0 | 1 | … | 9)+]
Rule 17: COMP → ( OPERAND = OPERAND ) | 
                ( OPERAND <> OPERAND ) |
                ( OPERAND > OPERAND ) | 
                ( OPERAND < OPERAND )
Rule 18: TYPE → int | float | double
Rule 19: FUNC → call ID( ID_LIST ) | 
                call ID(NUM)
```


### Reserved Words
`program`, `begin`, `end`, `if`, `then`, `else`, `input`, `output`, `while`, `loop`, `int`, `float`, `double`, `call`
 
### Operators
```
Assignment      | :=
Add             | + 
Subtract        | -
Multiply        | * 
Divide          | / 
Less Than       | <
Greater Than    | >
Equals          | =
Not Equals      | <> 
```


### Tokens
| Token | Code |
|---|---|
| INT_LIT | 10 |
| FLOAT_LIT | 11 |
| DOUBLE_LIT | 12 |
| IDENT | 13 |
| COLON | 14 |
| SEMICOLON | 15 |
| COMMA | 16 |
| ASSIGN_OP | 20 |
| ADD_OP | 21 |
| SUB_OP | 22 |
| MULT_OP | 23 |
| DIV_OP | 24 |
| LEFT_PAREN | 25 |
| RIGHT_PAREN | 26 |
| LESS_THAN | 27 |
| GREATER_THAN | 28 |
| EQUALS | 29 |
| NOT_EQUAL | 30 |
| KEY_PROGRAM | 40 |
| KEY_BEGIN | 41 |
| KEY_END | 42 |
| KEY_IF | 43 |
| KEY_THEN | 44 |
| KEY_ELSE | 45 |
| KEY_INPUT | 46 |
| KEY_OUTPUT | 47 |
| KEY_WHILE | 48 |
| KEY_LOOP | 49 |
| KEY_INT | 50 |
| KEY_FLOAT | 51 |
| KEY_DOUBLE | 52 |
| KEY_CALL | 53 |

---
## Explaination and Aplication

### Numbers
- **Integer** — up to 10 digits, no decimal point
- **Float** — up to 7 significant digits with a decimal point
- **Double** — 8 to 10 significant digits with a decimal point
- Any time there is a decimal point, there has to be a number before and after it
 
### Identifiers
- Must start with a letter or underscore
- Can contain letters, digits, and underscores
- Cannot be a reserved word

### Symbol Table
The symbol table is implemented using a HASHMAP<String, String>. After each DECL_SEC, the identifiers declared (which are stored in an array list called "tempIDs") are inserted into the HASHMAP; if and only if the identifier is not already a key within it.

### Error Handling
In the case of an error occuring within the program, the parser will break out and return the error within the output. 

For the Illegal Identifier handling, the parser is built such that it will not recognize an illegal identifier because it avoids it, passing the responsibility to the Parse Error handler. This is done during the lexical analyzer portion, which assigns tokens using the rules / reserved words table. Hence there being no error for identfiers since the only error for one is when ther is number for the first character (splits into two lexemes) and is a reserved word. 
#### Example
- lexeme 20.1myVar will return the FLOAT_LIT token for 20.1 then the IDENT token for myVar
- lexeme program will return the token RESERVED_WORD
- lexeme .myVar will return the DOT token and IDENT Token for myVar

### Errors handled


| Error | Description |
|---|---|
| Illegal symbol | Character not part of the language |
| Illegal identifier | N/A, not handled normally |.
| Illegal number | Number exceeds 10 digits, has multiple decimals, or a digit does not follow after dot |
| Redeclaration | Variable declared more than once during the DECL_SEC function|
| Undeclared identifier | Variable used before being declared when the variable is used and is not a key within the SYMBOL_TABLE |
| Parse error | Token sequence does not match grammar, Expected x Received Y |
 

---

