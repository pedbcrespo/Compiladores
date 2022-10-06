package frontEndCompilador.analizeLexica

class DTOTratamentoSecundario {
    List<String> listaToken
    int posInicioLista
    int posFinalLista
    Boolean flagAdicionarLista
    String desc

    DTOTratamentoSecundario(String desc) {
        this.listaToken = []
        this.flagAdicionarLista = false
        this.desc = desc
    }
}
