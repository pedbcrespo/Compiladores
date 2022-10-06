package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RCondicionalIF extends RegraEstrutura {
    RCondicionalIF() {
        super([
                new DTOHashToken(TokenPreDefinido.ELSE, { -> new Expressao() })
        ])
    }
}
