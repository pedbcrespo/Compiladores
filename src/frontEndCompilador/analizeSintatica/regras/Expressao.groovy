package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class Expressao extends RegraEstrutura {
    Expressao() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new AtribuidorExpressao() }),
                new DTOHashToken(TokenPreDefinido.IF, { -> new RCondicionalIF() }),
                new DTOHashToken(TokenPreDefinido.CASE, { -> new RCondicionalCase() }),
                new DTOHashToken(TokenPreDefinido.POOL, { -> null }),
                new DTOHashToken(TokenPreDefinido.SOMA, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.SUBTRACAO, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.ASTERISTICO, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.MAIOR, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.MAIOR_IGUAL, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.MENOR, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.MENOR_IGUAL, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.IGUAL, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.ABRE_PARENTESES, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_PARENTESES, { -> null }),
                new DTOHashToken(TokenPreDefinido.LET, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.VIRGULA, { -> new Expressao() }),
        ])
    }
}
