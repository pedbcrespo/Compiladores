package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken

class NodeToken {
    DTOToken dtoToken
    NodeToken anterior
    List<NodeToken> proximosNodes

    NodeToken(NodeToken anterior) {
        this.anterior = anterior
        this.dtoToken = null
        this.proximosNodes = []

    }

    NodeToken armazenaDTO(DTOToken dto) {
        dtoToken = dto
        return addRamificacao(new NodeToken(this))
    }

    private NodeToken addRamificacao(NodeToken proximoNode) {
        proximosNodes.add(0, proximoNode)
        return proximoNode
    }

    private String formataStringImpressao(int qtdTabs) {
        String tabs = qtdTabs > 0 ? '\t'.repeat(qtdTabs) : ''
        String dtoEmTexto = dtoToken.simb!=null ?: 'null'
        return tabs + dtoEmTexto
    }

    void imprime(int qtdTabs = 0) {
        println(this.formataStringImpressao(qtdTabs))
        for (NodeToken nodeToken : proximosNodes) {
            nodeToken.imprime(proximosNodes.indexOf(nodeToken) + 1)
        }
    }
}
