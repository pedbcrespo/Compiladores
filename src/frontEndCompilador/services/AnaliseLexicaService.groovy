package frontEndCompilador.services

import frontEndCompilador.dto.DTOSecComentarioBloco
import frontEndCompilador.dto.DTOSecComentarioLinha
import frontEndCompilador.dto.DTOSecTexto
import frontEndCompilador.dto.DTOSecundario
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseLexicaService {
    static List<DTOToken> limpaLista(List<DTOToken> listaDTO) {
        listaDTO.removeAll({ DTOToken dto ->
            simbEspacos(dto)
        }) as List<DTOToken>
        return listaDTO
    }

    static Boolean simbEspacos(DTOToken dtoToken) {
        Boolean resultado = true
        dtoToken.simb.findResult { String caracter ->
            resultado = resultado && ([' ', '', '\n', '\t']).contains(caracter)
            return caracter
        }
        return resultado
    }

    static Boolean verificaCaracterEspecial(final String caracter) {
        return (TokenPreDefinido.values()*.simb + ['<', '>', '/', '^', '?', '%']).contains(caracter)
    }

    static Boolean verificaBufferEspacos(String buffer) {
        int contEspacos = 0
        for (String caracter : buffer) {
            contEspacos += [' ', ''].contains(caracter) ? 1 : 0
        }
        return contEspacos == buffer.length()
    }

    static DTOSecundario instanciaDTOSecundario(String tokenSimb) {
        if(tokenSimb == TokenPreDefinido.ABRE_COMENTARIO_BLOCO.simb){
            return new DTOSecComentarioBloco()
        }
        else if(tokenSimb == TokenPreDefinido.COMENTARIO_LINHA.simb){
            return new DTOSecComentarioLinha()
        }
        else if(tokenSimb == TokenPreDefinido.ASPA_DUPLA.simb) {
            return new DTOSecTexto()
        }
        return null
    }

    static TokenPreDefinido instanciaIdentificador(String simb) {
        return TokenPreDefinido.tokenPersonalisado(simb, TokenPreDefinido.IDENTIFICADOR)
    }
}
