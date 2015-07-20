package gov.nist.hit.tkConsole;


import java.net.URL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import org.gradle.tooling.BuildException;


import java.io.File;

public class TkRunTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {
        // resp.getOutputStream().write("Hello World.".getBytes());


// Configure the connector and create the connection
        GradleConnector connector = GradleConnector.newConnector();

    

		        URL resource = new TkRunTaskServlet().getClass().getResource("/init.gradle");
        if (resource == null) {
            //logger.fatal("Cannot load init.gradle");
            throw new RuntimeException("Cannot load init.gradle");
        }


        connector.forProjectDirectory(new File(resource.getFile()).getParentFile()); // "../"

        ProjectConnection connection = connector.connect();
        try {

			
            

            // Configure the build
            BuildLauncher launcher = connection.newBuild();
 			launcher.setJvmArguments("-Xmx768m", "-XX:MaxPermSize=256m");

			String taskName = "help";
			if (req.getParameter("taskName")!=null) {
				taskName = req.getParameter("taskName");
			}



			String verboseOpt  
			
				= req.getParameter("verbose") + ":" + taskName; // Enable reporting for this task only.
			


			launcher.withArguments("-Pverbose="+verboseOpt, "-q","--init-script", "init.gradle"); // Make this a String[]

            launcher.forTasks(taskName); // Example: tasks, help
            launcher.setStandardOutput(resp.getOutputStream());
            launcher.setStandardError(resp.getOutputStream());

            // Run the build
            launcher.run();
		}  catch (BuildException t) {
			 // StackTraceElement[] stackTraceElements = t.getStackTrace();
			//resp.getOutputStream().write( t.toString().getBytes());

			String cause = t.getCause().toString();
			
			String candidateStr = "andidates are";
			int candidateIndex = cause.indexOf(candidateStr);
			int notFoundIndex = cause.indexOf("not found in root project");

			if (candidateIndex>-1) {
				cause = "Choices are" + cause.substring(candidateIndex+candidateStr.length());
			} else if (notFoundIndex>-1) {
				int taskIndex = cause.indexOf(": Task");
				int beginIndex = 0;
				if (taskIndex>-1)
					beginIndex = taskIndex += 2;
				
				cause =  cause.substring(beginIndex,notFoundIndex+9) + ".";
			}

			//cause += "\n<!-- " + t.getCause().toString() + " -->";
			
			resp.getOutputStream().write(cause.getBytes());

			//resp.getOutputStream().write(t.getMessage().toString().getBytes());

    	}    	
         /* } catch (Throwable t) {
			resp.getOutputStream().write( t.toString().getBytes());
		} */ finally {
            // Clean up
            connection.close();
        }

    }
}