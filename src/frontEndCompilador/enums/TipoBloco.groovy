package frontEndCompilador.enums

import frontEndCompilador.analiseSemantica.TipoBlocoEst
import frontEndCompilador.dto.DTOToken

import java.nio.file.attribute.UserDefinedFileAttributeView

enum TipoBloco {
    INT({ String simb -> simb.isInteger() }, 'Int', 'int'),
    BOOLEAN({ String simb -> simb in ['True', 'False'] }, 'Bool', 'bool'),
    STRING({ String simb -> simb[0] == '"' && simb[simb.length() - 1] == '"' }, 'String', 'str'),
    OBJECT({ String simb -> false }, 'Object', 'object')

    Closure<Boolean> teste
    String desc
    String tipo

    TipoBloco(Closure<Boolean> teste, String desc, String tipo) {
        this.teste = teste
        this.desc = desc
        this.tipo = tipo
    }

    static TipoBloco obtemTipo(DTOToken dtoToken) {
        String simb = dtoToken.simb
        TipoBloco tipoEncontrado = values().find { it -> it.teste(simb) || it.desc == simb }
        return tipoEncontrado
    }
}