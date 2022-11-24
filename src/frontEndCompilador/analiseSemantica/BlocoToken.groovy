package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken

class BlocoToken {
    List<DTOToken> dtosContidos

    BlocoToken(List<DTOToken> dtosContidos) {
        this.dtosContidos = dtosContidos
    }
}
