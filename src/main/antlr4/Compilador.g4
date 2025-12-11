grammar Compilador;

@header {
package com.compilador.gramatica;
}

// Fragmentos
fragment LETRA : [A-Za-z] ;
fragment DIGITO : [0-9] ;

// Símbolos
PYC : ';' ;
PA  : '(' ;
PC  : ')' ;
LLA : '{' ;
LLC : '}' ;
CA : '[' ;
CC : ']' ;
EQ : '=' ;
COMA  : ',' ;

// Operadores aritméticos
SUMA  : '+' ;
RESTA : '-' ;
MULT  : '*' ;
DIV   : '/' ;
MOD   : '%' ;

// Operadores lógicos
NEG : '!';
AND : '&&' ;
OR : '||';

// Operadores relacionales
DEQ : '==' ;
NEQ : '!=' ;
GT : '>' ;
LT : '<';
GTE : '>=' ;
LTE : '<=';

// Operadores incremento/decremento
INC : '++';
DEC : '--';

// Palabras reservadas - estructuras de control
IF : 'if' ;
ELSE: 'else';
FOR : 'for' ;
WHILE: 'while';
BREAK: 'break';
CONTINUE: 'continue';
RETURN : 'return';

// Palabras reservadas - tipos de datos
INT : 'int' ;
CHAR : 'char';
DOUBLE : 'double';
FLOAT : 'float';
VOID: 'void';
BOOL: 'bool';
STRING: 'string';

// Literales booleanos
TRUE: 'true';
FALSE: 'false';

// Literales
NUMERO : DIGITO+ ;
DECIMAL : DIGITO+ '.' DIGITO+ ;
CHAR_LITERAL : '\'' . '\'' ;

// Identificadores
ID : (LETRA | '_')(LETRA | DIGITO | '_')* ;

// Whitespace
WS : [ \t\n\r] -> skip ;

// Comentarios
COMMENT_LINE : '//' ~[\r\n]* -> skip ;
COMMENT_BLOCK : '/*' .*? '*/' -> skip ;

// ===== REGLAS GRAMATICALES =====

programa : instrucciones_globales EOF ;

instrucciones_globales : instruccion_global instrucciones_globales
                       |
                       ;

instruccion_global : declaracion
                   | asignacion
                   ;

instrucciones : instruccion instrucciones
              |
              ;

instruccion : bloque
            | asignacion
            | declaracion
            | call_f
            | return_i
            | while_i
            | for_i
            | if_i
            | break_i
            | continue_i
            ;

bloque : LLA instrucciones LLC ;

return_i : RETURN expresion PYC
         | RETURN PYC
         ;

while_i : WHILE PA expresion PC instruccion ;

for_i : FOR PA expresion_for PYC expresion_for PYC expresion_for PC instruccion ;

if_i : IF PA expresion PC instruccion else_if;

else_if : ELSE instruccion
        |
        ;

break_i : BREAK PYC ;

continue_i : CONTINUE PYC ;

expresion_for : exp_for listaexpfor
              |
              ;

exp_for : expresion
        | asignacion_npyc
        ;

listaexpfor : COMA exp_for listaexpfor
            |
            ;

asignacion_npyc : ID EQ expresion
                | ID CA expresion CC EQ expresion
                ;

asignacion : asign_type listaasign PYC
           ;

asign_type : ID EQ expresion
           | ID CA expresion CC EQ expresion
           | id_factor
           ;

listaasign : COMA asign_type listaasign
           |
           ;

declaracion : tipodato ID dec_types ;

dec_types : dec_var
          | dec_array
          | dec_f_pyc
          | dec_f_blk
          ;

dec_var : inicializacion listaid PYC ;

dec_array : CA NUMERO CC inicializacion_array PYC ;

inicializacion_array : EQ LLA lista_valores LLC
                     |
                     ;

lista_valores : expresion lista_valores_rest
              |
              ;

lista_valores_rest : COMA expresion lista_valores_rest
                   |
                   ;

dec_f_pyc : dec_f_params PYC ;

dec_f_blk : dec_f_params bloque ;

dec_f_params : PA params_f PC ;

tipodato : INT
         | FLOAT
         | DOUBLE
         | CHAR
         | VOID
         | BOOL
         | STRING
         ;

inicializacion : EQ expresion
               |
               ;

listaid : COMA ID inicializacion listaid
        | COMA ID CA NUMERO CC listaid
        |
        ;

params_f : tipodato ID listaparams_f
         |
         ;

listaparams_f : COMA tipodato ID listaparams_f
              |
              ;

call_f : ID PA params_call_f PC PYC ;

call_f_factor : ID PA params_call_f PC ;

params_call_f : expresion listaexpresion
              |
              ;

expresion : expresion_or ;

expresion_or : expresion_or OR expresion_and
             | expresion_and
             ;

expresion_and : expresion_and AND expresion_neg
              | expresion_neg
              ;

expresion_neg : NEG expresion_neg
              | expresion_comp
              ;

expresion_comp : expresion_comp comparaciones expresion_arit
               | expresion_arit
               ;

comparaciones : LT | LTE | GT | GTE | DEQ | NEQ ;

expresion_arit : expresion_arit SUMA termino
               | expresion_arit RESTA termino
               | termino
               ;

listaexpresion : COMA expresion listaexpresion
               |
               ;

termino : termino MULT factor
        | termino DIV factor
        | termino MOD factor
        | factor
        ;

factor : sign_factor NUMERO
       | sign_factor DECIMAL
       | sign_factor ID
       | sign_factor ID CA expresion CC
       | id_factor
       | sign_factor call_f_factor
       | sign_factor PA expresion PC
       | TRUE
       | FALSE
       | CHAR_LITERAL
       ;

id_factor : inc_dec ID
          | ID inc_dec
          ;

inc_dec : INC | DEC ;

sign_factor : SUMA | RESTA | ;
