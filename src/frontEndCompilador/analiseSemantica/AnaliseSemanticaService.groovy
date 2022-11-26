package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.regras.Expressao
import frontEndCompilador.analizeSintatica.regras.RType
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static Set<DTOTipoToken> listaTiposDto = []
    private static final List<DTOTipoToken> tiposTokenOperacoes = [
            new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR), TipoBloco.BOOLEAN),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR_IGUAL), TipoBloco.BOOLEAN),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR_IGUAL), TipoBloco.BOOLEAN),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR), TipoBloco.BOOLEAN),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.IGUAL), TipoBloco.BOOLEAN),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.ASTERISTICO), TipoBloco.INT),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.DIVISAO), TipoBloco.INT),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.SUBTRACAO), TipoBloco.INT),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.SOMA), TipoBloco.INT)
    ]

    static void analizaTiposNaArvore(NodeToken nodeToken) {
        identificaTipoTokensInstanciaDireta(nodeToken)
        identificaTipoTokensInstanciaIndireta(nodeToken)
        listaTiposDto
    }

    private static DTOTipoToken geraDto(NodeToken node) {
        TipoBloco tipoBloco = TipoBloco.obtemTipo(node.dtosDaMesmaRegra[0])
        return new DTOTipoToken(node.regraNode.dtoCabeca, tipoBloco)
    }

    private static void identificaTipoTokensInstanciaDireta(NodeToken node) {
        if (classeRType(node)) {
            listaTiposDto.add(geraDto(node))
        }
        for (NodeToken prox : node.proximosNodes) {
            identificaTipoTokensInstanciaDireta(prox)
        }
    }

    private static void identificaTipoTokensInstanciaIndireta(NodeToken node) {
        if (classeExpressao(node)) {
            listaTiposDto.addAll(geraDtoIndireto(node))
        }
        for (NodeToken prox : node.proximosNodes) {
            identificaTipoTokensInstanciaIndireta(prox)
        }
    }

    private static boolean classeRType(NodeToken node) {
        return node.regraNode ? node.regraNode.getClass() == RType : false
    }

    private static boolean classeExpressao(NodeToken node) {
        return node.regraNode ? node.regraNode.getClass() == Expressao : false
    }

    private static List<DTOTipoToken> geraDtoIndireto(NodeToken nodeToken) {
        List<DTOTipoToken> lista = []
        DTOTipoToken dtoTipo = null
        for (DTOToken dto : nodeToken.dtosDaMesmaRegra) {
            TokenPreDefinido token = TokenPreDefinido.obtemToken(dto.desc)
            if (token in [TokenPreDefinido.IDENTIFICADOR, TokenPreDefinido.TEXTO]) {
                TipoBloco tipo = analizaToken(dto)
                dtoTipo = new DTOTipoToken(dto, tipo)
            } else if (dto in tiposTokenOperacoes*.dtoToken) {
                dtoTipo = tiposTokenOperacoes.find { it -> it.dtoToken == dto }
            }
            lista.add(dtoTipo)
        }
        return lista
    }

    private static TipoBloco analizaToken(DTOToken dto) {
        if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.TEXTO) {
            return TipoBloco.STRING
        }
        DTOTipoToken dtoComTipo = listaTiposDto?.find { it -> it?.dtoToken == dto }
        if (dtoComTipo) {
            return dtoComTipo.tipoOperacao
        }
        TipoBloco tipo = dto.simb.isInteger() ? TipoBloco.INT :
                dto.simb in ['True', 'False'] ? TipoBloco.BOOLEAN :
                        dto.simb.contains('"') ? TipoBloco.STRING : TipoBloco.OBJECT
        return tipo
    }

    private static TipoBloco analisaTipoSequencia(List<DTOToken> lista) {
        List<DTOTipoToken> tipoTokenSequencia = []
        for (DTOToken dto : lista) {
            tipoTokenSequencia.add(new DTOTipoToken(dto, analizaToken(dto)))
        }
        boolean mesmoTipo = true
        DTOTipoToken anterior = null
        for (DTOTipoToken dto : tipoTokenSequencia) {
            if (anterior) {
                mesmoTipo = mesmoTipo && dto.tipoOperacao == anterior.tipoOperacao
            }
            anterior = dto
        }
        if (!mesmoTipo) {
            throw new Exception('ERRO ORDEM TIPOS INVALIDOS')
        }
        return tipoTokenSequencia[0].tipoOperacao
    }
}
