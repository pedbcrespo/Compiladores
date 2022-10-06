package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class ClasseHeranca extends RegraEstrutura {

    ClasseHeranca() {
        super([
                new DTOHashToken(TokenPreDefinido.INHERITS, { -> new RTypeClass() }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new RFeature() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null}),
        ])
    }
}