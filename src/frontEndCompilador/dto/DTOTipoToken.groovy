package frontEndCompilador.dto

import frontEndCompilador.analiseSemantica.TipoBlocoEst
import frontEndCompilador.enums.TipoEstrutura

class DTOTipoToken {
    DTOToken dtoClasseHeranca
    DTOToken dtoToken
    TipoEstrutura tipoEstrutura
    TipoBlocoEst tipoOperacao
    Map params

    DTOTipoToken(DTOToken dtoToken, TipoBlocoEst tipoBloco, TipoEstrutura tipoEstrutura) {
        this.dtoToken = dtoToken
        this.tipoOperacao = tipoBloco
        this.tipoEstrutura = tipoEstrutura
    }

    DTOTipoToken(DTOToken dtoClasseHeranca, DTOToken dtoToken, TipoBlocoEst tipoOperacao, TipoEstrutura tipoEstrutura, Map params =[:]) {
        this.dtoClasseHeranca = dtoClasseHeranca
        this.dtoToken = dtoToken
        this.tipoOperacao = tipoOperacao
        this.tipoEstrutura = tipoEstrutura
        this.params = params
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DTOTipoToken that = (DTOTipoToken) o

        if (dtoClasseHeranca != that.dtoClasseHeranca) return false
        if (dtoToken != that.dtoToken) return false
        if (tipoEstrutura != that.tipoEstrutura) return false
        if (tipoOperacao != that.tipoOperacao) return false

        return true
    }

    int hashCode() {
        int result
        result = (dtoClasseHeranca != null ? dtoClasseHeranca.hashCode() : 0)
        result = 31 * result + (dtoToken != null ? dtoToken.hashCode() : 0)
        result = 31 * result + (tipoEstrutura != null ? tipoEstrutura.hashCode() : 0)
        result = 31 * result + (tipoOperacao != null ? tipoOperacao.hashCode() : 0)
        return result
    }
}
