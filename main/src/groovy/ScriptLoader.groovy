import org.codehaus.groovy.control.CompilerConfiguration

/**
 * @author Noam Y. Tenne
 */
class ScriptLoader {

    public static void main(String[] args) {
        def loader = new ScriptLoader()
        loader.go()
    }

    void go() {
        def compilerConfiguration = new CompilerConfiguration()
        compilerConfiguration.scriptBaseClass = DelegatingInterceptable.class.name

        def collector = new DataCollector()
        def binding = new Binding(dataCollector: collector)

        def shell = new GroovyShell(this.class.classLoader, binding, compilerConfiguration)

        DelegatingScript script = shell.parse(new File('/home/noam/work/private/codewash/examples/splitMethodsCorrect.groovy'))
        def evaluator = new Evaluator()
        script.setDelegate(evaluator)
        println script.run()

        println "${collector.invokedMethods.size()} methods were invoked"
    }
}
