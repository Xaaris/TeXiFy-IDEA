package nl.hannahsten.texifyidea.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import nl.hannahsten.texifyidea.run.latex.LatexDistribution
import nl.hannahsten.texifyidea.util.TexLivePackages
import nl.hannahsten.texifyidea.util.runCommand

class TexLivePackageListInitializer : StartupActivity {
    override fun runActivity(project: Project) {
        if (LatexDistribution.isTexliveAvailable) {
            val result = "tlmgr list --only-installed".runCommand() ?: return
            TexLivePackages.packageList = Regex("i\\s(.*):").findAll(result)
                    .map { it.groupValues.last() }.toMutableList()
        }
    }
}