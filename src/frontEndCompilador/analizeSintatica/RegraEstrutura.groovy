package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RegraEstrutura {
    protected List<TokenPreDefinido> pilhaTokensLidosPorInstancia = []
    protected static List<DTOToken> listaDtoFornecida
    protected static List<DTOToken> pilhaDtoLida = []
    protected List<DTOHashToken> chaveProximoPasso
    protected NodeToken nodeToken

    RegraEstrutura(List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso
        this.nodeToken = new NodeToken(null, null)
    }

    static void setListaDtoTokenFornecida(List<DTOToken> listaDto) {
        listaDtoFornecida = listaDto
    }

    NodeToken analisa(NodeToken anterior) {
        nodeToken.anterior = anterior
        while (listaDtoFornecida.size() > 0) {
            DTOToken dtoTokenFornecida = listaDtoFornecida[0]
            adicionaTokenPilha(dtoTokenFornecida)
            adicionaNodeSubArvore(dtoTokenFornecida)
            removePrimeiroElementoListaToken()
            validacaoSequenciaTokens()
            if (!validaProximaEtapa(dtoTokenFornecida)) {
                if(validaExcessaoToken(dtoTokenFornecida))
                    break
            }
            if(!listaDtoFornecida) {
                validaAbreFecha()
            }
        }
        return nodeToken
    }

    protected void validacaoSequenciaTokens() {}

    protected void adicionaNodeSubArvore(DTOToken dtoToken){
        nodeToken.addNode(dtoToken)
    }

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        return true
    }

    private Boolean validaAbreFecha() {
        if(pilhaDtoLida[0].desc == TokenPreDefinido.ABRE_CHAVE.name() ||
                pilhaDtoLida[0].desc == TokenPreDefinido.ABRE_PARENTESES.name()){
            return true
        }
        int paridadeAbreFechaChave = 0
        int paridadeAbreFechaParenteses = 0
        for (DTOToken dto: pilhaDtoLida) {
            TokenPreDefinido token = TokenPreDefinido.obtemToken(dto.desc)
            paridadeAbreFechaChave += token == TokenPreDefinido.ABRE_CHAVE? 1:
                    token == TokenPreDefinido.FECHA_CHAVE? -1: 0
            paridadeAbreFechaParenteses += token == TokenPreDefinido.ABRE_PARENTESES? 1:
                    token == TokenPreDefinido.FECHA_PARENTESES? -1: 0
        }
        if(paridadeAbreFechaParenteses != 0 || paridadeAbreFechaChave != 0) {
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
        nodeToken.addSubArvore(proximaEtapa.analisa(nodeToken))
        return true
    }

    private void removePrimeiroElementoListaToken() {
        listaDtoFornecida = listaDtoFornecida.size() > 1 ? listaDtoFornecida.subList(1, listaDtoFornecida.size()) : []
    }

    private void adicionaTokenPilha(DTOToken dtoToken) {
        pilhaTokensLidosPorInstancia += TokenPreDefinido.obtemToken(dtoToken.desc) ?: []
        pilhaDtoLida.add(0, dtoToken)
    }

    private void desfazProcessoAdicaoPilha() {
        listaDtoFornecida.add(0, pilhaDtoLida[0])
        pilhaDtoLida.remove(0)
    }
}
