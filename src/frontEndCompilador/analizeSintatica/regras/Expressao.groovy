package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class Expressao extends RegraEstrutura {
    List<List<TokenPreDefinido>> sequenciasAceitas = [
            [
                    TokenPreDefinido.IDENTIFICADOR,
                    TokenPreDefinido.ATRIBUICAO,
                    TokenPreDefinido.TEXTO,
            ],
            [
                    TokenPreDefinido.IF,
                    TokenPreDefinido.THEN,
                    TokenPreDefinido.ELSE,
                    TokenPreDefinido.FI,
            ],
            [
                    TokenPreDefinido.WHILE,
                    TokenPreDefinido.LOOP,
                    TokenPreDefinido.POOL
            ],
            [
                    TokenPreDefinido.LET,
                    TokenPreDefinido.IDENTIFICADOR,
                    TokenPreDefinido.IN
            ],
            [
                    TokenPreDefinido.CASE,
                    TokenPreDefinido.OF,
                    TokenPreDefinido.IDENTIFICADOR,
                    TokenPreDefinido.ESAC
            ]
    ]

    private List<List<TokenPreDefinido>> listaCasosErro = [
            [TokenPreDefinido.ATRIBUICAO, TokenPreDefinido.PONTO_VIRGULA],
            [TokenPreDefinido.ATRIBUICAO, TokenPreDefinido.VIRGULA],
            [TokenPreDefinido.IDENTIFICADOR, TokenPreDefinido.IDENTIFICADOR],
            [TokenPreDefinido.TEXTO, TokenPreDefinido.TEXTO],
    ]

    Expressao() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.TEXTO, { -> null }),
                new DTOHashToken(TokenPreDefinido.ATRIBUICAO, { -> null }),
                new DTOHashToken(TokenPreDefinido.ABRE_PARENTESES, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_PARENTESES, { -> null }),
                new DTOHashToken(TokenPreDefinido.IF, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.THEN, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.ELSE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FI, { -> null }),
                new DTOHashToken(TokenPreDefinido.WHILE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.LOOP, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.POOL, { -> null }),
                new DTOHashToken(TokenPreDefinido.LET, { -> null }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.IN, { -> null }),
                new DTOHashToken(TokenPreDefinido.CASE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.OF, { -> null }),
                new DTOHashToken(TokenPreDefinido.ESAC, { -> null }),
                new DTOHashToken(TokenPreDefinido.NEW, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.SOMA, { -> null }),
                new DTOHashToken(TokenPreDefinido.SUBTRACAO, { -> null }),
                new DTOHashToken(TokenPreDefinido.ASTERISTICO, { -> null }),
                new DTOHashToken(TokenPreDefinido.DIVISAO, { -> null }),
                new DTOHashToken(TokenPreDefinido.NOT, { -> null }),
                new DTOHashToken(TokenPreDefinido.TRUE, { -> null }),
                new DTOHashToken(TokenPreDefinido.FALSE, { -> null }),
                new DTOHashToken(TokenPreDefinido.VIRGULA, { -> null }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null }),
        ])
    }

    protected void validacaoSequenciaTokens() {
        Boolean houveMatchTokes = false
        for (List<TokenPreDefinido> listaSequencia : sequenciasAceitas) {
            if (analizaSequencia(listaSequencia)) {
                houveMatchTokes = true
                break
            }
        }
        if (!houveMatchTokes) {
            throw new Exception("ERRO TOKEN ${pilhaDtoLida[0]} INVALIDO")
        }
    }

    private Boolean analizaSequencia(List<TokenPreDefinido> listaSequencia) {
        Boolean valido = true
        TokenPreDefinido tokenAnterior = null
        for (TokenPreDefinido token : pilhaTokensLidosPorInstancia) {
            if (!listaSequencia.contains(token)) {
                continue
            }
            valido = valido && (
                    !tokenAnterior ? true :
                            listaSequencia.findIndexOf { TokenPreDefinido tk -> tk == token } >
                                    listaSequencia.findIndexOf { TokenPreDefinido tk -> tk == tokenAnterior }
            )
            tokenAnterior = token
        }
        return valido && analizaCasosEspecificos()
    }

    private Boolean analizaCasosEspecificos() {
        TokenPreDefinido anterior = null
        for (DTOToken dto : pilhaDtoLida) {
            TokenPreDefinido tokenDto = TokenPreDefinido.obtemToken(dto.desc)
            List<TokenPreDefinido> casoErro = listaCasosErro.find {
                List<TokenPreDefinido> lista -> lista.containsAll([anterior, tokenDto])
            }
            anterior = tokenDto
            if (casoErro) {
                throw new Exception("ERRO ${pilhaDtoLida[0].desc} SEQUENCIA INVALIDA")
            }
        }
        return true
    }


}
