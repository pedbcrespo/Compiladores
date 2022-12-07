package backEndCompilador

import backEndCompilador.estruturas.EstruturaClasseJson
import backEndCompilador.estruturas.EstruturaFuncaoJson
import backEndCompilador.estruturas.EstruturaJson
import backEndCompilador.estruturas.EstruturaProgramaJson
import backEndCompilador.estruturas.TupInstrs
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Classe
import frontEndCompilador.analizeSintatica.regras.Feature
import frontEndCompilador.analizeSintatica.regras.Programa
import frontEndCompilador.analizeSintatica.regras.RType
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TokenPreDefinido

class GeradorDeCodigo {

    private static EstruturaJson estruturaBase

    static void geraCodigo(NodeToken nodeToken) {

    }

    private EstruturaJson geraEstruturaJson(NodeToken node, EstruturaJson estruturaAnterior = null) {

    }

    private static EstruturaClasseJson geraEstruturaClasse(NodeToken nodeToken) {
        String nome = nodeToken.dtosDaMesmaRegra[0].simb
        String tipo = nodeToken.dtosDaMesmaRegra
                .contains(new DTOToken(TokenPreDefinido.INHERITS)) ?
                nodeToken.dtosDaMesmaRegra[2] : TipoBloco.OBJECT.desc
        return new EstruturaClasseJson(nome, tipo)
    }

    private EstruturaFuncaoJson geraEstruturaFuncao(NodeToken nodeToken) {

    }
}
