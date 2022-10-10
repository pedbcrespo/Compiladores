package frontEndCompilador.services

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Programa
import frontEndCompilador.dto.DTOToken

class AnaliseSintaticaService {

    static void analisa(List<DTOToken> dtoTokens) {
        RegraEstrutura.setListaDtoTokenFornecida(dtoTokens)
        Programa programa = new Programa()
        programa.analisaChaveFornecida(null)
    }



}
