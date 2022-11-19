package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class Programa extends RegraEstrutura {
    Programa() {
        super([
                new DTOHashToken(TokenPreDefinido.CLASS, { -> new Classe() }),
                new DTOHashToken(TokenPreDefinido.PONTO_VIRGULA, { -> null })
        ])
    }

    Programa(DTOToken dtoToken, List<DTOHashToken> dtoHashTokens) {
        super(dtoToken, dtoHashTokens)
    }

    protected Boolean validaExcessaoToken(DTOToken dtoToken) {
        throw new Exception("ERRO TOKEN: ${pilhaDtoLida[0].simb}")
    }

//    @Override
//    protected void adicionaNodeSubArvore(){
//        RegraEstrutura instancia = new Programa(dtoTokenFornecida, this.chaveProximoPasso)
//        if(!nodeToken) {
//            nodeToken = new NodeToken(instancia)
//            return
//        }
//        nodeToken.addNode(instancia)
//    }
}
