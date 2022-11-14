package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static TokenPreDefinido tokenChave = TokenPreDefinido.ABRE_CHAVE
    private static TokenPreDefinido tokenContraChave = TokenPreDefinido.FECHA_CHAVE
    private static List<DTOToken> pilhaBloco = []
    private static List<BlocoToken> listaBlocos = []
    private static List<TokenPreDefinido> listaTokensCabecalho = [
            TokenPreDefinido.IN,
            TokenPreDefinido.THEN,
            TokenPreDefinido.ELSE,
            TokenPreDefinido.LOOP
    ]

    static List<BlocoToken> organizaBlocos(List<DTOToken> listaDto) {
        for (DTOToken dto : listaDto) {
            pilhaBloco.add(0, dto)
            if (TokenPreDefinido.obtemToken(dto.desc) == tokenChave) {
                fechaBloco()
            }
        }
        return listaBlocos
    }

    static void analizaBloco(BlocoToken bloco) {

    }

    private static void fechaBloco() {
        int pos = 0
        BlocoToken blocoToken = null
        for (DTOToken dto : pilhaBloco) {
            if (TokenPreDefinido.obtemToken(dto.desc) == tokenContraChave) {
                blocoToken = new BlocoToken(pilhaBloco[0..pos])
                listaBlocos.add(blocoToken)
                break
            }
            pos += 1
        }
        removeTrataBlocoDaPilha(pos, blocoToken)
    }

    private static void removeTrataBlocoDaPilha(int pos, BlocoToken blocoToken) {
        if (!blocoToken) {
            return
        }
        for (int i = 0; i < pos; i++) {
            pilhaBloco.remove(0)
        }
        DTOToken dtoMarcacao = casoBlocoClass() ? trataCasoClass(blocoToken) : trataCabecalhoBloco(blocoToken)
        pilhaBloco.add(dtoMarcacao)
    }

    private static boolean casoBlocoClass() {
        for (DTOToken dto : pilhaBloco[0..5]) {
            if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.CLASS) {
                return true
            }
        }
        return false
    }

    private static DTOToken trataCasoClass(BlocoToken blocoToken) {
        DTOToken anterior = null
        String marcador = ''
        for (DTOToken dto : pilhaBloco) {
            if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.INHERITS &&
                    TokenPreDefinido.obtemToken(anterior.desc) == TokenPreDefinido.IDENTIFICADOR) {
                blocoToken.dtoHeranca = anterior
            } else if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.IDENTIFICADOR &&
                    TokenPreDefinido.obtemToken(anterior.desc) == TokenPreDefinido.INHERITS) {
                blocoToken.dtoCabeca = dto
                marcador = identificadorDto(dto)
                break
            }
            anterior = dto
        }
        return new DTOToken(TokenPreDefinido.tokenPersonalisado(marcador, TokenPreDefinido.BLOCO_SEMANTICO))
    }

    private static DTOToken trataCabecalhoBloco(BlocoToken blocoToken) {
        String marcador = ''
        for (DTOToken dto : pilhaBloco) {
            if (listaTokensCabecalho.contains(TokenPreDefinido.obtemToken(dto.desc))) {
                blocoToken.dtoCabeca = dto
                blocoToken.dtoHeranca = dto
                marcador = identificadorDto(dto)
                break
            }
        }
        return new DTOToken(TokenPreDefinido.tokenPersonalisado(marcador, TokenPreDefinido.BLOCO_SEMANTICO))
    }

    private static String identificadorDto(DTOToken dto) {
        return "${dto.desc}#${dto.simb}"
    }
}
