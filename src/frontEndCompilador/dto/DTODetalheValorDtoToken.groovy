package frontEndCompilador.dto

import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TokenPreDefinido

class DTODetalheValorDtoToken {
    DTOToken dtoToken
    TipoBloco tipoOperacao

    DTODetalheValorDtoToken(DTOToken dtoToken) {
        this.dtoToken = dtoToken
        this.tipoOperacao = TipoBloco.determinaTipo(dtoToken.desc)
    }
}
