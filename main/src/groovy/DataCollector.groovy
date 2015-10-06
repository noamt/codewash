/**
 * @author Noam Y. Tenne
 */
class DataCollector {

    List<InvokedMethod> invokedMethods = []

    void methodInvoked(String methondName, Object args) {
        invokedMethods << new InvokedMethod(name: methondName, args: args)
    }
}
