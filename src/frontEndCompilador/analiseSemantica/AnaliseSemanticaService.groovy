package frontEndCompilador.analiseSemantica

import frontEndCompilador.dto.DTOTipoDto
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.dto.Dto
import frontEndCompilador.enums.TokenPreDefinido

class AnaliseSemanticaService {

    private static TokenPreDefinido tokenChave = TokenPreDefinido.FECHA_CHAVE
    private static TokenPreDefinido tokenContraChave = TokenPreDefinido.ABRE_CHAVE
    private static List<DTOToken> pilhaBloco = []
    private static List<BlocoToken> listaBlocos = []
    private static List<TokenPreDefinido> listaTokensCabecalho = [
            TokenPreDefinido.IN,
            TokenPreDefinido.THEN,
            TokenPreDefinido.ELSE,
            TokenPreDefinido.LOOP
    ]
    private static int nivelAtual

    static List<BlocoToken> organizaBlocos(List<DTOToken> listaDto) {
        nivelAtual = 0
        for (DTOToken dto : listaDto) {
            pilhaBloco.add(0, dto)
            nivelAtual += obtemTokenPreDefinido(dto) == tokenContraChave ? 1 : 0
            if (obtemTokenPreDefinido(dto) == tokenChave) {
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
        nivelAtual -= 1
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
            if (obtemTokenPreDefinido(dto) == TokenPreDefinido.INHERITS &&
                    obtemTokenPreDefinido(anterior) == TokenPreDefinido.IDENTIFICADOR) {
                blocoToken.dtoHeranca = anterior
            } else if (obtemTokenPreDefinido(dto) == TokenPreDefinido.IDENTIFICADOR &&
                    obtemTokenPreDefinido(anterior) == TokenPreDefinido.INHERITS) {
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

    private static List<DTOTipoDto> defineTipoDtoBloco(List<DTOToken> lista) {
        DTOToken anterior = null
        DTOToken antAnterior = null
        List<DTOTipoDto> listaDtoTokenComTipo = []
        for (DTOToken atual : lista) {
            List<DTOToken> lst = [antAnterior, anterior, atual]
            DTOTipoDto dtoTipo = trataCasoLista(lst)
            listaDtoTokenComTipo.add(dtoTipo)
        }

        return listaDtoTokenComTipo
    }


    private static TokenPreDefinido obtemTokenPreDefinido(DTOToken dto) {
        return TokenPreDefinido.obtemToken(dto.desc)
    }

    private static boolean validaSimb(DTOToken dtoToken) {
        String primeiroCaractere = dtoToken.simb[0]
        return primeiroCaractere == dtoToken.simb[0].toUpperCase()
    }

    private static DTOTipoDto trataCasoLista(List<DTOToken> dtoTokens) {

        List<Closure<DTOTipoDto>> casos = [
                { List<DTOToken> lista ->
                    obtemTokenPreDefinido(lista[0]) == TokenPreDefinido.IDENTIFICADOR &&
                            obtemTokenPreDefinido(lista[1]) == TokenPreDefinido.DOIS_PONTOS &&
                            obtemTokenPreDefinido(lista[2]) == TokenPreDefinido.IDENTIFICADOR &&
                            validaSimb(lista[2]) ?
                            new DTOTipoDto(lista[0], lista[2].simb) : null
                }
        ]
    }
}
