package frontEndCompilador.enums

enum TipoBloco {
    METODO('metodo'),
    FUNCAO('funcao')

    String desc

    TipoBloco(String desc) {
        this.desc = desc
    }
}