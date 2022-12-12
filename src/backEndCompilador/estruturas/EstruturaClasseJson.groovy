package backEndCompilador.estruturas

import frontEndCompilador.enums.TipoEstrutura

class EstruturaClasseJson extends EstruturaJson {
    List<EstruturaFuncaoJson> features

    EstruturaClasseJson(String name, String type, List<EstruturaFuncaoJson> features) {
        super(name, type, ["features": features])
        this.features = features
        this.params = ["features": features]
    }

    @Override
    Map geraInfoParaJson() {
        List<EstruturaFuncaoJson> listaAtributos = features.findAll { it -> it.tipoEstrutura == TipoEstrutura.ATRIBUTO }
        List<EstruturaFuncaoJson> listaMetodos = features.findAll { it -> it.tipoEstrutura == TipoEstrutura.METODO }
        return [
                "name"    : name,
                "type"    : type,
                "attributes": eachMethodInf(listaAtributos),
                "methods": eachMethodInf(listaMetodos)
        ]
    }

    static List<Map> eachMethodInf(List<EstruturaFuncaoJson> lista) {
        return lista.collect { it -> it.geraInfoParaJson() }
    }
}
