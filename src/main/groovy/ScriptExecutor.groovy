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
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = DelegatingInterceptable.class.name

        def collector = new DataCollector()
        def binding = new Binding(dataCollector: collector)

        def shell = new GroovyShell(this.class.classLoader, binding, compilerConfiguration)

        DelegatingScript script = shell.parse(scriptSource)
        def evaluator = new Evaluator()
        script.setDelegate(evaluator)
        def result = script.run()

        new ExecutedScriptReport(result: result, invokedMethods: collector.invokedMethods)
    }
}
