package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class Programa extends RegraEstrutura {
    Programa() {
        super([
                new DTOHashToken(TokenPreDefinido.CLASS, { -> new Classe() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null })
        ])
    }

    protected void verificaExcessao() {
        throw new Exception("ERRO TOKEN ${pilhaTokensLidos[0]} INVALIDO")
    }
}
