package backEndCompilador

import backEndCompilador.estruturas.EstruturaClasseJson
import backEndCompilador.estruturas.EstruturaFuncaoJson
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Classe
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken

class GeradorDeCodigo {
    private static Map<String, Object> arvoreMapeada
    private static GeradorDeCodigoService geradorDeCodigoService
    private static List<EstruturaFuncaoJson> listaEstruturaFeature
    private static List<EstruturaClasseJson> listaEstruturaClasse

    static void geraCodigo(Map<String, Object> mapDadosMapeados, NodeToken arvore) {
        geradorDeCodigoService = new GeradorDeCodigoService(arvore, mapDadosMapeados)
        arvoreMapeada = mapDadosMapeados
        listaEstruturaFeature = montaEstruturasFeature()
        listaEstruturaClasse = montaEstruturasClasse()
    }

    private static List<EstruturaClasseJson> montaEstruturasClasse() {
        List<EstruturaClasseJson> estruturaClasseJsonList = []
        List<DTOTipoToken> listaClasses = arvoreMapeada["listaClasses"] as List<DTOTipoToken>
        for (DTOTipoToken dto : listaClasses) {
            String name = dto.dtoToken.simb
            String type = dto.tipoOperacao.id
            List<EstruturaFuncaoJson> methods = buscaFeaturesClasse(dto.dtoToken)
            estruturaClasseJsonList.add(new EstruturaClasseJson(name, type, methods))
        }
        return estruturaClasseJsonList
    }

    private static List<EstruturaFuncaoJson> montaEstruturasFeature() {
        List<EstruturaFuncaoJson> estruturaFuncaoJsonList = []
        List<DTOTipoToken> listaFeatures = arvoreMapeada["listaFeatures"] as List<DTOTipoToken>
        for (DTOTipoToken dto : listaFeatures) {
            String name = dto.dtoToken.simb
            String type = dto.tipoOperacao.id
            List<Map<String, Object>> instrs = geradorDeCodigoService.buscaInstrucoes(dto)
            estruturaFuncaoJsonList.add(new EstruturaFuncaoJson(name, type, instrs))
        }
        return estruturaFuncaoJsonList
    }

    private static List<EstruturaFuncaoJson> buscaFeaturesClasse(DTOToken dtoToken) {

    }
}
