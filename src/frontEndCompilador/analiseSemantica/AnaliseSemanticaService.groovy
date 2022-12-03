package frontEndCompilador.analiseSemantica

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.*
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TipoEstrutura
import frontEndCompilador.enums.TipoOperacaoCorresp
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {
    private static NodeToken arvoreGerada
    private static Set<DTOTipoToken> listaTiposDto = []
    private static Set<DTOTipoToken> listaClasses = []
    private static Set<DTOTipoToken> listaFeatures = []
    private static Set<DTOTipoToken> listaVariavel = []

    static void analizaTiposNaArvore(NodeToken nodeToken) {
        arvoreGerada = nodeToken
        identificaClasses(nodeToken)
        identificaFeatures(nodeToken)
        identificarVariaveis(nodeToken)
        juntaTudoNaListaTiposDto()
        validaInstanciasTipo(nodeToken)
        analizaSemanticaSequencias(nodeToken)
    }

    private static void analizaSemanticaSequencias(NodeToken nodeToken) {
        RegraEstrutura regraAtual = nodeToken.regraNode
        if (!(regraAtual.getClass() == Expressao)) {
            for (NodeToken prox : nodeToken.proximosNodes) {
                analizaSemanticaSequencias(prox)
            }
        } else if (!nodeToken.proximosNodes) {
            List<DTOToken> listaSemChaves = nodeToken.dtosDaMesmaRegra.findAll { it ->
                !ehDtoToken(it, [TokenPreDefinido.ABRE_CHAVE, TokenPreDefinido.FECHA_CHAVE])
            }
            analizaSequenciaSimples(listaSemChaves, nodeToken)
        } else {
            analizaSequenciaNaoSimples(nodeToken)
        }
    }

    private static TipoBloco analizaToken(DTOToken dto, NodeToken nodeToken) {
        if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.TEXTO) {
            return TipoBloco.STRING
        }
        TipoBloco tipo
        tipo = dtoEhChamadaDeMetodo(dto) ? trataCasoChamadaDeMetodo(dto, nodeToken) : listaTiposDto.find { it ->
            it.dtoToken == dto
        }?.tipoOperacao?.tipoBloco
        return tipo ?: TipoBloco.OBJECT
    }

    private static boolean analizaSequenciaSimples(List<DTOToken> lista, NodeToken nodeToken) {
        if (!lista) {
            return true
        }
        int contTokenOperacao
        int pos
        Integer posPrimeiraTokenOperacao
        int totalTokensOperacao = 0
        List<DTOToken> buffer = lista
        lista.findResults { it ->
            totalTokensOperacao += TipoOperacaoCorresp.ehTokenOperacao(it) ? 1 : 0
        }
        if (totalTokensOperacao == 0) {
            lista.findResults { it ->
                totalTokensOperacao += ehDtoToken(it, TokenPreDefinido.ATRIBUICAO) ? 1 : 0
            }
        }
        List<List<DTOToken>> listaListasDtos = []
        while (true) {
            contTokenOperacao = 0
            posPrimeiraTokenOperacao = 0
            for (pos = 0; pos < buffer.size(); pos++) {
                DTOToken dto = buffer[pos]
                contTokenOperacao += TipoOperacaoCorresp.ehTokenOperacao(dto) ? 1 : 0
                posPrimeiraTokenOperacao = verificaStatusContagem(dto, contTokenOperacao, pos, posPrimeiraTokenOperacao)
                if (contTokenOperacao > 1 || ehDtoToken(dto, TokenPreDefinido.VIRGULA) || pos == (buffer.size() - 1)) {
                    int posCorteBuffer = TipoOperacaoCorresp.ehTokenOperacao(dto) ? pos - 1 : pos
                    listaListasDtos.add(buffer[0..posCorteBuffer])
                    break
                }
            }
            if (listaListasDtos.size() == totalTokensOperacao || totalTokensOperacao == 0) {
                break
            }
            buffer = buffer[posPrimeiraTokenOperacao + 1..buffer.size() - 1]
        }
        listaListasDtos.findResults { it -> analisaOperacao(it, nodeToken) }
        return true
    }

    private static void analisaOperacao(List<DTOToken> lista, NodeToken nodeToken) {
        TipoOperacaoCorresp tipoOperacao = TipoOperacaoCorresp.obtemTipo(lista)
        List<DTOToken> buffer = lista.findAll { dto ->
            !(TipoOperacaoCorresp.ehTokenOperacao(dto) ||
                    !ehDtoToken(dto, [
                            TokenPreDefinido.IDENTIFICADOR,
                            TokenPreDefinido.TEXTO,
                            TokenPreDefinido.TRUE,
                            TokenPreDefinido.FALSE
                    ]))
        }
        Set<TipoBloco> listaTipos = (Set<TipoBloco>) buffer.collect { it -> analizaToken(it, nodeToken) }.toSet()
        if (listaTipos.size() > 1 || !verificaTipoCorrespondente(listaTipos, tipoOperacao)) {
            String erro = lista*.simb.join(' ')
            throw new Exception("ERRO UTILIZACAO DE TIPO NA OPERACAO ${erro}")
        }
    }

    private static void validaInstanciasTipo(NodeToken nodeToken) {
        if (!(nodeToken.regraNode.getClass() == RType)) {
            for (NodeToken prox : nodeToken.proximosNodes) {
                validaInstanciasTipo(prox)
            }
        } else {
            DTOToken dtoInstancia = nodeToken.dtosDaMesmaRegra[0]
            if (!(dtoInstancia.simb[0] == dtoInstancia.simb[0].toUpperCase())) {
                throw new Exception("Instancia ${dtoInstancia.simb} INVALIDA")
            }
        }
    }

    private static void analizaSequenciaNaoSimples(NodeToken nodeToken) {
        if (nodeToken.proximosNodes) {
            for (NodeToken prox : nodeToken.proximosNodes) {
                analizaSequenciaNaoSimples(prox)
            }
        }
        if (!nodeToken.dtosDaMesmaRegra || !(nodeToken.regraNode.getClass() == Expressao)) {
            return
        }
        analizaSequenciaSimples(nodeToken.dtosDaMesmaRegra, nodeToken)
    }

    private static boolean ehDtoToken(DTOToken dtoToken, TokenPreDefinido tokenPreDefinido) {
        if (!dtoToken) {
            return false
        }
        return TokenPreDefinido.obtemToken(dtoToken.desc) == tokenPreDefinido
    }

    private static boolean ehDtoToken(DTOToken dtoToken, List<TokenPreDefinido> listaTokens) {
        for (TokenPreDefinido token : listaTokens) {
            if (ehDtoToken(dtoToken, token)) {
                return true
            }
        }
        return false
    }

    private static boolean verificaTipoCorrespondente(Set<TipoBloco> tipoBlocos, TipoOperacaoCorresp tipoCorresp) {
        if (!tipoCorresp) {
            return true
        }
        return (tipoBlocos[0] == tipoCorresp.tipoBloco)
    }

    private static int verificaStatusContagem(DTOToken dto, int cont, int pos, int posInicial) {
        return (TipoOperacaoCorresp.ehTokenOperacao(dto) && cont == 1) || ehDtoToken(dto, TokenPreDefinido.VIRGULA) ? pos : posInicial
    }

    private static boolean ehMetodo(NodeToken node, DTOToken dto) {
        RegraEstrutura regra = node.regraNode
        boolean resultado = false
        if (regra.getClass() == Feature && regra.dtoCabeca == dto) {
            List<DTOToken> listaParenteses = [
                    new DTOToken(TokenPreDefinido.ABRE_PARENTESES),
                    new DTOToken(TokenPreDefinido.FECHA_PARENTESES)
            ]
            resultado = node.dtosDaMesmaRegra.containsAll(listaParenteses)
        }
        for (NodeToken prox : node.proximosNodes) {
            resultado = resultado || ehMetodo(prox, dto)
        }
        return resultado
    }

    private static boolean percorreArvoreBuscandoMetodo(NodeToken node, DTOToken classe, DTOToken metodo, DTOToken classeAtual = null) {
        RegraEstrutura regra = node.regraNode
        boolean resultado = false
        if (regra.getClass() == Classe) {
            classeAtual = node.dtosDaMesmaRegra[0]
        }
        if (regra.getClass() == RFeature && classeAtual == classe) {
            DTOToken dtoMetodoBuscado = node.dtosDaMesmaRegra.find { it -> it == metodo }
            resultado = dtoMetodoBuscado
        } else {
            for (NodeToken prox : node.proximosNodes) {
                resultado = resultado || percorreArvoreBuscandoMetodo(prox, classe, metodo, classeAtual)
            }
        }
        return resultado
    }

    private static DTOTipoToken geraDtoTipoTokenClasse(DTOToken classe, NodeToken nodeToken) {
        DTOToken anterior = null
        DTOTipoToken tipoToken = null
        String idTipo
        for (DTOToken dto : nodeToken.dtosDaMesmaRegra) {
            if (ehDtoToken(anterior, TokenPreDefinido.INHERITS)) {
                idTipo = dto.simb
                TipoBloco tipoBlocoEnum = TipoBloco.obtemTipo(dto)?: TipoBloco.OBJECT
                TipoBlocoEst tipoBloco = new TipoBlocoEst(idTipo, tipoBlocoEnum)
                tipoToken = new DTOTipoToken(dto, classe, tipoBloco, TipoEstrutura.CLASSE)
            }
            anterior = dto
        }
        if (!tipoToken) {
            idTipo = classe.simb
            TipoBlocoEst tipoBloco = new TipoBlocoEst(idTipo, TipoBloco.OBJECT)
            tipoToken = new DTOTipoToken(classe, tipoBloco, TipoEstrutura.CLASSE)
        }
        return tipoToken
    }

    private static DTOTipoToken geraDtoTipoTokenFeature(DTOToken feature, NodeToken nodeToken, DTOToken classe) {
        DTOToken tipo
        DTOTipoToken resultado = null
        TipoEstrutura tipoEstrutura
        RegraEstrutura regraAtual = nodeToken.regraNode
        Map params = [:]
        if (regraAtual.getClass() == Feature && regraAtual.dtoCabeca == feature) {
            if (ehMetodo(nodeToken, feature)) {
                params.put('parametros', nodeToken.proximosNodes[0])
                tipo = nodeToken.proximosNodes[1].dtosDaMesmaRegra[0]
                tipoEstrutura = TipoEstrutura.METODO
            } else {
                tipo = nodeToken.proximosNodes[0].dtosDaMesmaRegra[0]
                tipoEstrutura = TipoEstrutura.ATRIBUTO
            }
            if (!tipoExistente(tipo, classe)) {
                throw new Exception("TIPO DE INSTANCIA INEXISTENTE: ${tipo.simb}")
            }
            TipoBloco tipoBlocoEnum = TipoBloco.obtemTipo(tipo)?: TipoBloco.OBJECT
            TipoBlocoEst tipoBloco = ehDtoToken(tipo, TokenPreDefinido.SELF_TYPE) ?
                    new TipoBlocoEst(classe.simb, tipoBlocoEnum):
                    new TipoBlocoEst(tipo.simb, tipoBlocoEnum)
            resultado = new DTOTipoToken(classe, feature, tipoBloco, tipoEstrutura, params)
        } else {
            for (NodeToken prox : nodeToken.proximosNodes) {
                resultado = geraDtoTipoTokenFeature(feature, prox, classe)
                if (resultado) {
                    break
                }
            }
        }
        return resultado
    }

    private static void identificaClasses(NodeToken nodeToken) {
        RegraEstrutura regraAtual = nodeToken.regraNode
        if (regraAtual.getClass() == Classe) {
            DTOToken classe = validaTituloClasse(nodeToken.dtosDaMesmaRegra[0])
            DTOTipoToken tipoClasse = geraDtoTipoTokenClasse(classe, nodeToken)
            listaClasses.add(tipoClasse)
        } else {
            for (NodeToken prox : nodeToken.proximosNodes) {
                identificaClasses(prox)
            }
        }
    }

    private static void identificaFeatures(NodeToken nodeToken, DTOToken classeAtual = null) {
        RegraEstrutura regraAtual = nodeToken.regraNode
        if (regraAtual.getClass() == Classe) {
            classeAtual = nodeToken.dtosDaMesmaRegra[0]
        }
        if (regraAtual.getClass() == RFeature) {
            List<DTOToken> features = nodeToken.dtosDaMesmaRegra.findAll { it ->
                !ehDtoToken(it, TokenPreDefinido.PONTO_VIRGULA)
            }
            for (DTOToken feature : features) {
                listaFeatures += geraDtoTipoTokenFeature(feature, nodeToken, classeAtual)
            }
        } else {
            for (NodeToken prox : nodeToken.proximosNodes) {
                identificaFeatures(prox, classeAtual)
            }
        }
    }

    private static void identificarVariaveis(NodeToken nodeToken, NodeToken anterior = null, DTOToken classeAtual = null) {
        RegraEstrutura regraAtual = nodeToken.regraNode
        RegraEstrutura regraAnterior = anterior?.regraNode
        List<DTOTipoToken> listaDtoVariaveis = []
        DTOTipoToken dtoTipoToken
        if (regraAtual.getClass() == Classe) {
            classeAtual = nodeToken.dtosDaMesmaRegra[0]
        } else if (regraAtual.getClass() == RType && regraAnterior.getClass() == RFormal) {
            DTOToken dto = regraAtual.dtoCabeca
            DTOToken tipo = nodeToken.dtosDaMesmaRegra[0]
            TipoBloco tipoBlocoEnum = TipoBloco.obtemTipo(tipo)?: TipoBloco.OBJECT
            dtoTipoToken = new DTOTipoToken(classeAtual, dto,
                    new TipoBlocoEst(tipo.simb, tipoBlocoEnum), TipoEstrutura.VARIAVEL)
            listaDtoVariaveis.add(dtoTipoToken)
        } else if (regraAtual.getClass() == Expressao) {
            List<DTOToken> dtoIdentificadores = nodeToken.dtosDaMesmaRegra.findAll { it ->
                ehDtoToken(it, TokenPreDefinido.IDENTIFICADOR) && !ehFeature(it)
            }
            listaDtoVariaveis += (List<DTOTipoToken>) dtoIdentificadores.collect { it ->
                TipoBloco tipoBloco = TipoBloco.obtemTipo(it)?: TipoBloco.OBJECT
                new DTOTipoToken(it, new TipoBlocoEst(it.simb, tipoBloco), TipoEstrutura.VARIAVEL)
            }
        }
        for (NodeToken prox : nodeToken.proximosNodes) {
            identificarVariaveis(prox, nodeToken, classeAtual)
        }
        listaVariavel.addAll(listaDtoVariaveis)
    }

    private static DTOToken validaTituloClasse(DTOToken dtoToken) {
        if (dtoToken.simb[0] != dtoToken.simb[0].toUpperCase()) {
            throw new Exception("ERRO INSTANCIA DA CLASSE ${dtoToken.simb}")
        }
        return dtoToken
    }

    private static boolean ehFeature(DTOToken dtoToken) {
        DTOTipoToken featureBuscada = listaFeatures.find { it -> it.dtoToken == dtoToken }
        return featureBuscada
    }

    private static void juntaTudoNaListaTiposDto() {
        listaTiposDto = listaClasses + listaFeatures + listaVariavel
    }

    private static TipoBloco trataCasoChamadaDeMetodo(DTOToken dto, NodeToken nodeDaChamada) {
        TipoBloco tipoBlocoRetorno
        String nomeAtributoChamadorDoMetodo = dto.simb.split('\\.')[0]
        String nomeMetodo = dto.simb.split('\\.')[1]
        DTOTipoToken metodoChamado = listaFeatures.find { it ->
            it.dtoToken.simb == nomeMetodo &&
                    it.tipoEstrutura == TipoEstrutura.METODO
        }
        DTOTipoToken variavelChamadoraDoMetodo = listaTiposDto.find { it ->
            it.dtoToken.simb == nomeAtributoChamadorDoMetodo &&
                    it.tipoEstrutura == TipoEstrutura.ATRIBUTO
        }
        if (variavelChamadoraDoMetodo.tipoOperacao.id != metodoChamado.dtoClasse.simb) {
            throw new Exception("ATRIBUTO: ${nomeAtributoChamadorDoMetodo} NAO PODE CHAMAR METODO DE OUTRO TIPO")
        }
        List<DTOToken> parametrosPassadosChamadaMetodo = buscaParametrosPassadosMetodo(nodeDaChamada, dto)
        List<DTOToken> dtosDaMesmaRegra = metodoChamado.params['parametros'].dtosDaMesmaRegra
        List<DTOToken> parametrosDoMetodo = dtosDaMesmaRegra.findAll { it ->
            ehDtoToken(it, TokenPreDefinido.IDENTIFICADOR)
        }
        if (parametrosPassadosChamadaMetodo.size() > parametrosDoMetodo.size()) {
            throw new Exception("EXCESSO DE PARAMETROS PASSADOS NA CHAMADA DE METODO: ${dto.simb}")
        } else if (parametrosPassadosChamadaMetodo.size() < parametrosDoMetodo.size()) {
            throw new Exception("FALTA DE PARAMETROS PASSADOS NA CHAMADA DE METODO: ${dto.simb}")
        }
        int pos = 0
        for (DTOToken parametroPassado : parametrosPassadosChamadaMetodo) {
            DTOTipoToken parametroDoMetodo = listaVariavel.find { it -> it.dtoToken == parametrosDoMetodo[pos] }
            DTOTipoToken paramPassadoComTipo = listaVariavel.find { it -> it.dtoToken == parametroPassado }
            if (!(paramPassadoComTipo.tipoOperacao.tipoBloco == parametroDoMetodo.tipoOperacao.tipoBloco)) {
                throw new Exception("ERRO PARAMETRO: ${parametroPassado.simb} INCORRETO NA CHAMADA ${dto.simb}")
            }
            pos += 1
        }
        tipoBlocoRetorno = metodoChamado.tipoOperacao.tipoBloco
        return tipoBlocoRetorno
    }

    private static void validaParametrosCorretosMetodo(DTOTipoToken dtoTipoToken, List<String> parametros, NodeToken node = arvoreGerada) {
        RegraEstrutura regraAtual = node.regraNode
        if (regraAtual.getClass() == Feature && regraAtual.dtoCabeca == dtoTipoToken.dtoToken) {
            List<DTOTipoToken> variaveisDoMetodo = listaVariavel.findAll { it -> it.dtoToken in node.proximosNodes[1].dtosDaMesmaRegra }
            int pos = 0
            for (String param : parametros) {
                TipoBloco tipoParametro = TipoBloco.obtemTipo(new DTOToken(param, TokenPreDefinido.IDENTIFICADOR.name()))?: TipoBloco.OBJECT
                if (tipoParametro != variaveisDoMetodo[pos].tipoOperacao.tipoBloco) {
                    throw new Exception("TIPO DE PARAMETRO ${param} INCOMPATIVEL COM O DEFINIDO NO METODO: ${dtoTipoToken.dtoToken.simb}")
                }
                pos += 1
            }
        } else {
            for (NodeToken prox : node.proximosNodes) {
                validaParametrosCorretosMetodo(dtoTipoToken, parametros, prox)
            }
        }
    }

    private static boolean dtoEhChamadaDeMetodo(DTOToken dtoToken) {
        List<String> simbSplitado = dtoToken.simb.split('\\.')
        return simbSplitado.size() > 1
    }

    private static List<DTOToken> buscaParametrosPassadosMetodo(NodeToken nodeToken, DTOToken dtoToken) {
        Integer posProximoNode = null
        int pos = 0
        for (DTOToken dto : nodeToken.dtosDaMesmaRegra) {
            posProximoNode = !dtoEhChamadaDeMetodo(dto) ? null : pos
            pos += dtoEhChamadaDeMetodo(dto) ? 1 : 0
            if (dto == dtoToken) {
                break
            }
        }
        return posProximoNode != null ? nodeToken.proximosNodes[posProximoNode].dtosDaMesmaRegra : []
    }

    private static boolean tipoExistente(DTOToken dtoToken, DTOToken classe) {
        DTOTipoToken tipoExistente = listaClasses.find { it -> it.dtoToken == dtoToken }
        return tipoExistente || TipoBloco.obtemTipo(dtoToken) || ehDtoToken(dtoToken, TokenPreDefinido.SELF_TYPE)
    }
}
