package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken

class NodeToken {
    DTOToken dtoToken
    List<NodeToken> proximosNodes

    NodeToken(DTOToken dtoToken) {
        this.dtoToken = dtoToken
        this.proximosNodes = []
    }

    void addRamificacao(NodeToken proximoNode) {
        proximosNodes.add(proximoNode)
    }
}
