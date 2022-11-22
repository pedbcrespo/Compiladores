import frontEndCompilador.analiseSemantica.AnaliseSemantica
import frontEndCompilador.analizeLexica.AnaliseLexica
import frontEndCompilador.analizeSintatica.AnaliseSintatica
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.dto.DTOToken

class CompileCool {
    static void main(String[] args) {
        List<DTOToken> listaTokens = AnaliseLexica.gerarTokens(args[0])
        for (DTOToken token : listaTokens) {
            println(token)
        }
        NodeToken arvoreGerada = AnaliseSintatica.analisaTokens(listaTokens)
        AnaliseSemantica.analisaArvore(arvoreGerada)
    }
}
