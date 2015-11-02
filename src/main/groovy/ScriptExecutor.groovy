import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.file.Paths

/**
 * @author Noam Y. Tenne
 */
class ScriptExecutor {

    public static void main(String[] args) {
        def loader = new ScriptExecutor()
        loader.go(Paths.get('/home/noam/work/private/codewash/examples/splitMethodsCorrect.groovy').text)
    }

    public ExecutedScriptReport go(String scriptSource) {
        def reporter = new Reporter(scriptSource)
        def executedScriptReport = reporter.report()

        def shell = new GroovyShell(this.class.classLoader, new Binding(), new CompilerConfiguration())
        Script script = shell.parse(scriptSource)
        def result = script.run()
        executedScriptReport.result = result

        executedScriptReport
    }
}