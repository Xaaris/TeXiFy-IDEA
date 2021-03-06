package nl.hannahsten.texifyidea.psi

import com.intellij.psi.util.PsiTreeUtil
import nl.hannahsten.texifyidea.settings.TexifySettings.Companion.getInstance
import nl.hannahsten.texifyidea.util.Magic

/*
* LatexEnvironment
*/
/**
 * Find the label of the environment. The method finds labels inside the environment content as well as labels
 * specified via an optional parameter
 *
 * @return the label name if any, null otherwise
 */
fun getLabel(element: LatexEnvironment): String? {
    val stub = element.stub
    if (stub != null) return stub.label
    return if (Magic.Environment.labelAsParameter.contains(element.environmentName)) {
        // See if we can find a label option
        val optionalParameters = getOptionalParameters(element.beginCommand.parameterList)
        optionalParameters.getOrDefault("label", null)
    }
    else {
        if (!Magic.Environment.labeled.containsKey(element.environmentName)) return null
        val content = element.environmentContent ?: return null

        // See if we can find a label command inside the environment
        val children = PsiTreeUtil.findChildrenOfType(content, LatexCommands::class.java)
        if (!children.isEmpty()) {
            val labelCommands = getInstance().labelPreviousCommands
            val labelCommand = children.firstOrNull { c: LatexCommands -> labelCommands.containsKey(c.name) } ?: return null
            val requiredParameters = labelCommand.requiredParameters
            if (requiredParameters.isEmpty()) return null
            val parameterPosition = labelCommands[labelCommand.name]!!.position - 1
            return if (parameterPosition > requiredParameters.size - 1 || parameterPosition < 0) null else requiredParameters[parameterPosition]
        }
        null
    }
}

fun getEnvironmentName(element: LatexEnvironment): String? {
    val stub = element.stub
    if (stub != null) return stub.environmentName
    val parameters = element.beginCommand.parameterList
    if (parameters.isEmpty()) return ""
    val environmentNameParam = parameters[0]
    val requiredParam = environmentNameParam.requiredParam ?: return ""
    val contentList = requiredParam.paramContentList
    if (contentList.isEmpty()) return ""
    val paramText = contentList[0].parameterText ?: return ""
    return paramText.text
}