package frontEndCompilador.dto

abstract class DTOSecundario {
    List<String> listaToken
    Integer posInicioLista = null
    Integer posFinalLista = null
    Boolean flagAdicionarLista = false
    String desc

    String tokenMarcador

    Closure<Boolean> condicaoInicial
    Closure<Boolean> condicaoFinal

    DTOSecundario(List<String> listaToken = []) {
        this.listaToken = listaToken
    }

    Boolean verificaToken(String simb) {
        return simb == tokenMarcador || flagAdicionarLista
    }
}
