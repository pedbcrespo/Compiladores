package backEndCompilador.estruturas

class EstruturaFuncaoJson extends EstruturaJson {

    List<TupInstrs> instrs

    EstruturaFuncaoJson(String name, String type) {
        super(name, type, null)
        this.instrs = instrs
    }

    @Override
    Map geraInfoParaJson() {
        return [
                "name": name,
                "type": type,
                "instrs": instrs.collect{it -> it.retOpr()}
        ]
    }

}
