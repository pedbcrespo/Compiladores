package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RType extends RegraEstrutura {


    RType() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.SELF, { -> null }),
                new DTOHashToken(TokenPreDefinido.SELF_TYPE, { -> null }),
        ])
    }

    protected void validacaoSequenciaTokens() {
        List<TokenPreDefinido> listaTokensChaves = [
                TokenPreDefinido.IDENTIFICADOR,
                TokenPreDefinido.SELF,
                TokenPreDefinido.SELF_TYPE
        ]
        if (!listaTokensChaves.contains(pilhaTokensLidosPorInstancia[0])) {
            throw new Exception("ERROR ${pilhaTokensLidosPorInstancia[0]} SEQUENCIA INVALIDA")
        }

    }
}
