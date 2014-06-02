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
     * Dates command line argument index
     *
     * @since 1.0
     */
    private val DatesArgumentIndex = 1
    
    /**
     * Program start
     *
     * @since  1.0
     * @param  args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        if (args.length < 1) {
            return help
        }
        
        var dates = ""
        if (args.isDefinedAt(DatesArgumentIndex)) {
            args(DatesArgumentIndex)
        }
        
        try {
            val loader = new AutoLoader(AutoLoader.homeDirectory)
            val services = loader.loadServicesChain(args(0))
            
            runAllServices(services, dates)
            printAndExportServices(services)
        } catch {
            case e: Exception => println("Error: %s".format(e.getMessage))
        }
    }
    
    /**
     * Runs all services with a set of comma separated dates to filter on
     *
     * @since 1.0
     * @param services the list of serviecs to run
     * @param dates the dates to include
     */
    protected def runAllServices(services: List[Service], dates: String): Unit = {
        val inputDates = parseDateString(dates)
        for (service <- services) service.run(inputDates)
    }
    
    /**
     * Parse a comma separated date string into a list
     *
     * @since  1.0
     * @param  dates a comma separated date string
     * @return a list of dates represented as strings
     */
    protected def parseDateString(dates: String): List[String] = {
        if (null != dates && !dates.isEmpty) {
            dates.split(',').toList
        } else {
            List.empty[String]
        }
    }
     
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
