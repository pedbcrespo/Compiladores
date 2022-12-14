package backEndCompilador.geradorJava

class GeradorCodigoJavaService {
    private Map mapJson
    private static List<Map<String, String>> variaveisCriadas = []

    GeradorCodigoJavaService(Map mapJson) {
        this.mapJson = mapJson
    }

    static String trataArgumentos(Map<String, Object> metodo) {
        List<Map<String, Object>> argumentos = metodo['args'] as List<Map<String, Object>>
        List<String> lstTxt = []


        for (Map<String, Object> argumento : argumentos) {
            String name = argumento['name']
            Map<String, String> argumentoRegistrado = variaveisCriadas.find { it -> it['nome'] as String == name }
            if (!argumentoRegistrado) {
                variaveisCriadas.add "nome": argumento['name'] as String,
                        "tipo": Equivalente.obtem(argumento['type'] as String) ,
                        "variavel": argumento['name'] as String
            }
            String type = Equivalente.obtem(argumento['type'] as String)
            lstTxt.add("${type} ${name}")
        }
        return lstTxt.join(', ')
    }

    static String trataInstrucoes(Map<String, Object> metodo) {
        List<Map<String, Object>> instrucoes = metodo['instrs'] as List<Map<String, Object>>
        List<String> lstTxt = []
        for (Map<String, Object> instrucao : instrucoes) {
            String op = instrucao['op']
            String txt = ''
            if (op) {
                lstTxt.add(trataCasoOperacao(instrucao, op))
            } else {
                String ret = instrucao['ret']
                txt = "return ${ret};"
            }
            lstTxt.add(txt)
        }
        return lstTxt.join('\n')
    }

    private static String trataCasoOperacao(Map<String, Object> instrucao, String op) {
        OperacaoEqv operacaoEqv = OperacaoEqv.obtem(op)
        String txt = ''
        if (operacaoEqv == OperacaoEqv.CALL) {
            txt = "${geraVariavel(Equivalente.obtem(instrucao['type'] as String), instrucao['name'] as String)}" +
                    " = ${instrucao['name']}(${(instrucao['args'] as List<String>).join(', ')});"
        } else if (operacaoEqv in [OperacaoEqv.ADD, OperacaoEqv.SUB, OperacaoEqv.MUL, OperacaoEqv.DIV]) {
            String variavel = geraVariavel(Equivalente.obtem(instrucao['type'] as String), instrucao['dest'] as String)
            txt = "${variavel} = ${operacaoEqv.funcConvert(instrucao['args'])};"
        } else if (operacaoEqv == OperacaoEqv.CONST) {
            String value = trataValor(instrucao['value'] as String, instrucao)
            txt = "${Equivalente.obtem(instrucao['type'] as String)} ${instrucao['dest']} = ${value};"
        }
        return txt
    }

    private static String geraVariavel(String tipo, String nomeOpr) {
        Random random = new Random()
        int num = variaveisCriadas.size() > 0 ? variaveisCriadas.size() : 1
        String variavel = "vari${random.nextInt(10**num)}"
        variaveisCriadas.add(["nome": nomeOpr, "tipo": tipo, "variavel": variavel])
        return "${tipo} ${variavel}"
    }

    private static String trataValor(String value, Map<String, Object> instrs) {
        String valor = value
        Map<String, String> mapVariavel = variaveisCriadas.find { it ->
            it["nome"] == value
        }
        return mapVariavel ? mapVariavel["variavel"] : valor
    }
}
