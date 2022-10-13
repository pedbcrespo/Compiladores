package frontEndCompilador.enums

enum TokenPreDefinido {
    IDENTIFICADOR(null),
    TEXTO(null),
    COMENTARIO(null),
    ABRE_PARENTESES('('),
    FECHA_PARENTESES(')'),
    ABRE_CHAVE('{'),
    FECHA_CHAVE('}'),
    ABRE_COLCHETE('['),
    FECHA_COLCHETE(']'),
    ASPA_DUPLA('"'),
    ATRIBUICAO('<-'),
    DOIS_PONTOS(':'),
    PONTO_VIRGULA(';'),
//    PONTO('.'),
    IGUALDADE('='),
    COMENTARIO_LINHA('--'),
    ABRE_COMENTARIO_BLOCO('(*'),
    FECHA_COMENTARIO_BLOCO('*)'),
    CLASS('class'),
    ELSE('else'),
    FALSE('false'),
    FI('fi'),
    IF('if'),
    IN('in'),
    INHERITS('inherits'),
    ISVOID('isvoid'),
    LET('let'),
    LOOP('loop'),
    POOL('pool'),
    THEN('then'),
    WHILE('while'),
    CASE('case'),
    ESAC('esac'),
    NEW('new'),
    OF('of'),
    NOT('not'),
    TRUE('true'),
    SELF('self'),
    SELF_TYPE('SELF_TYPE'),
    VIRGULA(','),
    ARROBA('@'),
    SOMA('+'),
    SUBTRACAO('-'),
    ASTERISTICO('*'),
    IGUAL('='),
    DIVISAO('\\'),
    MAIOR('>'),
    MENOR('<'),
    MAIOR_IGUAL('>='),
    MENOR_IGUAL('<=')

    String simb

    TokenPreDefinido(String simb) {
        this.simb = simb
    }

    static TokenPreDefinido obtemToken(String string) {
        TokenPreDefinido tokenBuscado = values().find { TokenPreDefinido token -> token.simb == string || token.name() == string}
        return tokenBuscado
    }

    static TokenPreDefinido tokenPersonalisado(String simb, TokenPreDefinido tokenEscolhido) {
        tokenEscolhido.simb = simb
        return tokenEscolhido
    }

}