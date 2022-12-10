package backEndCompilador.estruturas

class EstruturaClasseJson extends EstruturaJson {
    List<EstruturaFuncaoJson> methods

    EstruturaClasseJson(String name, String type) {
        super(name, type,null)
        this.methods = []
    }

    EstruturaClasseJson(String name, String type, List<EstruturaFuncaoJson> methods) {
        super(name, type, ["methods":methods])
        this.methods = methods
        this.params = ["methods":methods]
    }

    @Override
    Map geraInfoParaJson() {

        return [
                "name"   : name,
                "type"   : type,
                "functions": eachMethodInf()
        ]
    }

    List<Map> eachMethodInf() {
        return methods.collect { it -> it.geraInfoParaJson() }
    }
}
