package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.enums.TokenPreDefinido

class Classe extends RegraEstrutura {
    Classe() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> new ClasseHeranca() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null })
        ])
    }

    @Override
    protected void verificaExcessao() {
        throw new Exception("ERRO TOKEN ${listaDtoFornecida[0]} INVALIDO\n")
    }
}
