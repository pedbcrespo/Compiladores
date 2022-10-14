package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RType extends RegraEstrutura {


    RType() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
        ])
    }

    protected void validacaoSequenciaTokens() {
        if (pilhaTokensLidosPorInstancia[0] != TokenPreDefinido.IDENTIFICADOR) {
            throw new Exception("ERROR ${pilhaTokensLidosPorInstancia[0]} SEQUENCIA INVALIDA")
        }

    }
}
