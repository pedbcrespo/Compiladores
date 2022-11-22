package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RegraEstrutura {
    private UUID uuid
    protected List<TokenPreDefinido> pilhaTokensLidosPorInstancia = []
    protected static List<DTOToken> listaDtoFornecida
    protected static List<DTOToken> pilhaDtoLida = []
    protected DTOToken dtoTokenFornecida
    protected TokenPreDefinido tokenChamadaAnterior
    protected List<DTOHashToken> chaveProximoPasso
    protected NodeToken nodeToken

    RegraEstrutura(List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso
        this.nodeToken = null
        this.uuid = UUID.randomUUID()
    }

    RegraEstrutura(TokenPreDefinido tokenChamadaAnterior, List<DTOHashToken> chaveProximoPasso) {
        this.tokenChamadaAnterior = tokenChamadaAnterior
        this.chaveProximoPasso = chaveProximoPasso
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
            validacaoSequenciaTokens()
            if(casoEspecifico(dtoTokenFornecida)){
                break
            }
            if (!validaProximaEtapa(dtoTokenFornecida)) {
                if (validaExcessaoToken(dtoTokenFornecida))
                    break
            }
            if (!listaDtoFornecida) {
                validaAbreFecha()
            }
        }
        return nodeToken
    }

    protected void validacaoSequenciaTokens() {}

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        return true
    }

    protected Boolean casoEspecifico(DTOToken dtoToken) {
        return false
    }

    protected void adicionaNodeSubArvore() {
        if (!nodeToken) {
            nodeToken = new NodeToken(this)
            return
        }
        nodeToken.addNode(this)
    }

    private static Boolean validaAbreFecha() {
        if (pilhaDtoLida[0].desc == TokenPreDefinido.ABRE_CHAVE.name() ||
                pilhaDtoLida[0].desc == TokenPreDefinido.ABRE_PARENTESES.name()) {
            return true
        }
        int paridadeAbreFechaChave = 0
        int paridadeAbreFechaParenteses = 0
        for (DTOToken dto : pilhaDtoLida) {
            TokenPreDefinido token = TokenPreDefinido.obtemToken(dto.desc)
            paridadeAbreFechaChave += token == TokenPreDefinido.ABRE_CHAVE ? 1 :
                    token == TokenPreDefinido.FECHA_CHAVE ? -1 : 0
            paridadeAbreFechaParenteses += token == TokenPreDefinido.ABRE_PARENTESES ? 1 :
                    token == TokenPreDefinido.FECHA_PARENTESES ? -1 : 0
        }
        if (paridadeAbreFechaParenteses != 0 || paridadeAbreFechaChave != 0) {
            throw new Exception("ERROR PARENTES OU CHAVES FALTANDO")
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
        nodeToken.addNode(proximaEtapa.analisa())
        return true
    }

    private static void removePrimeiroElementoListaToken() {
        listaDtoFornecida = listaDtoFornecida.size() > 1 ? listaDtoFornecida.subList(1, listaDtoFornecida.size()) : []
    }

    private void adicionaTokenPilha(DTOToken dtoToken) {
        pilhaTokensLidosPorInstancia += TokenPreDefinido.obtemToken(dtoToken.desc) ?: []
        pilhaDtoLida.add(0, dtoToken)
    }

    private static void desfazProcessoAdicaoPilha() {
        listaDtoFornecida.add(0, pilhaDtoLida[0])
        pilhaDtoLida.remove(0)
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
}
