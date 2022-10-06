package frontEndCompilador.dto


import frontEndCompilador.enums.TokenPreDefinido

class DTOSecTexto extends DTOSecundario {
    DTOSecTexto() {
        this.condicaoInicial = (Closure<Boolean>) { String simb -> simb.contains(TokenPreDefinido.ASPA_DUPLA.simb) && !this.flagAdicionarLista }
        this.condicaoFinal = (Closure<Boolean>) { String simb -> simb == TokenPreDefinido.ASPA_DUPLA.simb }
        this.desc = 'TEXTO'
        this.tokenMarcador = Token.ASPA_DUPLA.simb
    }

}
