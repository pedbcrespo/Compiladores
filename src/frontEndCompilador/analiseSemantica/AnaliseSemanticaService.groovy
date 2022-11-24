package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Classe
import frontEndCompilador.dto.DTOTipoDto
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static List<BlocoToken> listaBlocos = []

    static List<BlocoToken> organizaBlocos(NodeToken arvore) {
        listaBlocos = geraBloco(arvore) + arvore.proximosNodes.collect{it -> geraBloco(it)}
        return listaBlocos
    }

    private static BlocoToken geraBloco(NodeToken node) {
        return new BlocoToken(node.dtosDaMesmaRegra)
    }

    static void analizaListaBlocos(List<BlocoToken> blocoTokens) {}
}
