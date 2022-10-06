package frontEndCompilador.analizeSintatica

import frontEndCompilador.analizeSintatica.regras.Classe
import frontEndCompilador.analizeSintatica.regras.Programa
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSintaticaService {


    static void analisa(List<DTOToken> dtoTokens) {
        RegraEstrutura.setListaDtoTokenFornecida(dtoTokens)
        Programa programa = new Programa()
        programa.analisaChaveFornecida(null)
    }

}
