package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken

class BlocoToken {
    DTOToken dtoCabeca
    DTOToken dtoHeranca
    List<DTOToken> dtosContidos
    private String mensagemErro
    private valido = true

    BlocoToken(List<DTOToken> dtosContidos) {
        this.dtosContidos = dtosContidos
    }

    BlocoToken(DTOToken dtoCabeca, DTOToken dtoHeranca, List<DTOToken> dtosContidos) {
        this.dtoCabeca = dtoCabeca
        this.dtoHeranca = dtoHeranca
        this.dtosContidos = dtosContidos
    }

    void setValido(valido) {
        this.valido = valido
    }

    void validade() {
        if(!valido){
            throw new Exception(mensagemErro)
        }
    }
}
