package frontEndCompilador.dto

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.enums.TokenPreDefinido

class DTOHashToken {
    TokenPreDefinido tokenChave
    Closure<RegraEstrutura> proximaEtapa

    DTOHashToken(TokenPreDefinido tokenChave, Closure<RegraEstrutura> proximaEtapa) {
        this.tokenChave = tokenChave
        this.proximaEtapa = proximaEtapa
    }
}
