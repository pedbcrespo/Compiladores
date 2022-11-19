package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken

class NodeToken {
    RegraEstrutura regraNode
    List<DTOToken> dtosDaMesmaRegra
    List<NodeToken> proximosNodes

    NodeToken(RegraEstrutura regra) {
        this.regraNode = regra
        this.dtosDaMesmaRegra = [regra.dtoTokenFornecida]
        this.proximosNodes = []
    }

    void addNode(RegraEstrutura regra) {
        if(regra == regraNode) {
            DTOToken dto = regra.dtoTokenFornecida
            dtosDaMesmaRegra.add(dto)
        }
        else {
            NodeToken nodeComRegra = proximosNodes.find{it -> it.regraNode == regra}
            if(!nodeComRegra) {
                proximosNodes.add(new NodeToken(regra))
            }
        }
    }

    void addNode(NodeToken node) {
        proximosNodes.add(node)
    }
}
