package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.services.AnaliseSintaticaService

class AnaliseSintatica {
    static NodeToken analisaTokens(List<DTOToken> dtoTokens) {
        NodeToken arvoreResultado = AnaliseSintaticaService.analisa(dtoTokens)
        return arvoreResultado
    }
}