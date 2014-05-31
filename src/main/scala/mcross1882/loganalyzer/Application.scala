/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import mcross1882.loganalyzer._
import mcross1882.loganalyzer.service.Service

/**
 * Main Entry point
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object Application {
    /**
     * The global system variable required to use the application
     * this value should point to the applications conf/ directory
     *
     * @since 1.0
     */
    protected val HomeEnvironmentKey = "LOGANALYZER_HOME"

    /**
     * Program start
     *
     * @since  1.0
     * @param  args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        if (args.length < 2) {
            return help
        }    
        
        try {
            val loader = new AutoLoader(homeDirectory)
            val services = loader.loadServicesChain(args(0))
            
            runAllServices(services, args(1))
            printAndExportServices(services)
        } catch {
            case e: Exception => println("Error: %s".format(e.getMessage))
        }
    }
    
    /**
     * Returns the applications home directory for configuration files
     * this method will throw if the LOGANALYZER_HOME environment variable
     * is not set
     *
     * @since  1.0
     * @return the path to the application home
     */
    protected def homeDirectory: String = {
        val home = System.getenv(HomeEnvironmentKey)
        if (null == home) {
            throw new Exception(f"Environment variable $HomeEnvironmentKey%s must be set.")
        } 
        home
    }
    
    /**
     * Runs all services with a set of comma separated dates to filter on
     *
     * @since 1.0
     * @param services the list of serviecs to run
     * @param dates the dates to include
     */
    protected def runAllServices(services: List[Service], dates: String): Unit =
        for (service <- services) service.run(dates.split(',').toList)
     
    /**
     * Iterates through all services first printing the results
     * followed by exporting them
     *
     * @since 1.0
     * @param services the list of services to run
     */
    protected def printAndExportServices(services: List[Service]): Unit = {
        for (service <- services) {
            service.print
            service.export
        }
    }
    
    /**
     * Program help dialog
     *
     * @since  1.0
     * @return Unit
     */
    protected def help: Unit = println(
"""
Log Analyzer Help
=================
Syntax: loganalyzer [service] [dates]

Service -- The name of the service you wish to run
Example: php, httpd, demo, etc..

Dates   -- A comma separated list of dates to filter on when parsing
Example: "2014-05-01,2014-05-02" (single dates cane be used as well)
"""
    )
}
