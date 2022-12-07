package backEndCompilador

import backEndCompilador.estruturas.EstruturaClasseJson
import backEndCompilador.estruturas.EstruturaFuncaoJson
import backEndCompilador.estruturas.EstruturaJson
import backEndCompilador.estruturas.EstruturaProgramaJson
import backEndCompilador.estruturas.TupInstrs
import frontEndCompilador.analizeSintatica.NodeToken
import frontEndCompilador.analizeSintatica.RegraEstrutura
import frontEndCompilador.analizeSintatica.regras.Classe
import frontEndCompilador.analizeSintatica.regras.Expressao
import frontEndCompilador.analizeSintatica.regras.Feature
import frontEndCompilador.analizeSintatica.regras.RType
import frontEndCompilador.dto.DTOToken
import frontEndCompilador.enums.TipoBloco
import frontEndCompilador.enums.TokenPreDefinido

class GeradorDeCodigoService {

    private EstruturaProgramaJson estruturaPrograma
    private List<EstruturaClasseJson> estrutrasClasse = []

    void preencheEstruturaClasse(NodeToken node) {
        RegraEstrutura regra = node.regraNode
        if (regra.getClass() == Classe) {
            String nome = node.dtosDaMesmaRegra[0]
            String tipo = node.dtosDaMesmaRegra.contains(new DTOToken(TokenPreDefinido.INHERITS)) ?
                    node.dtosDaMesmaRegra[2] : TipoBloco.OBJECT.desc
            EstruturaClasseJson estruturaClasseJson = new EstruturaClasseJson(nome, tipo)
            estruturaClasseJson.methods = geraListaMetodos(node)
            estrutrasClasse.add(estruturaClasseJson)
        } else {
            for (NodeToken prox : node.proximosNodes) {
                preencheEstruturaClasse(prox)
            }
        }
    }

    private List<EstruturaFuncaoJson> geraListaMetodos(NodeToken node) {
        RegraEstrutura regra = node.regraNode
        List<EstruturaFuncaoJson> lista = []
        if (regra.getClass() == Feature) {
            String nome = regra.dtoCabeca.simb
            String tipo = retTipoFeature(node, nome)
            EstruturaFuncaoJson estruturaFuncao = new EstruturaFuncaoJson(nome, tipo)
            estruturaFuncao.instrs = geraListaInstrs(node)
            lista.add(estruturaFuncao)
        } else {
            for (NodeToken prox : node.proximosNodes) {
                lista += geraListaMetodos(prox)
            }
        }
        return lista
    }

    private String retTipoFeature(NodeToken node, String nomeFuncao) {
        RegraEstrutura regra = node.regraNode
        String tipo = null
        if (regra.getClass() == RType && regra.dtoCabeca.simb == nomeFuncao) {
            tipo = node.dtosDaMesmaRegra[0].simb
        } else {
            for (NodeToken prox : node.proximosNodes) {
                tipo = retTipoFeature(prox, nomeFuncao)
            }
        }
        return tipo
    }

    private List<TupInstrs> geraListaInstrs(NodeToken node) {
        RegraEstrutura regra = node.regraNode
        if(regra.getClass() == Expressao) {

        }
    }
}
