package backEndCompilador.estruturas

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura

class EstruturaProgramaJson extends EstruturaJson {
    List<EstruturaClasseJson> classes

    EstruturaProgramaJson() {
        super(null, null, null)
        this.classes = []
    }

    @Override
    Map geraInfoParaJson() {
        return [
                "class": classes.collect{it ->
                    it.geraInfoParaJson()
                }
        ]
    }

    @Override
    void leInfoNodeToken(NodeToken node) {
        RegraEstrutura regra = node.regraNode

    }

}
