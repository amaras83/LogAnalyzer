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
 * AutoLoader companion object for storing static values
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object AutoLoader {
    /**
     * OS dependent separator for file paths
     *
     * @since  1.0
     * @access public
     * @var    Char
     */
    val Separator = System.getProperty("file.separator")
}

/**
 * AutoLoader handles the file loading of external
 * analyzers, parsers, and services
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  String baseDir the base conf directory to load files from
 */
class AutoLoader(baseDir: String) {
    /**
     * Load an analyzer from the conf directory
     *
     * @since  1.0
     * @access public
     * @param  String name the analyzer name
     * @return List[Analyzer] the list of analyzers from the XML file
     */
    def loadAnalyzer(name: String): List[Analyzer] =
        AnalyzerFactory.createFromXml(buildFilename("analyzers", name))
    
    /**
     * Load a parser from the conf directory
     *
     * @since  1.0
     * @access public
     * @param  String name the parser name
     * @return List[Parser] the list of parsers from the XML file
     */
    def loadParser(name: String, analyzers: List[Analyzer]): List[Parser] =
        ParserFactory.createFromXml(buildFilename("parsers", name), analyzers)
        
    /**
     * Load a service from the conf directory
     *
     * @since  1.0
     * @access public
     * @param  String name the service name
     * @return List[Service] the list of services from the XML file
     */
    def loadService(name: String, parsers: List[Parser]): List[Service] =
        ServiceFactory.createFromXml(buildFilename("services", name), parsers)
    
    /**
     * Loads a service including its related analyzers and parsers
     *
     * @since  1.0
     * @access public
     * @param  String name the service name to load
     * @return List[Service] a list of service loaded from the XML file
     */
    def loadServicesChain(name: String): List[Service] = {
        val analyzers = loadAnalyzer(name)
        val parsers = loadParser(name, analyzers)
        loadService(name, parsers)
    }
    
    /**
     * Builds a absolute file path name to the config file
     *
     * @since  1.0
     * @access protected
     * @param  String category the file category to load
     * @param  String name the file name to build
     * @return String absolute file path to the file
     */
    protected def buildFilename(category: String, name: String): String = 
        "%s%s%s%s%s.config".format(baseDir, AutoLoader.Separator, category, AutoLoader.Separator, name)
}
