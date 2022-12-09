package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken

class AnaliseSemantica {
    static Map<String, Object> analisaArvore(NodeToken arvore) {
        return AnaliseSemanticaService.analizaTiposNaArvore(arvore)
    }
}
