package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static List<DTOToken> pilhaBloco = []
    private static List<BlocoToken> listaBlocos = []
    private static TokenPreDefinido tokenChave
    private static TokenPreDefinido tokenContraChave



    static void defineTokenChave(TokenPreDefinido token, TokenPreDefinido contraChave) {
        tokenChave = token
        tokenContraChave = contraChave
    }

    static List<BlocoToken> organizaBlocos(List<DTOToken> listaDto) {
        for (DTOToken dto : listaDto) {
            pilhaBloco.add(0, dto)
            if (TokenPreDefinido.obtemToken(dto.desc) == tokenChave) {
                fechaBloco()
            }
        }
        return listaBlocos
    }


    private void fechaBloco() {
        int pos = 0
        BlocoToken blocoToken
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

    private void removeTrataBlocoDaPilha(int pos, BlocoToken blocoToken) {
        for (int i = 0; i < pos; i++) {
            pilhaBloco.remove(0)
        }
        if (casoBlocoClass()) {
            trataCasoClass(blocoToken)
        }
        trataCabecalhoBloco(blocoToken)
    }

    private boolean casoBlocoClass() {
        for (DTOToken dto : pilhaBloco[0..5]) {
            if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.CLASS) {
                return true
            }
        }
        return false
    }

    private void trataCasoClass(BlocoToken blocoToken) {
        DTOToken anterior = null
        for (DTOToken dto : pilhaBloco) {
            if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.INHERITS &&
                    TokenPreDefinido.obtemToken(anterior.desc) == TokenPreDefinido.IDENTIFICADOR) {
                blocoToken.dtoHeranca = anterior
            }
            else if (TokenPreDefinido.obtemToken(dto.desc) == TokenPreDefinido.IDENTIFICADOR &&
                    TokenPreDefinido.obtemToken(anterior.desc) == TokenPreDefinido.INHERITS) {
                blocoToken.dtoCabeca = dto
                return
            }
            anterior = dto
        }
    }

    private void trataCabecalhoBloco(BlocoToken blocoToken) {

    }
}
