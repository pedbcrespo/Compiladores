package frontEndCompilador.enums

import frontEndCompilador.dto.DTOToken

enum TipoBloco {
    INT('Int'),
    BOOLEAN('Bool'),
    STRING('String'),
    OBJECT('Object'),
    USER_OBJECT('')

    String desc

    TipoBloco(String desc) {
        this.desc = desc
    }

    static TipoBloco obtemTipo(DTOToken dtoToken) {
        String simb = dtoToken.simb
        TipoBloco tipoEncontrado = values().find { it -> it.desc == simb }
        return tipoEncontrado ?: tipoPersonalizado(simb)
    }

    private static TipoBloco tipoPersonalizado(String simb) {
        TipoBloco tipo = USER_OBJECT
        tipo.desc = simb
        return tipo
    }
}