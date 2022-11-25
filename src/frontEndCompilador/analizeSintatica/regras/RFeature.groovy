package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RFeature extends RegraEstrutura {
    RFeature() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new Feature() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null }),
        ])
    }

//    @Override
//    protected void validacaoSequenciaTokens(NodeToken nodeProximaEtapa) {
//        if (pilhaTokensLidosPorInstancia.size() < 2) {
//            return
//        }
//        TokenPreDefinido anterior = null
//        Boolean algumaSequenciaValida = false
//        for (TokenPreDefinido token : pilhaTokensLidosPorInstancia) {
//            for (Closure<Boolean> ehSequenciaValida : sequenciasValidas) {
//                algumaSequenciaValida = algumaSequenciaValida || ehSequenciaValida(anterior, token)
//            }
//            if (algumaSequenciaValida) {
//                break
//            }
//            anterior = token
//        }
//        if (!algumaSequenciaValida) {
//            throw new Exception("ERROR SEQUENCIA ${pilhaDtoLida[0].simb} INVALIDA")
//        }
//    }

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        if(TokenPreDefinido.obtemToken(dtoToken.desc) == TokenPreDefinido.FECHA_CHAVE) {
            desfazProcessoAdicaoPilha()
            nodeToken.dtosDaMesmaRegra.removeLast()
            return true
        }
        return false
    }
}
