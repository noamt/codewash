import spock.lang.IgnoreRest
import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class ScriptExecutorSpec extends Specification {

    def 'Execute a script with zero method invocations'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
def result = 'j'
result+= 'i'
result+= 'm'
result
""")

        then:
        executionReport.result == 'jim'
        executionReport.invokedMethods.size() == 0
    }

    def 'Execute a script with one method invocations'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
def result = 'j'
result+= getTheI()
result+= 'm'
result

def getTheI() {
   'i'
}
""")

        then:
        executionReport.result == 'jim'
        executionReport.invokedMethods.size() == 1
        def invokedMethod = executionReport.invokedMethods.first()
        invokedMethod.name == 'getTheI'
        invokedMethod.length == 1
    }

    def 'Execute a script with three method invocations'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
def result = getTheJ()
result+= getTheI()
result+= getTheM()
result

def getTheJ() {
   'j'
}

def getTheI() {
   'i'
}

def getTheM() {
   'm'
}
""")

        then:
        executionReport.result == 'jim'
        def invokedMethods = executionReport.invokedMethods
        invokedMethods.size() == 3
        invokedMethods.get(0).name == 'getTheJ'
        invokedMethods.get(1).name == 'getTheI'
        invokedMethods.get(2).name == 'getTheM'
        invokedMethods.every { it.length == 1}
    }

    def 'Execute a script with a nested method invocation'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
def result = 'j'
result+= getTheIAndTheM()
result

def getTheIAndTheM() {
   'i' + getTheM()
}

def getTheM() {
   'm'
}
""")

        then:
        executionReport.result == 'jim'
        executionReport.invokedMethods.size() == 1
        def method = executionReport.invokedMethods.first()
        method.name == 'getTheIAndTheM'
        method.length == 1
    }

    def 'Execute a script with a method that throws a checked exception'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
getTheI()

def getTheI() throws Exception {
   'i'
}
""")

        then:
        def invokedMethod = executionReport.invokedMethods.first()
        invokedMethod.exceptions.first() == 'java.lang.Exception'
    }

    def 'Execute a script with a method that throws an unchecked exception'() {
        setup:
        def executor = new ScriptExecutor()

        when:
        def executionReport = executor.go("""
getTheI()

def getTheI() throws IllegalArgumentException {
   'i'
}
""")

        then:
        def invokedMethod = executionReport.invokedMethods.first()
        invokedMethod.exceptions.first() == 'java.lang.IllegalArgumentException'
    }
}