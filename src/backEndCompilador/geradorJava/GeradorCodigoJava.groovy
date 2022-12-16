package backEndCompilador.geradorJava

import groovy.json.JsonSlurper

class GeradorCodigoJava {
    private static Map mapJson
    private static GeradorCodigoJavaService geradorCodigoJavaService

    static void traduzParaCodigoJava() {


        File arquivoCodigoGerado = new File('D:\\Users\\Pedro\\Documents\\Programacao\\Groovy\\untitled\\codigoCool.json')
        BufferedReader obj = new BufferedReader(new FileReader(arquivoCodigoGerado))
        List<String> listaPalavras = []
        String strng
        while ((strng = obj.readLine()) != null) {
            listaPalavras.add(strng)
        }
        def jsonSlurper = new JsonSlurper()
        mapJson = jsonSlurper.parseText(listaPalavras.join(' ')) as Map
        geradorCodigoJavaService = new GeradorCodigoJavaService(mapJson)
        String codigoTraduzido = traduzClasses()
        File fileJava = new File('codigoTraduzidoJava.java')
        fileJava.write(codigoTraduzido)
        String dir = System.getProperty("user.dir")
        executaCompilacao(fileJava, dir)
    }

    private static String traduzClasses() {
        List<Map<String, Object>> listaClasses = mapJson['class'] as List<Map<String, Object>>
        List<String> listaClasseTxt = []
        for (Map<String, Object> classe : listaClasses) {
            String classeTxt = traduzDadosClasse(classe)
            listaClasseTxt.add(classeTxt)
        }
        return listaClasseTxt.join('\n')
    }

    private static String traduzDadosClasse(Map<String, Object> classe) {
        String nome = classe['name']
        String type = classe['type']
        String txt = "class ${nome}"
        if (type != 'Object') {
            txt += " extends ${type}"
        }
        txt += "{ ${traduzAtributos(classe)}\n${traduzMetodos(classe)} }"
        return txt
    }

    private static String traduzAtributos(Map<String, Object> classe) {
        List<Map<String, Object>> atributos = classe['attributes'] as List<Map<String, Object>>
        if (!atributos) {
            return ''
        }
        List<String> lstTxt = []
        for (Map<String, Object> atributo : atributos) {
            String name = atributo['name']
            String type = Equivalente.obtem(atributo['type'] as String)
            geradorCodigoJavaService.salvaVariavel(name, type)
            String instancia = ehTipoPrimitivo(type)? "${type} ${name};" : "${type} ${name} = new ${type}();"
            lstTxt.add(instancia)
        }
        return lstTxt.join('\n')
    }

    private static String traduzMetodos(Map<String, Object> classe) {
        List<Map<String, Object>> metodos = classe['methods'] as List<Map<String, Object>>
        if (!metodos) {
            return ''
        }
        List<String> lstTxt = []
        for (Map<String, Object> metodo : metodos) {
            String name = metodo['name']
            String type = Equivalente.obtem(metodo['type'] as String)
            String args = geradorCodigoJavaService.trataArgumentos(metodo)
            String instrs = geradorCodigoJavaService.trataInstrucoes(metodo)
            lstTxt.add("${type} ${name} (${args}) { ${instrs} }")
        }
        return lstTxt.join('\n')
    }

    private static void executaCompilacao(File arquivoJava, String dir) throws IOException, InterruptedException {
        String[] cmds = [
            "cmd /c start cmd.exe",
            "cd ${dir}",
            "javac ${arquivoJava.name}"
        ]

        try {
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c",
                    String.join("& ", cmds));

            builder.redirectErrorStream(true);

            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }

                System.out.println(line);
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    private static boolean ehTipoPrimitivo(String tipo) {
        List<String> tipoPrimitivo = ['String', 'Integer', 'Boolean']
        return tipo in tipoPrimitivo
    }
}
