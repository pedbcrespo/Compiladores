package backEndCompilador


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
                map = ["op": op, "type": tipo.toLowerCase(), "dest": dest, "args": args]
            } else {
                String valor = defineValorAtribuicao(listaTokens)
                map = ["op": op, "type": tipo.toLowerCase(), "dest": dest, "value": valor]
            }
            mapList.add(map)
        }
        String ret = buscaValorRetorno(dto)
        mapList.add(["ret":ret])
        return mapList
    }

    static boolean ehMetodo(DTOTipoToken dtoTipoToken) {
        return dtoTipoToken.tipoEstrutura == TipoEstrutura.METODO
    }

    static boolean ehToken(DTOToken dtoToken, TokenPreDefinido tokenPreDefinido) {
        return !dtoToken? false: TokenPreDefinido.obtemToken(dtoToken.desc) == tokenPreDefinido
    }

    private static String defineOperacao(List<DTOToken> dtoTokens) {
        ConvTipo convTipo = null
        for (DTOToken dto : dtoTokens) {
            convTipo = ConvTipo.obtem(dto)
            if (convTipo && convTipo != ConvTipo.CONST) {
                break
            }
        }
        return convTipo ? convTipo.tipo : ConvTipo.CONST.tipo
    }

    private static String defineTipoOperacao(String op, List<DTOToken> dtoTokens) {
        List<String> listaOp = ['add', 'sub', 'mul', 'div']
        DTOToken anterior = null
        List<DTOTipoToken> listaTiposDto = mapDadosMapeados["listaTiposDto"] as List<DTOTipoToken>
        if (op in listaOp) {
            return 'int'
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
        List<DTOToken> filtrado = dtoTokens[1..dtoTokens.size()-1]
        List<DTOToken> args = [] as List<DTOToken>
        for (DTOToken dto : filtrado) {
            args += ehToken(dto, TokenPreDefinido.IDENTIFICADOR)? dto : []
        }
        return args?.collect{it -> it.simb}
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
        if(!dtoTokens.find{it->ehToken(it, TokenPreDefinido.PONTO_VIRGULA)}) {
            return [dtoTokens]
        }
        for(DTOToken dto : dtoTokens) {
            if(ehToken(dto, TokenPreDefinido.PONTO_VIRGULA)) {
                listaLista.add(lista)
                lista = []
                continue
            }
            lista.add(dto)
        }
        return listaLista
    }

    static List<Map<String, String>> buscaParametros(DTOTipoToken dtoTipoToken) {
        if(dtoTipoToken.tipoEstrutura != TipoEstrutura.METODO) {
            return []
        }
        NodeToken nodeParametros = dtoTipoToken.params['parametros']
        List<DTOToken> listaDtoParametros = nodeParametros.dtosDaMesmaRegra.findAll{it ->
            ehToken(it, TokenPreDefinido.IDENTIFICADOR)
        }
        List<DTOTipoToken> listaVariaveis = mapDadosMapeados['listaVariavel'] as List<DTOTipoToken>
        return listaDtoParametros? listaDtoParametros.collect { it ->
            String type = listaVariaveis.find{tipoToken -> tipoToken.dtoToken == it}?.tipoOperacao?.id
            ["name": it.simb, "type": type.toLowerCase()]
        } : []
    }

    static String buscaValorRetorno(DTOTipoToken dto) {
        NodeToken nodeFeature = dto.params["instrucoes"]
        List<List<DTOToken>> dtoListList = separaListasPorPontoVirgula(nodeFeature.dtosDaMesmaRegra)
        List<DTOToken> ultimaLinha = dtoListList.last()
        String dest =  defineDestinoOperacao(ultimaLinha)
        return dest
    }
}
