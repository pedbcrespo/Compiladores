package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class Atribuidor extends RegraEstrutura {
    Atribuidor() {
        super([
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.ABRE_PARENTESES, { -> new RFormal() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),
                new DTOHashToken(TokenPreDefinido.FECHA_PARENTESES, { -> new Atribuidor() }),
        ])
    }
}
