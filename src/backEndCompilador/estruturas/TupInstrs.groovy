package backEndCompilador.estruturas

class TupInstrs extends EstruturaJson{
    String type
    String val
    String dest
    String opr

    TupInstrs(String name, String type) {
        super(name, type, null)
    }

    Map retOpr() {
        return [
                "op"  : opr,
                "type": type,
                "dest": dest,
                "value": val
        ]
    }
}
