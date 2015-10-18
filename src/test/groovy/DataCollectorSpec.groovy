import spock.lang.Specification

/**
 * @author Noam Y. Tenne
 */
class DataCollectorSpec extends Specification {

    def 'Invoke a method'() {
        setup:
        def collector = new DataCollector()

        when:
        collector.methodInvoked('methodName', ['args'])

        then:
        def invokedMethods = collector.invokedMethods
        invokedMethods.size() == 1

        def invokedMethod = invokedMethods.first()
        invokedMethod.name == 'methodName'
        invokedMethod.args == ['args']
    }

    def 'Invoke a nested method'() {
        setup:
        def collector = new DataCollector()
        collector.methodInvoked('someMethod', ['args'])

        when:
        someMethod(collector)

        then: 'Make sure that the nested invocation isn\'t on the list'
        def invokedMethods = collector.invokedMethods
        invokedMethods.size() == 1

        def invokedMethod = invokedMethods.first()
        invokedMethod.name == 'someMethod'
        invokedMethod.args == ['args']
    }

    void someMethod(DataCollector collector) {
        collector.methodInvoked('nestedInvocation', ['args'])
    }
}
