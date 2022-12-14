package backEndCompilador.geradorJava

enum OperacaoEqv {
    ADD('add', {List<String> args -> args.join(' + ')}),
    SUB('sub', {List<String> args -> args.join(' - ')}),
    DIV('div', {List<String> args -> args.join(' / ')}),
    MUL('mul', {List<String> args -> args.join(' * ')}),
    CALL('call', {Object x -> x}),
    CONST('const', {Object x -> x})
    String desc
    Closure<Object> funcConvert

    OperacaoEqv(String desc, Closure<Object> funcConvert) {
        this.desc = desc
        this.funcConvert = funcConvert
    }

    static OperacaoEqv obtem(String val) {
        OperacaoEqv op = values().find {it -> it.desc == val}
        return op
    }
}