package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
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

    Programa(TokenPreDefinido tokenPreDefinido, List<DTOHashToken> dtoHashTokens) {
        super(tokenPreDefinido, dtoHashTokens)
    }

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        throw new Exception("ERRO TOKEN: ${pilhaDtoLida[0].simb}")
    }

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        return TokenPreDefinido.obtemToken(dtoToken.desc) == TokenPreDefinido.PONTO_VIRGULA
    }
}
