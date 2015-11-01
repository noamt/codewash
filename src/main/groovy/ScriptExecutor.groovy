import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.control.CompilePhase
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
        List<ASTNode> scriptAst = new AstBuilder().buildFromString(CompilePhase.CLASS_GENERATION, false, scriptSource)
        BlockStatement scriptBlockNode = scriptAst.first()
        def scriptStatements = scriptBlockNode.statements
        def potentialMethodInvocations = scriptStatements.findResults { blockExpression ->
            if ((blockExpression.expression instanceof DeclarationExpression) ||
                    (blockExpression.expression instanceof BinaryExpression)) {
                return blockExpression.expression.rightExpression
            }
            null
        }
        def methodInvocations = potentialMethodInvocations.findResults {
            if (it instanceof MethodCallExpression) {
                return new InvokedMethod(name: it.method.value)
            }
            null
        }

        def lastStatement = scriptStatements.last()
        if (!(lastStatement instanceof ReturnStatement)) {
            throw new Exception("oops the end of the block seems to miss a return statement")
        }

        def shell = new GroovyShell(this.class.classLoader, new Binding(), new CompilerConfiguration())
        Script script = shell.parse(scriptSource)
        def result = script.run()

        new ExecutedScriptReport(result: result, invokedMethods: methodInvocations)
    }
}