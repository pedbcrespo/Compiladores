package backEndCompilador.estruturas

class EstruturaFuncaoJson extends EstruturaJson {

    List<Map<String, Object>> instrs

    EstruturaFuncaoJson(String name, String type) {
        super(name, type, null)
        this.instrs = instrs
    }

    EstruturaFuncaoJson(String name, String type, List<Map<String, Object>> instrs) {
        super(name, type, ["instrs":instrs])
        this.instrs = instrs
        this.params = ["instrs":instrs]
    }

    @Override
    Map geraInfoParaJson() {
        return [
                "name": name,
                "type": type,
                "instrs": instrs
        ]
    }

}
