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
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object Application {  
    /**
     * Program start
     *
     * @since  1.0
     * @access public
     * @param  Array[String] args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        val loader = new AutoLoader("conf\\dist")
        
        val services = loader.loadServicesChain("demo")
        
        for (service <- services) service.run
        
        for (service <- services) service.print
    }
}
