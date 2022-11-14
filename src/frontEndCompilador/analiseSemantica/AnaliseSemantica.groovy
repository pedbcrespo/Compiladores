package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemantica {

    private static final List<TokenPreDefinido> listaTokensBloco = [
            TokenPreDefinido.ABRE_CHAVE,
            TokenPreDefinido.ABRE_PARENTESES
    ]

    static void analisaArvore(List<DTOToken> listaDto) {
        List<BlocoToken> listaBlocoToken = AnaliseSemanticaService.organizaBlocos(listaDto)
        listaBlocoToken.findResults{ BlocoToken bloco -> bloco.validade()}
    }
}
