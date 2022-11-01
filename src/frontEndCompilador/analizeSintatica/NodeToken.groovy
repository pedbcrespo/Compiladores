package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken

class NodeToken {
    DTOToken dtoToken
    NodeToken anterior
    List<NodeToken> proximosNodes

    NodeToken(DTOToken dto, NodeToken anterior = null) {
        this.dtoToken = dto
        this.anterior = anterior
        this.proximosNodes = []
    }

    void addNode(DTOToken dto) {
        if (dto == dtoToken) {
            return
        } else if (!dtoToken) {
            dtoToken = dto
        } else if (!proximosNodes) {
            proximosNodes.add(new NodeToken(dto))
        } else {
            proximosNodes.forEach(node -> node.addNode(dto))
        }
    }

    void addSubArvore(NodeToken subArvore) {
        proximosNodes.add(subArvore)
    }
}
