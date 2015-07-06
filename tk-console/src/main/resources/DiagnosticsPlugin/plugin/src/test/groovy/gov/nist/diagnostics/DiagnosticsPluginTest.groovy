package gov.nist.diagnostics

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class DiagnosticsPluginTest {
    @Test
    public void diagPluginAddsValidationTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'diagnostics'

        // assertTrue(project.tasks.hello instanceof ValidationTask)
    }
}
