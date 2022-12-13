package backEndCompilador.geradorJson


import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.ConvTipo
import frontEndCompilador.enums.TipoEstrutura
import frontEndCompilador.enums.TipoOperacaoCorresp
import frontEndCompilador.enums.TokenPreDefinido

class GeradorDeCodigoService {
    private static NodeToken arvore
    private static Map<String, Object> mapDadosMapeados
    private static List<DTOToken> listaTokenOperacao = [
            new DTOToken(TokenPreDefinido.SOMA),
            new DTOToken(TokenPreDefinido.SUBTRACAO),
            new DTOToken(TokenPreDefinido.ASTERISTICO),
            new DTOToken(TokenPreDefinido.DIVISAO),
            new DTOToken(TokenPreDefinido.MAIOR_IGUAL),
            new DTOToken(TokenPreDefinido.MAIOR),
            new DTOToken(TokenPreDefinido.MENOR),
            new DTOToken(TokenPreDefinido.MENOR_IGUAL),
            new DTOToken(TokenPreDefinido.IGUAL)
    ]


    GeradorDeCodigoService(NodeToken arvore, Map<String, Object> mapDadosMapeados) {
        this.arvore = arvore
        this.mapDadosMapeados = mapDadosMapeados
    }

    static List<Map<String, Object>> buscaInstrucoes(DTOTipoToken dto) {
        if (!ehMetodo(dto)) {
            return [[:]] as List<Map<String, Object>>
        }
        List<Map<String, Object>> mapList = []
        NodeToken nodeFeature = dto.params["instrucoes"]
        List<List<DTOToken>> dtoListList = separaListasPorPontoVirgula(nodeFeature.dtosDaMesmaRegra)
        Map<String, Object> map

        for (List<DTOToken> listaTokens : dtoListList) {
            if (listaTokens.size() == 1 && ehToken(listaTokens[0], TokenPreDefinido.PONTO_VIRGULA)) {
                continue
            }
            String op = defineOperacao(listaTokens)
            String tipo = defineTipoOperacao(op, listaTokens)
            String dest = defineDestinoOperacao(listaTokens)
            if (ehCasoOperacao(listaTokens)) {
                List<String> args = defineArgsOperacao(listaTokens)
                map = ["op": op, "type": tipo, "dest": dest, "args": args]
            } else {
                String valor = defineValorAtribuicao(listaTokens)
                map = ["op": op, "type": tipo, "dest": dest, "value": valor]
            }
            mapList.add(map)
            mapList += trataChamadasDeMetodos(listaTokens, dto)
        }
        String ret = buscaValorRetorno(dto)
        mapList.add(["ret": ret])
        return mapList
    }

    static boolean ehMetodo(DTOTipoToken dtoTipoToken) {
        return dtoTipoToken.tipoEstrutura == TipoEstrutura.METODO
    }

    static DTOTipoToken retMetodo(String nomeMetodo) {
        List<DTOTipoToken> listaTipoToken = mapDadosMapeados['listaFeatures'] as List<DTOTipoToken>
        DTOTipoToken dto = listaTipoToken.find { it -> it.dtoToken.simb == nomeMetodo }
        return dto ? dto.tipoEstrutura == TipoEstrutura.METODO ? dto : null : null
    }

    static boolean ehToken(DTOToken dtoToken, TokenPreDefinido tokenPreDefinido) {
        return !dtoToken ? false : TokenPreDefinido.obtemToken(dtoToken.desc) == tokenPreDefinido
    }

    private static String defineOperacao(List<DTOToken> dtoTokens) {
        ConvTipo convTipo = ConvTipo.CONST
        DTOToken casoOperacaoAritmedica = dtoTokens.find{it -> it in listaTokenOperacao }
        if(casoOperacaoAritmedica) {
            for (DTOToken dto : dtoTokens) {
                convTipo = ConvTipo.obtem(dto)
                if (convTipo != ConvTipo.CONST) {
                    break
                }
            }
        }
        return convTipo.tipo.toLowerCase()
    }

    private static String defineTipoOperacao(String op, List<DTOToken> dtoTokens) {
        List<String> listaOp = ['add', 'sub', 'mul', 'div']
        DTOToken anterior = null
        List<DTOTipoToken> listaTiposDto = mapDadosMapeados["listaTiposDto"] as List<DTOTipoToken>
        if (op in listaOp) {
            return 'Int'
        } else {
            anterior = null
            for (DTOToken dto : dtoTokens) {
                if (ehToken(dto, TokenPreDefinido.ATRIBUICAO)) {
                    break
                }
                anterior = dto
            }
        }
        return listaTiposDto.find { it -> it.dtoToken == anterior }?.tipoOperacao?.id
    }

