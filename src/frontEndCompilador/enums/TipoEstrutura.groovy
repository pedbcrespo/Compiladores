package frontEndCompilador.enums

enum TipoEstrutura {
    CLASSE('classe'),
    METODO('metodo'),
    ATRIBUTO('atributo'),
    VARIAVEL('variavel'),
    OPERACAO('operacao')

    String desc

    TipoEstrutura(String desc) {
        this.desc = desc
    }
}