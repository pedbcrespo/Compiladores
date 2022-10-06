package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class RCondicionalCase extends RegraEstrutura {
    RCondicionalCase() {
        super([
                new DTOHashToken(TokenPreDefinido.OF, { -> new RTypeClass() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> new RTypeClass() }),
                new DTOHashToken(TokenPreDefinido.ESAC, { -> null })
        ])
    }
}
