package backEndCompilador.estruturas

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Programa
import groovy.json.JsonOutput

abstract class EstruturaJson {
    String name
    String type
    Map params

    EstruturaJson(String name, String type, Map params) {
        this.name = name
        this.type = type
        this.params = params
    }

    void geraArquivoJson(EstruturaJson dados) {
        JsonOutput.toJson(geraInfoParaJson())
    }

    Map geraInfoParaJson() { return [:] }


    void leInfoNodeToken(NodeToken node) {
    }
}
