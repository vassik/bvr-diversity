grammar ProductLine;

productLine
    :   features constraints            
    ;

features
    :   'features' ':' ID (',' ID)*
    ;

constraints
    :   'constraints' ':' ('-' constraint)+
    ;

constraint
    :   left=constraint 'and' right=constraint          # Conjunction
    |   left=constraint 'or'  right=constraint          # Disjunction
    |   left=constraint 'implies' right=constraint      # Implication
    |   'not' operand=constraint                        # Negation
    |   ID                                              # FeatureReference
    |   '(' constraint ')'                              # WithParentheses
    ;


fragment
DIGIT   
    :   [0-9] 
    ;

LETTER  
    :   [a-zA-Z\u0080-\u00FF_]
    ;


INTEGER
    :   DIGIT+
    ;

ID      
    :   LETTER(LETTER|DIGIT|'-')+
    ;

WS
    :   [ \t\n\r]+ -> skip 
    ;

LINE_COMMENT
    : '#' ~[\r\n]* -> channel(HIDDEN)
    ;