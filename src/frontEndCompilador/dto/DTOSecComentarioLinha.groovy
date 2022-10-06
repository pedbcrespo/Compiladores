package frontEndCompilador.dto

import frontEndCompilador.enums.TokenPreDefinido

class DTOSecComentarioLinha extends DTOSecComentario {
    DTOSecComentarioLinha() {
        super(TokenPreDefinido.COMENTARIO_LINHA.simb,
                (Closure<Boolean>) { String simb -> simb.contains(TokenPreDefinido.COMENTARIO_LINHA.simb) },
                (Closure<Boolean>) { String simb -> ['\n', ''].contains(simb[simb.length() - 1]) })
    }
}
