package backEndCompilador

import backEndCompilador.estruturas.EstruturaFuncaoJson
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoEstrutura
import frontEndCompilador.enums.TokenPreDefinido

class GeradorDeCodigoService {
    private NodeToken arvore
    private Map<String, Object> mapDadosMapeados

    GeradorDeCodigoService(NodeToken arvore, Map<String, Object> mapDadosMapeados) {
        this.arvore = arvore
        this.mapDadosMapeados = mapDadosMapeados
    }

    List<Map<String, Object>> buscaInstrucoes(DTOTipoToken dto) {
        if (!ehMetodo(dto)) {
            return [[:]] as List<Map<String, Object>>
        }
        List<Map<String, Object>> mapList = []
        NodeToken nodeFeature = dto.params["instrucoes"]
        List<List<DTOToken>> dtoListList = nodeFeature.dtosDaMesmaRegra.split { it -> ehToken(it, TokenPreDefinido.PONTO_VIRGULA) }
        Map<String, Object> map = [:]
        for (List<DTOToken> listaTokens : dtoListList) {
            String op = defineOperacao(listaTokens)
            String tipo = defineTipoOperacao(listaTokens)
            String dest = defineDestinoOperacao(listaTokens)
            if(ehCasoOperacao(listaTokens)){
                String args = defineArgsOperacao(listaTokens)
                map = ["op":op, "type":tipo, "dest":dest, "args": args]
            } else {
                String valor = defineValorAtribuicao(listaTokens)
                map = ["op":op, "type":tipo, "dest":dest, "value": valor]
            }
            mapList.add(map)
        }
        return mapList
    }

    private static boolean ehMetodo(DTOTipoToken dtoTipoToken) {
        return dtoTipoToken.tipoEstrutura == TipoEstrutura.METODO
    }

    static boolean ehToken(DTOToken dtoToken, TokenPreDefinido tokenPreDefinido) {
        return dtoToken == new DTOToken(tokenPreDefinido)
    }
}
