package frontEndCompilador.analizeSintatica.regras

import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.dto.DTOHashToken
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
    protected void validacaoSequenciaTokens() {
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
}
