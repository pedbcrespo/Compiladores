package frontEndCompilador.dto

import frontEndCompilador.analiseSemantica.TipoBlocoEst
import frontEndCompilador.enums.TipoBloco

class DTOTipoCorresp {
    DTOTipoToken dtoTipo
    TipoBlocoEst tipoEsperado

    DTOTipoCorresp(DTOTipoToken dtoTipo, TipoBlocoEst tipoEsperado) {
        this.dtoTipo = dtoTipo
        this.tipoEsperado = tipoEsperado
    }
}
