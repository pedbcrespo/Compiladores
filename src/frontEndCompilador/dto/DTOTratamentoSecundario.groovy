package frontEndCompilador.dto

class DTOTratamentoSecundario extends Dto {
    List<String> listaToken
    int posInicioLista
    int posFinalLista
    Boolean flagAdicionarLista
    String desc

    String tokenMarcador

    Closure<Boolean> condicaoInicial
    Closure<Boolean> condicaoFinal

    DTOTratamentoSecundario(String desc,
                            Closure<Boolean> condicaoInicial,
                            Closure<Boolean> condicaoFinal,
                            String tokenMarcador) {
        this.tokenMarcador = tokenMarcador
        this.condicaoInicial = condicaoInicial
        this.condicaoFinal = condicaoFinal
        this.listaToken = []
        this.flagAdicionarLista = false
        this.desc = desc
    }

    Boolean verificaToken(String simb) {
        return simb == tokenMarcador || flagAdicionarLista
    }
}
