package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken

class AnaliseSemantica {
    static void analisaArvore(NodeToken arvore) {
        AnaliseSemanticaService.analizaTiposNaArvore(arvore)
    }
}
