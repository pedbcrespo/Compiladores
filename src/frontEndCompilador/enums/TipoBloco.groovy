package frontEndCompilador.enums

enum TipoBloco {
    OPERACAO_ARIT('operacao aritmedica', [TokenPreDefinido.SOMA, TokenPreDefinido.SUBTRACAO, TokenPreDefinido.ASTERISTICO, TokenPreDefinido.DIVISAO]),
    OPERACAO_BOOL('operacao booleana', [TokenPreDefinido.MAIOR, TokenPreDefinido.MAIOR_IGUAL, TokenPreDefinido.MENOR, TokenPreDefinido.MAIOR_IGUAL]),
    ATRIBUICAO('atribuicao', [TokenPreDefinido.DOIS_PONTOS])

    String desc
    List<TokenPreDefinido> listaTokensAceitos

    TipoBloco(String desc, List<TokenPreDefinido> listaTokensAceitos) {
        this.desc = desc
        this.listaTokensAceitos = listaTokensAceitos
    }

    static TipoBloco determinaTipo(String desc) {
        return values().find { TipoBloco tipo ->
            tipo.listaTokensAceitos.contains(TokenPreDefinido.obtemToken(desc))
        }
    }
}