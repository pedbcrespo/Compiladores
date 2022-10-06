package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RTypeClass extends RegraEstrutura {
    RTypeClass() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new ClasseHeranca() }),
        ])
    }
}
