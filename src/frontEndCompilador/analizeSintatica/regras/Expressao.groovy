package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOParToken
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

    Expressao() {
        super(geraListaToken())
    }

    private List<List<TokenPreDefinido>> listaCasosErro = [
            [TokenPreDefinido.ATRIBUICAO, TokenPreDefinido.PONTO_VIRGULA],
            [TokenPreDefinido.ATRIBUICAO, TokenPreDefinido.VIRGULA],
            [TokenPreDefinido.IDENTIFICADOR, TokenPreDefinido.IDENTIFICADOR],
            [TokenPreDefinido.TEXTO, TokenPreDefinido.TEXTO],
    ]

    private static List<DTOHashToken> geraListaToken() {
        return [
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
                new DTOHashToken(TokenPreDefinido.LET, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.IN, { -> null }),
                new DTOHashToken(TokenPreDefinido.CASE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.OF, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.ESAC, { -> null }),
                new DTOHashToken(TokenPreDefinido.NEW, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.SOMA, { -> null }),
                new DTOHashToken(TokenPreDefinido.SUBTRACAO, { -> null }),
                new DTOHashToken(TokenPreDefinido.ASTERISTICO, { -> null }),
                new DTOHashToken(TokenPreDefinido.DIVISAO, { -> null }),
                new DTOHashToken(TokenPreDefinido.MAIOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.MAIOR_IGUAL, { -> null }),
                new DTOHashToken(TokenPreDefinido.MENOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.MENOR_IGUAL, { -> null }),
                new DTOHashToken(TokenPreDefinido.IGUAL, { -> null }),
                new DTOHashToken(TokenPreDefinido.NOT, { -> null }),
                new DTOHashToken(TokenPreDefinido.TRUE, { -> null }),
                new DTOHashToken(TokenPreDefinido.FALSE, { -> null }),
                new DTOHashToken(TokenPreDefinido.VIRGULA, { -> null }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),

        ]
    }

    protected void validacaoSequenciaTokens(NodeToken nodeProximaEtapa) {
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

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        if(validaCasosEspecificosTokens()) {
            desfazProcessoAdicaoPilha()
            nodeToken.dtosDaMesmaRegra.removeLast()
            return true
        }
        List<DTOParToken> listaCasosEspecificos = [
                new DTOParToken(TokenPreDefinido.ABRE_PARENTESES, TokenPreDefinido.FECHA_PARENTESES),
                new DTOParToken(TokenPreDefinido.ABRE_CHAVE, TokenPreDefinido.FECHA_CHAVE)
        ]
        DTOParToken conjuntoTokenChave = listaCasosEspecificos.find { it ->
            it.correspondeChaveFecha(TokenPreDefinido.obtemToken(dtoToken.desc))
        }
        if ((!conjuntoTokenChave || !tokenChave) || casoEspecificoExpressao()) {
            return false
        } else if (conjuntoTokenChave.obtemChaveFecha(tokenChave)) {
            nodeToken.dtosDaMesmaRegra.removeLast()
            desfazProcessoAdicaoPilha()
        }
        return true
    }

    @Override
    protected TokenPreDefinido casoChavePar() {
        TokenPreDefinido token = TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        return [
                TokenPreDefinido.ABRE_PARENTESES,
                TokenPreDefinido.ABRE_CHAVE,
                TokenPreDefinido.IF,
                TokenPreDefinido.THEN,
                TokenPreDefinido.ELSE,
        ].find{ it -> it == token}
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

    private boolean casoEspecificoExpressao() {
        DTOToken dtoSegundoDaPilha = pilhaDtoLida[1]
        TokenPreDefinido tokenSegundoDaPilha = TokenPreDefinido.obtemToken(dtoSegundoDaPilha.desc)
        TokenPreDefinido tokenDtoAtual = TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        return (tokenSegundoDaPilha == TokenPreDefinido.FECHA_CHAVE &&
                tokenDtoAtual == TokenPreDefinido.PONTO_VIRGULA) ||
                (tokenDtoAtual == TokenPreDefinido.FECHA_PARENTESES &&
                        tokenChave == TokenPreDefinido.ABRE_CHAVE)
    }

    private boolean casoEspecificoIf() {
        return (tokenChave == TokenPreDefinido.IF &&
                TokenPreDefinido.obtemToken(dtoTokenFornecida.desc) == TokenPreDefinido.THEN) ||
                (tokenChave == TokenPreDefinido.THEN &&
                        TokenPreDefinido.obtemToken(dtoTokenFornecida.desc) == TokenPreDefinido.ELSE) ||
                (tokenChave == TokenPreDefinido.ELSE &&
                        TokenPreDefinido.obtemToken(dtoTokenFornecida.desc) == TokenPreDefinido.FI)
    }

    private boolean casoEspecificoLet() {
        TokenPreDefinido tokenCabeca = TokenPreDefinido.obtemToken(dtoCabeca.desc)
        return (tokenCabeca == TokenPreDefinido.LET &&
                TokenPreDefinido.obtemToken(dtoTokenFornecida.desc) == TokenPreDefinido.IN)
    }

    private boolean casoEspecificoAtribuicao() {
        TokenPreDefinido tokenCabeca = TokenPreDefinido.obtemToken(dtoCabeca.desc)
        TokenPreDefinido tokenFornecido = TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        return tokenCabeca == TokenPreDefinido.ATRIBUICAO && tokenFornecido == TokenPreDefinido.PONTO_VIRGULA
    }

    private boolean validaCasosEspecificosTokens() {
        return casoEspecificoIf() || casoEspecificoLet()
    }
}
