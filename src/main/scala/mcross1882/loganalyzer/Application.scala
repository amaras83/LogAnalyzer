/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import mcross1882.loganalyzer._

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
        if (args.length < 2) {
            return help
        }
        
        try {
            val loader = new AutoLoader("src/universal/dist/")
            
            val services = loader.loadServicesChain(args(0))
            
            for (service <- services) service.run(args(1).split(',').toList)
            
            for (service <- services) {
                service.print
                service.export
            }
        } catch {
            case e: Exception => println("Error: %s".format(e.getMessage))
        }
    }
    
    /**
     * Program help dialog
     *
     * @since  1.0
     * @return Unit
     */
    protected def help: Unit = println("Syntax: loganalyzer [options] [service]")
}
