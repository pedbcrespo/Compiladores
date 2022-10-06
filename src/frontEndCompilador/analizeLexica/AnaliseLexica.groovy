package frontEndCompilador.analizeLexica


import frontEndCompilador.dto.DTOToken
import frontEndCompilador.dto.DTOSecundario

import frontEndCompilador.enums.TokenPreDefinido
import frontEndCompilador.services.AnaliseLexicaService

class AnaliseLexica {
    private static final List<String> conjuntoEspacos = [' ', '']

    static List<DTOToken> gerarTokens(final String caminhoArquivo) {
        if (caminhoArquivo == '') {
            return []
        }
        File doc = new File(caminhoArquivo)
        BufferedReader obj = new BufferedReader(new FileReader(doc))
        List<String> listaPalavras = new ArrayList()
        String strng
        while ((strng = obj.readLine()) != null) {
            listaPalavras.add(strng)
        }
        List<DTOToken> tokensGerados = quebrandoStringEmTokens(listaPalavras.join('\n'))
        return tratamentoSecundarioToken(tokensGerados)
    }

    private static List<DTOToken> quebrandoStringEmTokens(final String strng) {
        if (strng == '') {
            return []
        }
        String bufferCaracter = ''
        List<DTOToken> listaTokens = (List<DTOToken>) []
        int pos = 0
        while (pos < strng.length()) {
            String caracter = strng[pos]
            bufferCaracter += caracter
            pos += 1
            if (conjuntoEspacos.contains(caracter) || pos == strng.length()) {
                if (!AnaliseLexicaService.verificaBufferEspacos(bufferCaracter)) {
                    listaTokens.addAll(tratamentoStringToken(bufferCaracter))
                }
                bufferCaracter = ''
            }
        }
        return listaTokens
    }

    private static List<DTOToken> tratamentoStringToken(final String bufferCaracter) {
        List<String> listaStringTokens = verificarSequenciaInterrompida(bufferCaracter)
        return (List<DTOToken>) gerarTokensListaString(listaStringTokens)
    }

    private static List<String> verificarSequenciaInterrompida(final String sequencia) {
        String bufferCaracteresAnteriores = ''
        List<String> sequenciasCaracteres = (List<String>) []
        for (String caracter : sequencia) {
            if (!verificaMesmoTipo(caracter, bufferCaracteresAnteriores)) {
                sequenciasCaracteres += bufferCaracteresAnteriores != '' ? bufferCaracteresAnteriores : []
                bufferCaracteresAnteriores = ''
            }
            bufferCaracteresAnteriores += caracter
        }
        sequenciasCaracteres += bufferCaracteresAnteriores != '' ? bufferCaracteresAnteriores : []
        return (List<String>) sequenciasCaracteres
    }

    private static Boolean verificaMesmoTipo(final String caracter, final String bufferCaracteresAnteriores) {
        if (bufferCaracteresAnteriores == '' || conjuntoEspacos.contains(caracter)) {
            return false
        }
        String ultimoCaracterDaSequencia = bufferCaracteresAnteriores[bufferCaracteresAnteriores.length() - 1]
        Boolean caracterEhSimboloEspecial = AnaliseLexicaService.verificaCaracterEspecial(caracter)
        Boolean ultimoCaracterDaSequenciaEhSimboloEspecial = AnaliseLexicaService.verificaCaracterEspecial(ultimoCaracterDaSequencia)
        return caracterEhSimboloEspecial == ultimoCaracterDaSequenciaEhSimboloEspecial
    }

    private static List<DTOToken> gerarTokensListaString(final List<String> listaSequencias) {
        List<DTOToken> listaTratamentoPrimario = []
        List<List<DTOToken>> listaDeLista = listaSequencias.collect(({ String sequencia -> tratamentoPrimarioToken(sequencia) } as Closure<DTOToken>)) as List<List<DTOToken>>
        for (List<DTOToken> lista : listaDeLista) {
            listaTratamentoPrimario.addAll(lista)
        }
        return listaTratamentoPrimario
    }

    private static List<DTOToken> tratamentoPrimarioToken(final String sequencia) {
        List<DTOToken> listaTokens = []
        Boolean sequenciaLetra = verificaSequenciaLetra(sequencia)
        if (sequenciaLetra) {
            TokenPreDefinido token = TokenPreDefinido.obtemToken(sequencia)
            token = !token? TokenPreDefinido.tokenPersonalisado(sequencia, TokenPreDefinido.IDENTIFICADOR):token
            listaTokens += new DTOToken(token)
        } else {
            List<DTOToken> listaDtoToken = analizaStringRetornaDTOToken(sequencia)
            if (listaDtoToken) {
                listaTokens.addAll(listaDtoToken)
            }
        }
        return listaTokens
    }

