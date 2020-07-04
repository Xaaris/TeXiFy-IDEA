package nl.hannahsten.texifyidea.run.latex.logtab

/**
 * Regex (text) every error handler and message processor can use.
 */
object LogMagicRegex {
    const val LINE_WIDTH = 79

    const val FILE_LINE_REGEX: String = """(?<file>.+)?:(?<line>\d+):""" // error
    const val LINE_REGEX: String = """on input line (?<line>\d+).""" // meestal warning
    const val LATEX_ERROR_REGEX: String = "!" // error
    const val PDFTEX_ERROR_REGEX: String = "!pdfTeX error:"
    const val LATEX_WARNING_REGEX: String = "LaTeX( Font)? Warning:" // warning
    const val PACKAGE_REGEX: String = """(?<package>[\d\w]+)""" // package error/warning?
    const val REFERENCE_REGEX: String = """(?<label>(`|').+')""" // reference warning

    val TEX_MISC_WARNINGS = listOf(
            "LaTeX Warning: ",
            "LaTeX Font Warning: ",
            "AVAIL list clobbered at",
            "Citation",
            "Double-AVAIL list clobbered at",
            "Doubly free location at",
            "Bad flag at",
            "Runaway definition",
            "Runaway argument",
            "Runaway text",
            "Missing character: There is no",
            "No auxiliary output files.",
            "No pages of output.",
            "Underfull \\hbox",
            "Overfull \\hbox",
            "Loose \\hbox",
            "Tight \\hbox",
            "Underfull \\vbox",
            "Overfull \\vbox",
            "Loose \\vbox",
            "Tight \\vbox",
            "(\\end occurred"
    )

    /**
     * These warnings span more than two lines, so the [LatexOutputListener] needs to continue collecting it.
     */
    val TEX_MISC_WARNINGS_MULTIPLE_LINES = listOf(
        "LaTeX Warning: You have requested, on input line"
    )
}