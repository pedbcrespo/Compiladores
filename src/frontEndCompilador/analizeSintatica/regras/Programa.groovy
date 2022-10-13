package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class Programa extends RegraEstrutura {
    Programa() {
        super([
                new DTOHashToken(TokenPreDefinido.CLASS, { -> new Classe() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null })
        ])
    }

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        throw new Exception("ERRO TOKEN: ${pilhaDtoLida[0].simb}")
    }
}
