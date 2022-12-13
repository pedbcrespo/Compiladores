package backEndCompilador.geradorJava
import groovy.json.JsonSlurper

class GeradorCodigoJava {
    private static Map mapJson
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
        traduzClasses()
    }

    private static void traduzClasses() {

    }

    private static List<String> traduzAtributos() {

    }

    private static List<String> traduzMetodos() {

    }
}
