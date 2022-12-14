package backEndCompilador.geradorJava

class GeradorCodigoJavaService {
    private Map mapJson
    private List<Map<String, String>> variaveisCriadas = []

    GeradorCodigoJavaService(Map mapJson) {
        this.mapJson = mapJson
    }

    static String trataArgumentos(Map<String, Object> metodo) {
        List<Map<String, Object>> argumentos = metodo['args'] as List<Map<String, Object>>
        List<String> lstTxt = []
        for (Map<String, Object> argumento : argumentos) {
            String name = argumento['name']
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
        return lstTxt.join(';\n')
    }

    private String trataCasoOperacao(Map<String, Object> instrucao, String op) {
        OperacaoEqv operacaoEqv = OperacaoEqv.obtem(op)
        String txt = ''
        if (operacaoEqv == OperacaoEqv.CALL) {

        }

    }

    private String geraVariavel(String tipo, String nomeOpr) {
        Random random = new Random()
        int num = variaveisCriadas.size() > 0 ? variaveisCriadas.size() : 1
        String variavel = "${tipo} vari${random.nextInt(10**num)}"
        variaveisCriadas.add(["nome": nomeOpr, "tipo":tipo, "variavel":variavel])
        return variavel
    }
}
