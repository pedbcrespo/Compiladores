package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco

class TipoBlocoEst {
    final String id
    TipoBloco tipoBloco

    TipoBlocoEst(String id, TipoBloco tipoBloco) {
        this.id = id
        this.tipoBloco = tipoBloco
    }

}
