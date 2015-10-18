/**
 * @author Noam Y. Tenne
 */
class DelegatingInterceptable extends DelegatingScript implements GroovyInterceptable {

    @Override
    Object run() {
        ''
    }

    @Override
    Object invokeMethod(String name, Object args) {
        if (name == 'setDelegate') {
            return super.setDelegate(args)
        }
        if ((!DelegatingScript.declaredMethods.any { it.name == name }) &&
                name != 'run' && name != 'getBinding') {
            binding.dataCollector.methodInvoked(name, args)
        }

        super.invokeMethod(name, args)
    }
}