    private static List<DTOToken> analizaStringRetornaDTOToken(final String buffer) {
        if (AnaliseLexicaService.verificaBufferEspacos(buffer)) {
            return []
        }
        String bufferAnalise = buffer
        String caracterAnterior
        TokenPreDefinido tokenBufferAnalise = TokenPreDefinido.obtemToken(bufferAnalise)
        TokenPreDefinido tokenCaracterAnterior
        List<DTOToken> listaDTO = [] + (tokenBufferAnalise ? new DTOToken(tokenBufferAnalise) : []) as List<DTOToken>
        if (tokenBufferAnalise) {
            return listaDTO
        }
        int qtdPos = 1
        while (bufferAnalise.length() > 1) {
            caracterAnterior = bufferAnalise[0]
            bufferAnalise = bufferAnalise.length() > 1 ? bufferAnalise.substring((qtdPos > 0 ? qtdPos : 1)) : bufferAnalise
            tokenBufferAnalise = TokenPreDefinido.obtemToken(bufferAnalise)
            tokenCaracterAnterior = TokenPreDefinido.obtemToken(caracterAnterior)
            if (tokenCaracterAnterior) {
                listaDTO.add(new DTOToken(tokenCaracterAnterior))
            }
            if (tokenBufferAnalise) {
                listaDTO.add(new DTOToken(tokenBufferAnalise))
            }
            qtdPos = (tokenBufferAnalise ? tokenBufferAnalise.simb.length() : 0) +
                    (tokenCaracterAnterior ? tokenCaracterAnterior.simb.length() : 0)
        }
        return listaDTO
    }

    private static List<DTOToken> tratamentoSecundarioToken(List<DTOToken> listaTratamentoPrimario) {
        if (!listaTratamentoPrimario) {
            return []
        }
        List<DTOSecundario> listaDTOTratamentoSecundario = []
        DTOSecundario dtoSecundario = null
        List<DTOToken> listaDTOTratada = (List<DTOToken>) []
        DTOToken dtoToken
        int pos = 0
        while (pos < listaTratamentoPrimario.size()) {
            dtoToken = listaTratamentoPrimario[pos]


            if (!dtoSecundario) {
                dtoSecundario = AnaliseLexicaService.instanciaDTOSecundario(dtoToken.simb)
            }
            trataCasoToken(dtoToken.simb, pos, dtoSecundario, listaDTOTratamentoSecundario)
            dtoSecundario = !dtoSecundario || (!dtoSecundario.flagAdicionarLista && dtoSecundario.posFinalLista) ? null : dtoSecundario
            listaDTOTratada += dtoToken
            pos += 1
        }
        pos = listaDTOTratamentoSecundario.size()-1
        while (pos>=0) {
            DTOSecundario dto = listaDTOTratamentoSecundario.get(pos)
            listaDTOTratada = ajustarListaTokens(listaDTOTratada, dto)
            pos-=1
        }

        listaDTOTratada = AnaliseLexicaService.limpaLista(listaDTOTratada)
        return listaDTOTratada
    }

    private static List<DTOToken> ajustarListaTokens(List<DTOToken> listaTratada, DTOSecundario dto) {
        if (!dto.listaToken) {
            return listaTratada
        }
        String simb = dto.listaToken.join('').replaceFirst('\n', '')
        for (int pos = dto.posInicioLista; pos <= dto.posFinalLista; pos++) [
                listaTratada.remove(dto.posInicioLista)
        ]
        listaTratada.add(dto.posInicioLista, new DTOToken(2, simb, 'TEXTO'))
        return listaTratada
    }

    private static Boolean verificaSequenciaLetra(final String sequencia) {
        Boolean resultado = true
        for (String caracter : sequencia) {
            resultado = resultado && !AnaliseLexicaService.verificaCaracterEspecial(caracter)
        }
        return resultado
    }

    private static void trataCasoToken(String simb, int pos, DTOSecundario dtoSec, List<DTOSecundario> listaDTOTratamentoSecundario) {
        if(!dtoSec || !dtoSec.verificaToken(simb)){
            return;
        }
        if (dtoSec.condicaoInicial(simb)) {
            dtoSec.flagAdicionarLista = true
            dtoSec.posInicioLista = pos
        } else if (dtoSec.condicaoFinal(simb)) {
            dtoSec.flagAdicionarLista = false
            dtoSec.posFinalLista = pos
        }
        dtoSec.listaToken += simb
        if (!dtoSec.flagAdicionarLista && dtoSec.posFinalLista) {
            listaDTOTratamentoSecundario.add(dtoSec)
        }
    }
}




