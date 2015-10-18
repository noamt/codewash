/**
 * @author Noam Y. Tenne
 */
class DataCollector {

    List<InvokedMethod> invokedMethods = []

    void methodInvoked(String methodName, Object args) {
        def stackTrace = Thread.currentThread().stackTrace

        def elementHasAlreadyBeenInvoked = { StackTraceElement stackTraceElement ->
            def stackTraceMethodName = stackTraceElement.methodName

            def equalsStackTraceMethodName = { InvokedMethod invokedMethod ->
                stackTraceMethodName == invokedMethod.name
            }

            invokedMethods.any equalsStackTraceMethodName
        }

        def invokedMethodIsASubInvocationOfAnother = stackTrace.any(elementHasAlreadyBeenInvoked)
        if (!invokedMethodIsASubInvocationOfAnother) {
            invokedMethods << new InvokedMethod(name: methodName, args: args)
        }
    }
}
