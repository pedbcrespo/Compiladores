package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RType extends RegraEstrutura{
    RType() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new AtribuidorExpressao() }),
        ])
    }
}
