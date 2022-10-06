package frontEndCompilador.dto

import frontEndCompilador.enums.TokenPreDefinido

class DTOToken {
    String simb
    String desc

    DTOToken(String simb, String desc) {
        this.simb = simb
        this.desc = desc
    }

    DTOToken(TokenPreDefinido token) {
        this.simb = token.simb
        this.desc = token.name()
    }

    @Override
    String toString() {
        return "DTOToken{ simb= '${simb}', desc= '${desc}'}"
    }
}
