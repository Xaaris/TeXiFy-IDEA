package nl.hannahsten.texifyidea.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.impl.CamelHumpMatcher
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.ProcessingContext
import nl.hannahsten.texifyidea.TexifyIcons
import nl.hannahsten.texifyidea.completion.handlers.LatexReferenceInsertHandler
import nl.hannahsten.texifyidea.psi.BibtexEntry
import nl.hannahsten.texifyidea.psi.LatexCommands
import nl.hannahsten.texifyidea.util.findBibtexItems
import java.util.*

object LatexBibliographyReferenceProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val lookupItems = parameters.originalFile.findBibtexItems()
                .map { bibtexEntry ->
                    when (bibtexEntry) {
                        is BibtexEntry -> {
                            val lookupStrings = LinkedList(bibtexEntry.authors)
                            lookupStrings.add(bibtexEntry.title)
                            LookupElementBuilder.create(bibtexEntry.identifier)
                                    .withPsiElement(bibtexEntry)
                                    .withPresentableText(bibtexEntry.title)
                                    .bold()
                                    .withInsertHandler(LatexReferenceInsertHandler())
                                    .withLookupStrings(lookupStrings)
                                    .withTypeText(bibtexEntry.identifier,
                                            true)
                                    .withIcon(TexifyIcons.DOT_BIB)
                        }
                        is LatexCommands -> {
                            if (bibtexEntry.requiredParameters.size == 0) return@map null
                            LookupElementBuilder.create(bibtexEntry.requiredParameters[0])
                                    .bold()
                                    .withInsertHandler(LatexReferenceInsertHandler())
                                    .withTypeText(bibtexEntry.containingFile.name + ": " + (1 + StringUtil.offsetToLineNumber(bibtexEntry.containingFile.text, bibtexEntry.textOffset)), true)
                                    .withIcon(TexifyIcons.DOT_BIB)
                        }
                        else -> {
                            null
                        }
                    }

                }
        result.withPrefixMatcher(CamelHumpMatcher(result.prefixMatcher.prefix, false)).addAllElements(lookupItems)
    }


}