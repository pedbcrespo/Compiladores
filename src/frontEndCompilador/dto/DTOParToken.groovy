package frontEndCompilador.dto

import frontEndCompilador.enums.TokenPreDefinido

class DTOParToken {
    TokenPreDefinido tokenChaveAbre
    TokenPreDefinido tokenChaveFecha

    DTOParToken(TokenPreDefinido tokenChaveAbre, TokenPreDefinido tokenChaveFecha) {
        this.tokenChaveAbre = tokenChaveAbre
        this.tokenChaveFecha = tokenChaveFecha
    }

    Boolean correspondeChaveFecha(TokenPreDefinido token) {
        return token == tokenChaveFecha
    }

    TokenPreDefinido obtemChaveFecha(TokenPreDefinido token) {
        return token == tokenChaveAbre ? tokenChaveFecha : null
    }
}
