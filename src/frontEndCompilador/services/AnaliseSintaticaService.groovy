package frontEndCompilador.services

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Programa
import frontEndCompilador.dto.DTOToken

class AnaliseSintaticaService {

    static NodeToken analisa(List<DTOToken> dtoTokens) {
        RegraEstrutura.setListaDtoTokenFornecida(dtoTokens)
        Programa programa = new Programa()
        programa.analisa()
        return null
    }



}
