package frontEndCompilador.enums

import frontEndCompilador.dto.DTOToken

enum ConvTipo {

    ADD(TokenPreDefinido.SOMA, 'add'),
    SUB(TokenPreDefinido.SUBTRACAO, 'sub'),
    MUL(TokenPreDefinido.ASTERISTICO, 'mul'),
    DIV(TokenPreDefinido.DIVISAO, 'mul'),
    CONST(TokenPreDefinido.IDENTIFICADOR, 'const'),
    CALL(null, 'call')

    TokenPreDefinido token
    String tipo

    ConvTipo(TokenPreDefinido token, String tipo) {
        this.token = token
        this.tipo = tipo
    }

    static ConvTipo obtem(DTOToken dtoToken) {
        return values().find { it ->
            TokenPreDefinido.obtemToken(dtoToken.desc) == it.token
        }?:CONST
    }
}