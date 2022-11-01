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

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DTOToken)) return false

        DTOToken dtoToken = (DTOToken) o

        if (desc != dtoToken.desc) return false
        if (simb != dtoToken.simb) return false

        return true
    }

    int hashCode() {
        int result
        result = simb.hashCode()
        result = 31 * result + desc.hashCode()
        return result
    }
}
