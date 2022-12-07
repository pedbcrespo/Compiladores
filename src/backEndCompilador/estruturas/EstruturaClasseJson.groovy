package backEndCompilador.estruturas

class EstruturaClasseJson extends EstruturaJson {
    List<EstruturaFuncaoJson> methods

    EstruturaClasseJson(String name, String type) {
        super(name, type,null)
        this.methods = []
    }

    @Override
    Map geraInfoParaJson() {

        return [
                "name"   : name,
                "type"   : type,
                "methods": eachMethodInf()
        ]
    }

    List<Map> eachMethodInf() {
        return methods.collect { it -> it.geraInfoParaJson() }
    }
}
