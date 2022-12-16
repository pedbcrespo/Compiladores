import backEndCompilador.geradorJava.TradutorCodigoJson
import backEndCompilador.geradorJson.GeradorDeCodigo
import frontEndCompilador.analiseSemantica.AnaliseSemantica
import frontEndCompilador.analizeLexica.AnaliseLexica
import frontEndCompilador.analizeSintatica.AnaliseSintatica
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.dto.DTOToken
import groovy.util.logging.Log

class CompileCool {
    static void main(String[] args) {
        List<DTOToken> listaTokens = AnaliseLexica.gerarTokens(args[0])
        Log.println('Analise Lexica concluida')
        NodeToken arvoreGerada = AnaliseSintatica.analisaTokens(listaTokens)
        Log.println('Analise Sintatica concluida')
        Map<String, Object> mapDadosMapeados = AnaliseSemantica.analisaArvore(arvoreGerada)
        Log.println('Analise Semantica concluida')
        Log.println('Gerando codigo para o bril')
        GeradorDeCodigo.geraCodigo(mapDadosMapeados, arvoreGerada)
        Log.println('Codigo cool gerado')
        Log.println('Traduzindo para codigo Java')
        TradutorCodigoJson.traduzParaCodigoJava()
    }
}
