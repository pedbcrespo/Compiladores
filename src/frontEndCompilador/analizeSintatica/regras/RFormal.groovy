package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RFormal extends RegraEstrutura{
    RFormal() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, {-> new Atribuidor()})
        ])
    }
}
