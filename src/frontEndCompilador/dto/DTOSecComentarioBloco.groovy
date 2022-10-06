package frontEndCompilador.dto

import frontEndCompilador.enums.TokenPreDefinido

class DTOSecComentarioBloco extends DTOSecComentario {
    DTOSecComentarioBloco() {
        super(TokenPreDefinido.ABRE_COMENTARIO_BLOCO.simb,
                (Closure<Boolean>) { String simb -> simb.contains(TokenPreDefinido.ABRE_COMENTARIO_BLOCO.simb) },
                (Closure<Boolean>) { String simb -> simb == TokenPreDefinido.FECHA_PARENTESES.simb })
    }
}
