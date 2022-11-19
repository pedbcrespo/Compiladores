package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RFeature extends RegraEstrutura {
    private List<Closure<Boolean>> sequenciasValidas = [
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.ABRE_PARENTESES && anterior == TokenPreDefinido.IDENTIFICADOR
            },
//            { TokenPreDefinido anterior, TokenPreDefinido token ->
//                token == TokenPreDefinido.DOIS_PONTOS && anterior == TokenPreDefinido.IDENTIFICADOR
//            },
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.IDENTIFICADOR &&
                        (anterior == null || anterior == TokenPreDefinido.VIRGULA)
            },
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.DOIS_PONTOS && anterior == TokenPreDefinido.FECHA_PARENTESES
            },
    ]


    RFeature() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.ABRE_PARENTESES, { -> new RFormal() }),
                new DTOHashToken(TokenPreDefinido.FECHA_PARENTESES, { -> null }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),
                new DTOHashToken(TokenPreDefinido.ATRIBUICAO, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.VIRGULA, { -> null }),
        ])
    }

    RFeature(DTOToken dtoToken, List<DTOHashToken> dtoHashTokens) {
        super(dtoToken, dtoHashTokens)
    }

    @Override
    protected void validacaoSequenciaTokens() {
        if (pilhaTokensLidosPorInstancia.size() < 2) {
            return
        }
        TokenPreDefinido anterior = null
        Boolean algumaSequenciaValida = false
        for (TokenPreDefinido token : pilhaTokensLidosPorInstancia) {
            for (Closure<Boolean> ehSequenciaValida : sequenciasValidas) {
                algumaSequenciaValida = algumaSequenciaValida || ehSequenciaValida(anterior, token)
            }
            if (algumaSequenciaValida) {
                break
            }
            anterior = token
        }
        if (!algumaSequenciaValida) {
            throw new Exception("ERROR SEQUENCIA ${pilhaDtoLida[0].simb} INVALIDA")
        }
    }

//    @Override
//    protected void adicionaNodeSubArvore(){
//        RegraEstrutura instancia = new RFeature(dtoTokenFornecida, this.chaveProximoPasso)
//        if(!nodeToken) {
//            nodeToken = new NodeToken(instancia)
//            return
//        }
//        nodeToken.addNode(instancia)
//    }
}
