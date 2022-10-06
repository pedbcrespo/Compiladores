package frontEndCompilador.analizeLexica

class DTOToken {
    int id
    String simb
    String desc

    DTOToken(int id, String simb, String desc) {
        this.id = id
        this.simb = simb
        this.desc = desc
    }

    DTOToken(Token token) {
        this.id = token.id
        this.simb = token.simb
        this.desc = token.name()
    }

    @Override
    String toString() {
        return "DTOToken{ simb= '${simb}', desc= '${desc}', id= ${id}}"
    }
}
