package frontEndCompilador.enums

import frontEndCompilador.analiseSemantica.TipoBlocoEst
import frontEndCompilador.dto.DTOToken

import java.nio.file.attribute.UserDefinedFileAttributeView

enum TipoBloco {
    INT({ String simb -> simb.isInteger() }, 'Int'),
    BOOLEAN({ String simb -> simb in ['True', 'False'] }, 'Bool'),
    STRING({ String simb -> simb[0] == '"' && simb[simb.length() - 1] == '"' }, 'String'),
    OBJECT({ String simb -> false }, 'Object')
//    USER_OBJECT({ String simb -> false })

    Closure<Boolean> teste
    String desc

    TipoBloco(Closure<Boolean> teste, String desc) {
        this.teste = teste
        this.desc = desc
    }

    static TipoBloco obtemTipo(DTOToken dtoToken) {
        String simb = dtoToken.simb
        TipoBloco tipoEncontrado = values().find { it -> it.teste(simb) || it.desc == simb }
        return tipoEncontrado
    }
}