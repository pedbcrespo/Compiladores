package frontEndCompilador.dto

class DTOSecComentario extends DTOSecundario {
    DTOSecComentario(String tokenMarcador,
                     Closure<Boolean> condicaoInicial,
                     Closure<Boolean> condicaoFinal) {
        this.condicaoInicial = condicaoInicial
        this.condicaoFinal = condicaoFinal
        this.tokenMarcador = tokenMarcador
    }
}
