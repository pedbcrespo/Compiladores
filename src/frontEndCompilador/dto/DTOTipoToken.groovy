package frontEndCompilador.dto

import frontEndCompilador.enums.TipoBloco

class DTOTipoToken {
    DTOToken dtoToken
    TipoBloco tipoOperacao

    DTOTipoToken(DTOToken dtoToken, TipoBloco tipoBloco) {
        this.dtoToken = dtoToken
        this.tipoOperacao = tipoBloco
    }



    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof DTOTipoToken)) return false

        DTOTipoToken that = (DTOTipoToken) o

        if (dtoToken != that.dtoToken) return false
        if (tipoOperacao != that.tipoOperacao) return false

        return true
    }

    int hashCode() {
        int result
        result = dtoToken.hashCode()
        result = 31 * result + tipoOperacao.hashCode()
        return result
    }

    @Override
    public String toString() {
        return "${dtoToken.simb} : ${tipoOperacao.name()}"
    }
}
