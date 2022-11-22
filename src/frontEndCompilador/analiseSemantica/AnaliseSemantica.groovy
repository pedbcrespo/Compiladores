package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken

class AnaliseSemantica {
    static void analisaArvore(NodeToken arvore) {
        List<BlocoToken> listaBlocos = AnaliseSemanticaService.organizaBlocos(arvore)
        AnaliseSemanticaService.analizaListaBlocos(listaBlocos)
    }
}
