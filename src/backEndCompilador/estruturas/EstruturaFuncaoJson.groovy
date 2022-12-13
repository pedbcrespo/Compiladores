package backEndCompilador.estruturas

import frontEndCompilador.enums.TipoEstrutura

class EstruturaFuncaoJson extends EstruturaJson {
    TipoEstrutura tipoEstrutura
    List<Map<String, Object>> instrs
    List<Map<String, Object>> args

    EstruturaFuncaoJson(String name, String type, List<Map<String, Object>> instrs, List<Map<String, Object>> args, TipoEstrutura tipoEstrutura) {
        super(name, type, ["instrs": instrs])
        this.instrs = instrs
        this.params = ["instrs": instrs]
        this.args = args
        this.tipoEstrutura = tipoEstrutura
    }

    @Override
    Map geraInfoParaJson() {
        Map<String, Object> mapResultado = [
                "name": name,
                "type": type,
        ]
        if (tipoEstrutura == TipoEstrutura.METODO) {
            mapResultado.putAll(["args"  : args,"instrs": instrs])
        }
        return mapResultado
    }

}
