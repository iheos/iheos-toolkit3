package gov.nist.diagnostics

import javax.inject.Inject
import org.gradle.api.Task
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

import groovy.transform.TupleConstructor

import gov.nist.diagnostics.TaskUtils

/*
 **  Validation Task
 *	see TaskTemplate for a mock implementation 
 */
@TupleConstructor(includeFields=true)
class ValidationTask extends DefaultTask 
{
	def static final String validatedResource='output'
	def static final ERROR = 'E'
	def static final INFO = 'I'
	def static final DEBUG = 'D'
	def static final eventType = [(ERROR):'[E] ',(INFO):'[i] ',(DEBUG):'[D] ']

	//The task input
	def input = [:]
	
	//A map of task params
	def parameter = [:]
		
	//Reporting events containing a map type (E,I,H) & message
	def report = []
	 	
	//this usually contains the unverified task output 
	def resource

	//Closure to be supplied by the task config
	def Closure onNoOutput
	//The default output is set automatically for simple tasks
	//(must handle output assignment manually if a closure was provided)
	def Closure onComplete = {
		ext.output = resource
	}
	//This is for reporting purposes (runs after onComplete)
	def Closure onSuccess
	
	
	//Dependant task inputs 
	//(custom list incase some tasks need not have any outputs)
	def boolean nonNullInput(tList = []) {
		def nonNullInputCt = 0
		
		try {
			for (i in tList) {			
				if (this.input[(i)]!=null) 
					nonNullInputCt++
			}
			
		} catch (e) {
			logger.debug 'hasInputs method failed ' + e.toString()
			return false
		}
		
		return (tList.size() == nonNullInputCt)
			
	}

	@Inject
	ValidationTask() {
		super()
	}

}

class GeneralTask extends ValidationTask 
{


	@TaskAction
		def action() {
			try {
				def source = parameter['source']
				
							
			   if (!TaskUtils.isNullishString(source)) {			   
					if (this.onComplete instanceof Closure) {
						resource = source
						onComplete(this)
						if (this.onSuccess instanceof Closure) {
								onSuccess(this)
						}
					}
			   } else {
		   		 report << [(ERROR):'The source param is null']
				 }
			
			} catch (e) {
				report << [(ERROR):e.toString()]
		    }
			
			if (this.onNoOutput instanceof Closure && !this.hasProperty(validatedResource)) {
				onNoOutput(this)
			}

	}
		
}


class ScrapTask extends ValidationTask 
{

	@TaskAction
    def action() {
			
		 try {
			 def source = parameter['source']
			 
			 report << source
			 resource = System.getenv()[source]
			 report << "results: ${resource}"
			 
			 //byte[] fileContent = FileUtils.readFileToByteArray(new File("hello.txt"))
			 
				//byte[] fileContent = FileUtils.readFileToByteArray(new File("hello.txt"));
		  
				//ext.info << ext.input
				//ext.output = TaskUtils.getExternalProperty(ext,ext.code)
				
			} catch (e) {
				report << [(ERROR):e.toString()]
			}
    }




}


/*
 **  Validation Task
 */
class FileExists extends ValidationTask 
{

		@TaskAction
		def action() {
			
			try {
				def source = parameter['source']
							
			   if (!TaskUtils.isNullishString(source)) {
				   if (TaskUtils.isFileLocatable(source))
						if (this.onComplete instanceof Closure) {
							resource = source
							onComplete(this)
							if (this.onSuccess instanceof Closure) {
									onSuccess(this)
							}
						}
			   } else
		   		 report << [(ERROR):'The source param is null']
			
			} catch (e) {
				report << [(ERROR):e.toString()]
		    }
			
			if (this.onNoOutput instanceof Closure && !this.hasProperty(validatedResource)) {
				onNoOutput(this)
			}

	}
		

	
	
}


/*
**  Validation Task
*/
class GetExternalProperty extends ValidationTask 
{

	@TaskAction
	def action() {
		
		try {
			def source = parameter['source']
			def key = 	 parameter['key']

			resource = TaskUtils.getExternalProperty(source,key)
			if (!TaskUtils.isNullishString(resource)) { 		
				if (this.onComplete instanceof Closure) {
					onComplete(this)
					if (this.onSuccess instanceof Closure) {
						onSuccess(this)					
					}
				}
			}

			if (this.onNoOutput instanceof Closure && !this.hasProperty(validatedResource)) {
				onNoOutput(this)
			}
		} catch (e) {
			report << [(ERROR):e.toString()]
		}
		
	}
}
