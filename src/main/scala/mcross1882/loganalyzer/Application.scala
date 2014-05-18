/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import mcross1882.loganalyzer.analyzer.{Analyzer, AnalyzerFactory}
import mcross1882.loganalyzer.parser.{Parser, ParserFactory}
import mcross1882.loganalyzer.service.{Service, ServiceFactory}

/**
 * Main Entry point
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object Application {
    /**
     * Analyzers that will be used on the log input
     *
     * @since  1.0
     * @access protected
     * @var    List[Analyzer]
     */
    protected val _analyzers = AnalyzerFactory.createFromXml
    
    /**
     * Parsers that will be used on the log output
     * and stdout
     *
     * @since  1.0
     * @access protected
     * @var    List[Parser]
     */
    protected val _parsers = ParserFactory.createFromXml(_analyzers)
    
    /**
     * Services that will be used
     *
     * @since  1.0
     * @access protected
     * @var    List[Service]
     */
    protected val _services = ServiceFactory.createFromXml(_parsers)
    
    /**
     * Run all of the loaded services
     *
     * @since  1.0
     * @access protected
     * @return Unit
     */
    protected def runAllServices: Unit =
        for (service <- _services) service.run
    
    /**
     * Print the results of all the services
     *
     * @since  1.0
     * @access protected
     * @return Unit
     */
    protected def printAllServices: Unit =
        for (service <- _services) service.print
    
    /**
     * Program start
     *
     * @since  1.0
     * @access public
     * @param  Array[String] args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        runAllServices
        printAllServices
    }
}
