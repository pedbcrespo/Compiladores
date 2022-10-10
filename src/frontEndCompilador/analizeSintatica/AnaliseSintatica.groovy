package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken
import frontEndCompilador.services.AnaliseSintaticaService

class AnaliseSintatica {
        static void analisaTokens(List<DTOToken> dtoTokens) {
            AnaliseSintaticaService.analisa(dtoTokens)
        }
}