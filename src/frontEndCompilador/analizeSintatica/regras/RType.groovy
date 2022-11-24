package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class RType extends RegraEstrutura {


    RType() {
        super([
                new DTOHashToken(TokenPreDefinido.IDENTIFICADOR, { -> null }),
                new DTOHashToken(TokenPreDefinido.SELF, { -> null }),
                new DTOHashToken(TokenPreDefinido.SELF_TYPE, { -> null }),
        ])
    }

    @Override
    protected Boolean casoEspecifico(DTOToken dtoToken) {
        if(TokenPreDefinido.obtemToken(dtoToken.desc) == TokenPreDefinido.IDENTIFICADOR) {
            return true
        }
        return false
    }

    protected void validacaoSequenciaTokens() {
        List<TokenPreDefinido> listaTokensChaves = [
                TokenPreDefinido.IDENTIFICADOR,
                TokenPreDefinido.SELF,
                TokenPreDefinido.SELF_TYPE
        ]
        if (!listaTokensChaves.contains(pilhaTokensLidosPorInstancia[0])) {
            throw new Exception("ERROR ${pilhaTokensLidosPorInstancia[0]} SEQUENCIA INVALIDA")
        }

    }

//    @Override
//    protected void adicionaNodeSubArvore(){
//        RegraEstrutura instancia = new RType(dtoTokenFornecida, this.chaveProximoPasso)
//        if(!nodeToken) {
//            nodeToken = new NodeToken(instancia)
//            return
//        }
//        nodeToken.addNode(instancia)
//    }
}
