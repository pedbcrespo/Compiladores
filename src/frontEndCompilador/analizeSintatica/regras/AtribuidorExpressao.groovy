package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class AtribuidorExpressao extends RegraEstrutura{
    AtribuidorExpressao() {
        super([
                new DTOHashToken(TokenPreDefinido.ATRIBUICAO, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
                new DTOHashToken(TokenPreDefinido.ABRE_CHAVE, { -> new Expressao() }),
                new DTOHashToken(TokenPreDefinido.FECHA_CHAVE, { -> null }),
        ])
    }
}
