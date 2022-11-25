package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class Feature extends RegraEstrutura {
    private List<Closure<Boolean>> sequenciasValidas = [
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.ABRE_PARENTESES && anterior == TokenPreDefinido.IDENTIFICADOR
            },
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.IDENTIFICADOR &&
                        (anterior == null || anterior == TokenPreDefinido.VIRGULA)
            },
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.DOIS_PONTOS && anterior == TokenPreDefinido.FECHA_PARENTESES
            },
            { TokenPreDefinido anterior, TokenPreDefinido token ->
                token == TokenPreDefinido.PONTO_VIRGULA && anterior == TokenPreDefinido.FECHA_CHAVE
            },
    ]


    Feature() {
        super([
                new DTOHashToken(TokenPreDefinido.ABRE_PARENTESES, { -> new RFormal() }),
                new DTOHashToken(TokenPreDefinido.FECHA_PARENTESES, { -> null }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),
                new DTOHashToken(TokenPreDefinido.ATRIBUICAO, { -> new Expressao() }),
        ])
    }

    @Override
    protected void validacaoSequenciaTokens(NodeToken nodeProximaEtapa) {
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

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        if(TokenPreDefinido.obtemToken(dtoToken.desc) == TokenPreDefinido.PONTO_VIRGULA) {
            desfazProcessoAdicaoPilha()
            nodeToken.dtosDaMesmaRegra.removeLast()
            return true
        }
        return false
    }
}
