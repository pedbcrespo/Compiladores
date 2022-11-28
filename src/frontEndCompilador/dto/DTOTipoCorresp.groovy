package frontEndCompilador.dto

import frontEndCompilador.enums.TipoBloco

class DTOTipoCorresp {
    DTOTipoToken dtoTipo
    TipoBloco tipoEsperado

    DTOTipoCorresp(DTOTipoToken dtoTipo, TipoBloco tipoEsperado) {
        this.dtoTipo = dtoTipo
        this.tipoEsperado = tipoEsperado
    }
}
