package gov.nist.diagnostics

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import static org.junit.Assert.*

class ValidationTaskTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('hello task test', type: gov.nist.diagnostics.ValidationTask)
        assertTrue(task instanceof ValidationTask)
    }
}
