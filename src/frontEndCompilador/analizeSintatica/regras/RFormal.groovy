package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RFormal extends RegraEstrutura {
    private List<TokenPreDefinido> sequenciaAceita = [
            TokenPreDefinido.IDENTIFICADOR,
            TokenPreDefinido.DOIS_PONTOS
    ]

    RFormal() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.DOIS_PONTOS, { -> new RType() }),
        ])
    }

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        List<TokenPreDefinido> listaToken = [TokenPreDefinido.ABRE_CHAVE, TokenPreDefinido.FECHA_PARENTESES]
        if(TokenPreDefinido.obtemToken(dtoToken.desc) in listaToken) {
            desfazProcessoAdicaoPilha()
            dtoTokenFornecida = null
            return true
        }
        return false
    }

    @Override
    protected void validacaoSequenciaTokens(NodeToken nodeProximaEtapa) {
        int contSequenciaTokens = 0
        for (int pos = 0; pos < pilhaTokensLidosPorInstancia.size(); pos++) {
            contSequenciaTokens += pilhaTokensLidosPorInstancia[pos] == sequenciaAceita[pos] ? 1 : 0
            if ((pilhaTokensLidosPorInstancia[pos] != sequenciaAceita[pos])) {
                break
            }
        }
        if(contSequenciaTokens > 0 && contSequenciaTokens < sequenciaAceita.size()){
            throw new Exception("ERROR SEQUENCIA INVALIDA: ${pilhaDtoLida[0]}")
        }
    }

//    @Override
//    protected void adicionaNodeSubArvore(){
//        RegraEstrutura instancia = new RFormal(dtoTokenFornecida, this.chaveProximoPasso)
//        if(!nodeToken) {
//            nodeToken = new NodeToken(instancia)
//            return
//        }
//        nodeToken.addNode(instancia)
//    }
}
