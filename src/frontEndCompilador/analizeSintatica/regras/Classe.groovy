package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class Classe extends RegraEstrutura {

    private List<TokenPreDefinido> sequenciaAceita1 = [
            TokenPreDefinido.IDENTIFICADOR,
            TokenPreDefinido.INHERITS,
            TokenPreDefinido.IDENTIFICADOR,
            TokenPreDefinido.ABRE_CHAVE
    ]

    private List<TokenPreDefinido> sequenciaAceita2 = [
            TokenPreDefinido.IDENTIFICADOR,
            TokenPreDefinido.ABRE_CHAVE,
    ]

    Classe() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.INHERITS, { -> null }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new RFeature() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null }),
        ])
    }

    Classe(DTOToken dtoToken, List<DTOHashToken> dtoHashTokens) {
        super(dtoToken, dtoHashTokens)
    }

    @Override
    protected void validacaoSequenciaTokens() {
        Boolean sequenciaValida = true
        for (int pos = 0; pos < pilhaTokensLidosPorInstancia.size(); pos++) {
            if (pos < sequenciaAceita1.size()) {
                sequenciaValida = sequenciaValida && (
                        pilhaTokensLidosPorInstancia[pos] == sequenciaAceita1[pos] ||
                                pilhaTokensLidosPorInstancia[pos] == sequenciaAceita2[pos]
                )
            } else if (pos < sequenciaAceita2.size()) {
                sequenciaValida = sequenciaValida && pilhaTokensLidosPorInstancia[pos] == sequenciaAceita2[pos]
            }
        }
        if(!sequenciaValida) {
            throw new Exception("ERRO SEQUENCIA INVALIDA: ${pilhaDtoLida[0]}")
        }
    }

//    @Override
//    protected void adicionaNodeSubArvore(){
//        RegraEstrutura instancia = new Classe(dtoTokenFornecida, chaveProximoPasso)
//        if(!nodeToken) {
//            nodeToken = new NodeToken(instancia)
//            return
//        }
//        nodeToken.addNode(instancia)
//    }
}
