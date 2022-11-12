package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken

class BlocoToken {
    DTOToken dtoCabeca
    DTOToken dtoHeranca
    List<DTOToken> dtosContidos
    protected String mensagemErro

    BlocoToken(List<DTOToken> dtosContidos) {
        this.dtosContidos = dtosContidos
    }

    BlocoToken(DTOToken dtoCabeca, DTOToken dtoHeranca, List<DTOToken> dtosContidos) {
        this.dtoCabeca = dtoCabeca
        this.dtoHeranca = dtoHeranca
        this.dtosContidos = dtosContidos
    }

    boolean analiza() {
        if (identificaSemantica()) {
            return true
        } else {
            throw new Exception(mensagemErro)
        }
    }

    protected boolean identificaSemantica() {

    }


}
