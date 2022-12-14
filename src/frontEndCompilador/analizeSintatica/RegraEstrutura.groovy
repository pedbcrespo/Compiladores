package frontEndCompilador.analizeSintatica


import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOParToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RegraEstrutura {
    DTOToken dtoCabeca
    protected DTOToken dtoTokenFornecida
    protected NodeToken nodeToken
    protected TokenPreDefinido tokenChave
    protected List<TokenPreDefinido> pilhaTokensLidosPorInstancia = []
    protected List<DTOHashToken> chaveProximoPasso
    protected static List<DTOToken> listaDtoFornecida
    protected static List<DTOToken> pilhaDtoLida = []
    private final UUID uuid

    RegraEstrutura(List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso
        this.nodeToken = null
        this.tokenChave = null
        this.uuid = UUID.randomUUID()
    }


    RegraEstrutura(NodeToken nodeToken, List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso
        this.nodeToken = nodeToken
        this.uuid = UUID.randomUUID()
    }

    static void setListaDtoTokenFornecida(List<DTOToken> listaDto) {
        listaDtoFornecida = listaDto
    }

    NodeToken analisa() {
        while (listaDtoFornecida.size() > 0) {
            dtoTokenFornecida = listaDtoFornecida[0]
            adicionaNodeSubArvore()
            adicionaTokenPilha(dtoTokenFornecida)
            removePrimeiroElementoListaToken()
            if (casoEspecifico(dtoTokenFornecida)) {
                break
            }
            if (!validaProximaEtapa(dtoTokenFornecida)) {
                if (validaExcessaoToken(dtoTokenFornecida))
                    break
            }
        }
        return nodeToken
    }

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        return true
    }

    protected void adicionaNodeSubArvore() {
        if (!nodeToken) {
            nodeToken = new NodeToken(this)
            return
        }
        nodeToken.addNode(this)
    }

    protected static void desfazProcessoAdicaoPilha() {
        listaDtoFornecida.add(0, pilhaDtoLida[0])
        pilhaDtoLida.remove(0)
    }

    protected TokenPreDefinido casoChavePar() {
        TokenPreDefinido token = TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        return [
                TokenPreDefinido.ABRE_PARENTESES,
                TokenPreDefinido.ABRE_CHAVE
        ].find{ it -> it == token}
    }

    protected Boolean casoEspecifico(DTOToken dtoToken) {
        List<DTOParToken> listaCasosEspecificos = [
                new DTOParToken(TokenPreDefinido.ABRE_PARENTESES, TokenPreDefinido.FECHA_PARENTESES),
                new DTOParToken(TokenPreDefinido.ABRE_CHAVE, TokenPreDefinido.FECHA_CHAVE)
        ]
        DTOParToken conjuntoTokenChave = listaCasosEspecificos.find { it ->
            it.correspondeChaveFecha(TokenPreDefinido.obtemToken(dtoToken.desc))
        }
        if (!conjuntoTokenChave || !tokenChave) {
            return false
        } else if (conjuntoTokenChave.obtemChaveFecha(tokenChave)) {
            desfazProcessoAdicaoPilha()
        }
        return true
    }

    private Boolean validaProximaEtapa(DTOToken dtoToken) {
        DTOHashToken dtoHashTokenComProximoPasso = chaveProximoPasso.find { DTOHashToken dto ->
            dto.tokenChave == TokenPreDefinido.obtemToken(dtoToken.desc)
        }
        if (!dtoHashTokenComProximoPasso) {
            desfazProcessoAdicaoPilha()
            return false
        }
        RegraEstrutura proximaEtapa = dtoHashTokenComProximoPasso.proximaEtapa()
        if (!proximaEtapa) {
            return true
        }
        proximaEtapa.tokenChave = casoChavePar()
        proximaEtapa.dtoCabeca = casoDtoCabeca()
        NodeToken nodeProximaEtapa = proximaEtapa.analisa()
        nodeToken.addNode(nodeProximaEtapa)
        return true
    }

    protected void validacaoSequenciaTokens(NodeToken nodeProximaEtapa) {}

    private static void removePrimeiroElementoListaToken() {
        listaDtoFornecida = listaDtoFornecida.size() > 1 ? listaDtoFornecida.subList(1, listaDtoFornecida.size()) : []
    }

    private void adicionaTokenPilha(DTOToken dtoToken) {
        pilhaTokensLidosPorInstancia += TokenPreDefinido.obtemToken(dtoToken.desc) ?: []
        pilhaDtoLida.add(0, dtoToken)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof RegraEstrutura)) return false

        RegraEstrutura that = (RegraEstrutura) o

        if (uuid != that.uuid) return false

        return true
    }

    int hashCode() {
        return uuid.hashCode()
    }

    private DTOToken casoDtoCabeca() {
        TokenPreDefinido token = TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        if(token == TokenPreDefinido.IDENTIFICADOR){
            return dtoTokenFornecida
        }
        List<TokenPreDefinido> listaTokensParaSerCabeca = [
                TokenPreDefinido.IDENTIFICADOR,
                TokenPreDefinido.IF,
                TokenPreDefinido.ELSE,
                TokenPreDefinido.THEN,
                TokenPreDefinido.CLASS,
                TokenPreDefinido.WHILE,
                TokenPreDefinido.LOOP,
                TokenPreDefinido.CASE,
                TokenPreDefinido.LET,
        ]
        for(DTOToken dto : pilhaDtoLida) {
            token = TokenPreDefinido.obtemToken(dto.desc)
            if(token in listaTokensParaSerCabeca) {
                return dto
            }
        }
        return null
    }
}
