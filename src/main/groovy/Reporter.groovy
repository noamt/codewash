import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase

/**
 * @author Noam Y. Tenne
 */
class Reporter {

    private String scriptSource

    public Reporter(String scriptSource) {
        this.scriptSource = scriptSource
    }

    public ExecutedScriptReport report() {
        List<ASTNode> scriptAst = buildAst()
        Collection<InvokedMethod> methodInvocations = invokedMethods(scriptAst)
        new ExecutedScriptReport(invokedMethods: methodInvocations)
    }

    private List<ASTNode> buildAst() {
        new AstBuilder().buildFromString(CompilePhase.CANONICALIZATION, false, scriptSource)
    }

    private Collection<InvokedMethod> invokedMethods(List<ASTNode> scriptAst) {
        Collection<Statement> potentialMethodInvocations = potentialInvocations(scriptAst)
        resolveMethodInvocations(scriptAst, potentialMethodInvocations)
    }

    private Collection<Statement> potentialInvocations(List<ASTNode> scriptAst) {
        BlockStatement scriptBlockNode = scriptAst.first()
        def scriptStatements = scriptBlockNode.statements
        def potentialMethodInvocations = scriptStatements.findResults { blockExpression ->
            if (expressionIsMethodCall(blockExpression)) {
                return blockExpression
            }
            if (expressionIsNesting(blockExpression)) {
                return blockExpression.expression.rightExpression
            }
            if (expressionIsStatement(blockExpression)) {
                return blockExpression.expression
            }
            null
        }
        potentialMethodInvocations
    }

    private Collection<InvokedMethod> resolveMethodInvocations(List<ASTNode> scriptAst, Collection<Statement> potentialMethodInvocations) {
        ClassNode scriptClassNode = scriptAst.last()
        def methodInvocations = potentialMethodInvocations.findResults { expression ->
            if (expressionIsMethodCall(expression)) {
                def methodName = expression.method.value
                MethodNode method = scriptClassNode.methods.find { it.name == methodName }
                return new InvokedMethod(name: methodName,
                        length: (method.lastLineNumber - method.lineNumber - 1),
                        exceptions: method.exceptions.collect { it.name }
                )
            }
            null
        }
        methodInvocations
    }

    private boolean expressionIsMethodCall(blockExpression) {
        blockExpression instanceof MethodCallExpression
    }

    private boolean expressionIsNesting(blockExpression) {
        (blockExpression.expression instanceof DeclarationExpression) ||
                (blockExpression.expression instanceof BinaryExpression)
    }

    private boolean expressionIsStatement(blockExpression) {
        blockExpression instanceof ExpressionStatement
    }
}
