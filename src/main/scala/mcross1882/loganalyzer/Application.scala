/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import mcross1882.loganalyzer.service.Service

/**
 * Main Entry point
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object Application {
    /**
     * Program start
     *
     * @since  1.0
     * @param  args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        val console = createApplicationConsole
        if (args.length < 1) {
            return console.help
        }
        
        val params = console.build(args)
        
        try {
            val loader = new AutoLoader(AutoLoader.HomeDirectory)
            val services = loader.loadServicesChain(params("service"))
            
            runAllServices(services, params.getOrElse("dates", ""))
            printAndExportServices(services)
        } catch {
            case e: Exception => println("Error: %s".format(e.getMessage))
        }
    }
   
    /**
     * Create the application console to parse command line arguments
     *
     * @since  1.0
     * @return Command line Console
     */
    protected def createApplicationConsole: Console = {
        val console = new Console
        console
            .argument("service", "The service to run (demo, php, httpd, etc...)")
            .argument("dates", "Comma separated date values to filter on (Format YYYY-MM-DD HH:mm:ss time is optional)")
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
}
