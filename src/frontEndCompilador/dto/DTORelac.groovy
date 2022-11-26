package frontEndCompilador.dto

import frontEndCompilador.analizeSintatica.NodeToken

class DTORelac {
    DTOToken cabeca
    NodeToken nodeToken

    DTORelac(DTOToken cabeca, NodeToken nodeToken) {
        this.cabeca = cabeca
        this.nodeToken = nodeToken
    }
}
