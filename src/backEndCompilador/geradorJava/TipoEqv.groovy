package backEndCompilador.geradorJava

enum TipoEqv {
    INT('Int', {String val -> Integer.parseInt(val)}),
    BOOL('Bool', {String val -> Boolean.parseBoolean(val)})

    String desc
    Closure<Object> funcConvert

    TipoEqv(String desc, Closure<Object> funcConvert) {
        this.desc = desc
        this.funcConvert = funcConvert
    }
}