    private static String defineDestinoOperacao(List<DTOToken> dtoTokens) {
        DTOToken anterior = null
        for (DTOToken dto : dtoTokens) {
            if (ehToken(dto, TokenPreDefinido.ATRIBUICAO)) {
                break
            }
            anterior = dto
        }
        return anterior.simb
    }

    private static boolean ehCasoOperacao(List<DTOToken> dtoTokens) {
        List<DTOToken> listaTokenOperacao = TipoOperacaoCorresp.values().collect {
            it -> it.dtoTipoToken.dtoToken
        }
        for (DTOToken dto : dtoTokens) {
            if (listaTokenOperacao.contains(dto)) {
                return true
            }
        }
        return false
    }

    private static List<String> defineArgsOperacao(List<DTOToken> dtoTokens) {
        List<DTOToken> filtrado = dtoTokens[1..dtoTokens.size() - 1]
        List<DTOToken> args = [] as List<DTOToken>
        for (DTOToken dto : filtrado) {
            args += ehToken(dto, TokenPreDefinido.IDENTIFICADOR) ? dto : []
        }
        return args?.collect { it -> it.simb }
    }

    private static String defineValorAtribuicao(List<DTOToken> dtoTokens) {
        DTOToken anterior = null
        for (DTOToken dto : dtoTokens) {
            if (ehToken(anterior, TokenPreDefinido.ATRIBUICAO)) {
                return dto.simb
            }
            anterior = dto
        }
        return null
    }

    private static List<List<DTOToken>> separaListasPorPontoVirgula(List<DTOToken> dtoTokens) {
        List<List<DTOToken>> listaLista = []
        List<DTOToken> lista = []
        if (!dtoTokens.find { it -> ehToken(it, TokenPreDefinido.PONTO_VIRGULA) }) {
            return [dtoTokens]
        }
        for (DTOToken dto : dtoTokens) {
            if (ehToken(dto, TokenPreDefinido.PONTO_VIRGULA)) {
                listaLista.add(lista)
                lista = []
                continue
            }
            lista.add(dto)
        }
        return listaLista
    }

    static List<Map<String, String>> buscaParametros(DTOTipoToken dtoTipoToken) {
        if (dtoTipoToken.tipoEstrutura != TipoEstrutura.METODO) {
            return []
        }
        NodeToken nodeParametros = dtoTipoToken.params['parametros']
        List<DTOToken> listaDtoParametros = nodeParametros.dtosDaMesmaRegra.findAll { it ->
            ehToken(it, TokenPreDefinido.IDENTIFICADOR)
        }
        List<DTOTipoToken> listaVariaveis = mapDadosMapeados['listaVariavel'] as List<DTOTipoToken>
        return listaDtoParametros ? listaDtoParametros.collect { it ->
            String type = listaVariaveis.find { tipoToken -> tipoToken.dtoToken == it }?.tipoOperacao?.id
            ["name": it.simb, "type": type]
        } : []
    }

    static String buscaValorRetorno(DTOTipoToken dto) {
        NodeToken nodeFeature = dto.params["instrucoes"]
        List<List<DTOToken>> dtoListList = separaListasPorPontoVirgula(nodeFeature.dtosDaMesmaRegra)
        List<DTOToken> ultimaLinha = dtoListList.last()
        String dest = defineDestinoOperacao(ultimaLinha)
        return dest
    }

    private static List<Map<String, Object>> trataChamadasDeMetodos(List<DTOToken> dtoTokens, DTOTipoToken dtoTipoToken) {
        List<Map<String, Object>> listaMap = []
        Map<String, Object> map
        int pos = 0
        for(DTOToken dto : dtoTokens) {
            if(ehChamadaDeMetodo(dto)) {
                String nomeMetodo = dto.simb.contains('.')? dto.simb
                        .substring(dto.simb.indexOf('.'))
                        .replaceFirst('.', '') : dto.simb
                DTOTipoToken metodo = retMetodo(nomeMetodo)
                String op = ConvTipo.CALL.tipo
                String type = metodo.tipoOperacao.id
                String name = nomeMetodo
                NodeToken node = dtoTipoToken.params['instrucoes']
                List<String> args = argsMetodo(metodo, node.proximosNodes[0] )
                map = ["op": op, "type":type, "name": name, "args": args]
                listaMap.add(map)
            }
            pos += 1
        }
        return listaMap
    }

    private static boolean ehChamadaDeMetodo(DTOToken dtoToken) {
        String nomeMetodo = dtoToken.simb.contains('.')? dtoToken.simb
                .substring(dtoToken.simb.indexOf('.'))
                .replaceFirst('.', '') : dtoToken.simb
        DTOTipoToken metodo = retMetodo(nomeMetodo)
        return metodo
    }

    private static List<String> argsMetodo(DTOTipoToken dtoTipoTokens, NodeToken nodeParametros) {
        List<DTOToken> tokensParametro = nodeParametros.dtosDaMesmaRegra
        return tokensParametro*.simb
    }
}
