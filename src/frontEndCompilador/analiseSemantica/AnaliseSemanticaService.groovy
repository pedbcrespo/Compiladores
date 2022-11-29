package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.regras.Expressao
import frontEndCompilador.analizeSintatica.regras.RType
import frontEndCompilador.dto.DTOTipoCorresp
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
            new DTOTipoToken(new DTOToken(TokenPreDefinido.SOMA), TipoBloco.INT),
            new DTOTipoToken(new DTOToken(TokenPreDefinido.ATRIBUICAO), TipoBloco.OBJECT),
    ]

    private static final List<DTOTipoCorresp> valorCorrespOperacao = [
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR), TipoBloco.BOOLEAN), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR_IGUAL), TipoBloco.BOOLEAN), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR), TipoBloco.BOOLEAN), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR_IGUAL), TipoBloco.BOOLEAN), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.SOMA), TipoBloco.INT), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.SUBTRACAO), TipoBloco.INT), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.ASTERISTICO), TipoBloco.INT), TipoBloco.INT),
            new DTOTipoCorresp(new DTOTipoToken(new DTOToken(TokenPreDefinido.DIVISAO), TipoBloco.INT), TipoBloco.INT),
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

    private static boolean analizaSequencia(List<DTOToken> lista) {
        int qtdTokenOperacao = 0
        int pos, inicial = 0
        int posPrimeiraTokenOperacao = 0
        List<List<DTOToken>> listaListasDtos = []
        while (true) {
            for (pos = 0; pos < lista.size(); pos++) {
                DTOToken dto = lista[pos]
                qtdTokenOperacao += dto in tiposTokenOperacoes*.dtoToken ? 1 : 0
                posPrimeiraTokenOperacao = qtdTokenOperacao == 1 ? pos: posPrimeiraTokenOperacao
                if (qtdTokenOperacao > 1 || pos == lista.size()-1) {
                    listaListasDtos.add(lista[inicial..pos-1])
                    break
                }
            }
            if(pos == lista.size()-1){
                break
            }
            lista = lista[posPrimeiraTokenOperacao+1..lista.size()-1]
        }
        listaListasDtos.findResults{it -> analisaOperacao(it)}
        return true
    }

    private static boolean verificaStack(NodeToken node, DTOToken dtoToken) {
        if (node.regraNode.dtoCabeca == dtoToken) {
            return true
        }
        boolean validade = false
        for (NodeToken prox : node.proximosNodes) {
            validade = validade || verificaStack(prox, dtoToken)
        }
        return validade
    }

    private static void analisaOperacao(List<DTOToken> lista) {
        List<DTOToken> buffer = lista
        DTOTipoCorresp tipoOperacao = null
        for (DTOToken dto : buffer) {
            tipoOperacao = valorCorrespOperacao.find { it ->
                it.dtoTipo.dtoToken == dto
            }
            if (tipoOperacao) {
                buffer.remove(dto)
                break
            }
        }
        Set<TipoBloco> listaTipos = (Set<TipoBloco>) buffer.collect { it -> analizaToken(it) }.toSet()
        if (listaTipos.size() > 1 || listaTipos[0] != tipoOperacao.tipoEsperado) {
            throw new Exception("ERRO UTILIZACAO DE TIPO NA OPERACAO ${tipoOperacao.dtoTipo.dtoToken.simb}")
        }
    }
}
