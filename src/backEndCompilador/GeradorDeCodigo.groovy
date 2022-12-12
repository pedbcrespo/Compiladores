package backEndCompilador

import backEndCompilador.estruturas.EstruturaClasseJson
import backEndCompilador.estruturas.EstruturaFuncaoJson
import backEndCompilador.estruturas.EstruturaProgramaJson
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.dto.DTOTipoToken
import frontEndCompilador.dto.DTOToken
import groovy.json.JsonOutput

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
        EstruturaProgramaJson estruturaProgramaJson = new EstruturaProgramaJson()
        estruturaProgramaJson.classes = listaEstruturaClasse
        String arvoreJson = JsonOutput.toJson(estruturaProgramaJson.geraInfoParaJson())
        File file = new File("arvore.json")
        file.write(JsonOutput.prettyPrint(arvoreJson))
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
            List<Map<String, Object>> args = geradorDeCodigoService.buscaParametros(dto)
            List<Map<String, Object>> instrs = geradorDeCodigoService.buscaInstrucoes(dto)
            estruturaFuncaoJsonList.add(new EstruturaFuncaoJson(name, type, instrs, args))
        }
        return estruturaFuncaoJsonList
    }

    private static List<EstruturaFuncaoJson> buscaFeaturesClasse(DTOToken dtoClasse) {
        List<DTOTipoToken> listaFeatures = arvoreMapeada["listaFeatures"] as List<DTOTipoToken>
        List<DTOTipoToken> featuresDaClasse = listaFeatures.findAll { it ->
            it.dtoClasseHeranca == dtoClasse
        }
        List<String> nomeFeatures = featuresDaClasse*.dtoToken.simb
        return listaEstruturaFeature.findAll { it -> it.name in nomeFeatures }
    }
}
