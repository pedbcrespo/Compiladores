package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.regras.RType
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static List<DTOTipoToken> listaTiposDto = []

    static void analizaTiposNaArvore(NodeToken nodeToken) {
        preencheListaTiposDto(nodeToken)
    }

    private static TipoBloco defineTipoSequencia(NodeToken node) {

    }

    private static void preencheListaTiposDto(NodeToken nodeToken) {
        defineTipoDto(nodeToken)
        for (NodeToken node : nodeToken.proximosNodes) {
            if (!(node.regraNode.getClass() == RType.getClass())) {
                preencheListaTiposDto(node)
            }
        }
    }

    private static boolean verificaTiposInstancia(NodeToken node) {
        boolean validacao = true
        node.dtosDaMesmaRegra.findResults { it ->
            validacao = validacao && it.simb[0] == it.simb[0].toUpperCase()
        }
        return validacao
    }

    private static void defineTipoDto(NodeToken node) {
        TokenPreDefinido tokenDoisPontos = TokenPreDefinido.DOIS_PONTOS
        if (!node.dtosDaMesmaRegra.contains(new DTOToken(tokenDoisPontos))) {
            return
        }
        DTOToken anterior = null
        Closure<Boolean> buscaNodeDefTipo = { NodeToken nodeClosure -> nodeClosure.regraNode.getClass() == RType }
        for (DTOToken dto : node.dtosDaMesmaRegra) {
            if (TokenPreDefinido.obtemToken(dto.desc) == tokenDoisPontos) {
                NodeToken nodeTipo = encontraNode(node, buscaNodeDefTipo)
                TipoBloco tipoDefinido = TipoBloco.obtemTipo(nodeTipo.dtosDaMesmaRegra[0])
                adicionaListaTipos(new DTOTipoToken(dto, tipoDefinido))
            }
            anterior = dto
        }
    }

    private static NodeToken encontraNode(NodeToken nodeBase, Closure<Boolean> verificacao) {
        for (NodeToken node : nodeBase.proximosNodes) {
            if (verificacao(node)) {
                return node
            }
        }
        return null
    }

    private static boolean existeToken(DTOToken dtoToken) {
        return listaTiposDto.find { it -> it.dtoToken == dtoToken }
    }

    private static void adicionaListaTipos(DTOTipoToken dto) {
        if(existeToken(dto.dtoToken)) {
            return
        }
        listaTiposDto.add(dto)
    }
}
