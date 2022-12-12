package backEndCompilador.estruturas

class EstruturaFuncaoJson extends EstruturaJson {

    List<Map<String, Object>> instrs
    List<Map<String, Object>> args

    EstruturaFuncaoJson(String name, String type) {
        super(name, type, null)
        this.instrs = instrs
    }

    EstruturaFuncaoJson(String name, String type, List<Map<String, Object>> instrs, List<Map<String, Object>> args) {
        super(name, type, ["instrs":instrs])
        this.instrs = instrs
        this.params = ["instrs":instrs]
        this.args = args
    }

    @Override
    Map geraInfoParaJson() {
        return [
                "name": name,
                "type": type,
                "args": args,
                "instrs": instrs
        ]
    }

}
