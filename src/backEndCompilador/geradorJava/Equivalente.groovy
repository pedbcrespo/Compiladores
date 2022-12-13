package backEndCompilador.geradorJava

enum Equivalente {
    INT('Int', 'Integer'),
    BOOL('Bool', 'Boolean'),
    STRING('String', 'String'),
    CLASS('class', 'class'),
    INHERITS('inherits', 'extends')

    String valor
    String traducao

    Equivalente(String valor, String traducao) {
        this.valor = valor
        this.traducao = traducao
    }
}