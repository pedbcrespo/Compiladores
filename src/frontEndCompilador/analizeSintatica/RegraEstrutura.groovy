package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RegraEstrutura {
    protected static List<TokenPreDefinido> pilhaTokensLidos = []
    protected static List<DTOToken> listaDtoFornecida
    protected static NodeToken nodeToken = null
    protected List<DTOHashToken> chaveProximoPasso
    protected RegraEstrutura anterior

    RegraEstrutura(List<DTOHashToken> chaveProximoPasso) {
        this.chaveProximoPasso = chaveProximoPasso

    }

    static void setListaDtoTokenFornecida(List<DTOToken> listaDto) {
        listaDtoFornecida = listaDto
    }

    static void setNodeToken(NodeToken nodeToken) {
        this.nodeToken = nodeToken
    }

    void analisaChaveFornecida(RegraEstrutura anterior) {
        if (!listaDtoFornecida) {
            return
        }
        DTOToken dtoTokenFornecida = listaDtoFornecida[0]
        pilhaTokensLidos.add(0, TokenPreDefinido.obtemToken(dtoTokenFornecida.desc))
        this.anterior = anterior
        DTOHashToken dtoHashTokenComProximoPasso = chaveProximoPasso.find { DTOHashToken dto ->
            dto.tokenChave == TokenPreDefinido.obtemToken(dtoTokenFornecida.desc)
        }
        setNodeToken(new NodeToken(dtoTokenFornecida))
        if (!dtoHashTokenComProximoPasso) {
            if (anterior) {
                anterior.analisaChaveFornecida(anterior.anterior)
            }
            verificaExcessao()
            return
        }
        RegraEstrutura proximaEtapa

        listaDtoFornecida = listaDtoFornecida.size() > 1 ? listaDtoFornecida.subList(1, listaDtoFornecida.size()) : []
        proximaEtapa = dtoHashTokenComProximoPasso.proximaEtapa()
        if (!proximaEtapa) {
            return
        }
        proximaEtapa.analisaChaveFornecida(this)
    }

    protected void verificaExcessao() {}
}
