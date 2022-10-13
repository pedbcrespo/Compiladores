package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RegraEstrutura {
    protected List<TokenPreDefinido> pilhaTokensLidosPorInstancia = []
    protected static List<DTOToken> listaDtoFornecida
    protected static List<DTOToken> pilhaDtoLida = []
    protected List<DTOHashToken> chaveProximoPasso

    RegraEstrutura(List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso
    }

    static void setListaDtoTokenFornecida(List<DTOToken> listaDto) {
        listaDtoFornecida = listaDto
    }

    void analisa() {
        while (listaDtoFornecida.size() > 0) {
            DTOToken dtoTokenFornecida = listaDtoFornecida[0]
            adicionaTokenPilha(dtoTokenFornecida)
            removePrimeiroElementoListaToken()
            validacaoSequenciaTokens()
            if (!validaProximaEtapa(dtoTokenFornecida)) {
                if(validaExcessaoToken(dtoTokenFornecida))
                    return
            }
        }
    }

    protected void validacaoSequenciaTokens() {}

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
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
        proximaEtapa.analisa()
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
