package frontEndCompilador.analizeLexica

enum Token {
    ABRE_PARENTESES(4, '('),
    FECHA_PARENTESES(5, ')'),
    ABRE_CHAVE(6, '{'),
    FECHA_CHAVE(7, '}'),
    ABRE_COLCHETE(8, '['),
    FECHA_COLCHETE(9, ']'),
    ASPA_DUPLA(10, '"'),
    ATRIBUICAO_2(11, '<-'),
    DOIS_PONTOS(12, ':'),
    PONTO_VIRGULA(13, ';'),
    PONTO(14, '.'),
    ATRIBUICAO(15, '='),
    IGUALDADE(16, '=='),
    COMENTARIO_LINHA(17, '--'),
    ABRE_COMENTARIO_BLOCO(18, '(*'),
    FECHA_COMENTARIO_BLOCO(19, '\\*\\)'),
    CLASS(20, 'class'),
    ELSE(21, 'else'),
    FALSE(22, 'false'),
    FI(23, 'fi'),
    IF(24, 'if'),
    IN(25, 'in'),
    INHERITS(26, 'inherits'),
    ISVOID(27, 'isvoid'),
    LET(28, 'let'),
    LOOP(29, 'loop'),
    POOL(30, 'pool'),
    THEN(31, 'then'),
    WHILE(32, 'while'),
    CASE(33, 'case'),
    ESAC(34, 'esac'),
    NEW(35, 'new'),
    OF(36, 'of'),
    NOT(37, 'not'),
    TRUE(38, 'true'),
    SELF(39, 'self'),
    SELF_TYPE(40, 'SELF_TYPE'),
    VIRGULA(41, ','),
    ARROBA(42, '@'),
    SOMA(43, '\\+'),
    SUBTRACAO(44,'-')

    int id
    String simb

    Token(int id, String simb) {
        this.id = id
        this.simb = simb
    }

    static Token obtemToken(String string) {
        return values().find { Token token -> token.simb == string }
    }
}