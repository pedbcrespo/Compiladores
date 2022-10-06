package frontEndCompilador.analizeSintatica

import frontEndCompilador.dto.DTOToken

class AnaliseSintatica {
        static void analisaTokens(List<DTOToken> dtoTokens) {
            AnaliseSintaticaService.analisa(dtoTokens)
        }
}