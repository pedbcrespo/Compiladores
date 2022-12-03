package frontEndCompilador.enums

import frontEndCompilador.analiseSemantica.TipoBlocoEst
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken

enum TipoOperacaoCorresp {

    MAIOR(new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR), new TipoBlocoEst('BOOLEAN', TipoBloco.BOOLEAN), TipoEstrutura.OPERACAO), TipoBloco.INT),
    MAIOR_IGUAL(new DTOTipoToken(new DTOToken(TokenPreDefinido.MAIOR_IGUAL), new TipoBlocoEst('BOOLEAN', TipoBloco.BOOLEAN), TipoEstrutura.OPERACAO), TipoBloco.INT),
    MENOR(new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR), new TipoBlocoEst('BOOLEAN', TipoBloco.BOOLEAN), TipoEstrutura.OPERACAO), TipoBloco.INT),
    MENOR_IGUAL(new DTOTipoToken(new DTOToken(TokenPreDefinido.MENOR_IGUAL), new TipoBlocoEst('BOOLEAN', TipoBloco.BOOLEAN), TipoEstrutura.OPERACAO), TipoBloco.INT),
    SOMA(new DTOTipoToken(new DTOToken(TokenPreDefinido.SOMA), new TipoBlocoEst('INT', TipoBloco.INT), TipoEstrutura.OPERACAO), TipoBloco.INT),
    SUBTRACAO(new DTOTipoToken(new DTOToken(TokenPreDefinido.SUBTRACAO), new TipoBlocoEst('INT', TipoBloco.INT), TipoEstrutura.OPERACAO), TipoBloco.INT),
    ASTERISTICO(new DTOTipoToken(new DTOToken(TokenPreDefinido.ASTERISTICO), new TipoBlocoEst('INT', TipoBloco.INT), TipoEstrutura.OPERACAO), TipoBloco.INT),
    DIVISAO(new DTOTipoToken(new DTOToken(TokenPreDefinido.DIVISAO), new TipoBlocoEst('INT', TipoBloco.INT), TipoEstrutura.OPERACAO), TipoBloco.INT)

    DTOTipoToken dtoTipoToken
    TipoBloco tipoBloco

    TipoOperacaoCorresp(DTOTipoToken dtoTipoToken, TipoBloco tipoBloco) {
        this.dtoTipoToken = dtoTipoToken
        this.tipoBloco = tipoBloco
    }

    static TipoOperacaoCorresp obtemTipo(List<DTOToken> listaDtoToken) {
        return values().find { it -> it.dtoTipoToken.dtoToken in listaDtoToken }
    }

    static boolean ehTokenOperacao(DTOToken dto) {
        return values().find{it -> it.dtoTipoToken.dtoToken == dto}
    }
}