package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RFeature extends RegraEstrutura{
    RFeature() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new Atribuidor() })
        ])
    }
}
