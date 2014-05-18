/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

import scala.collection.mutable.ListBuffer
import scala.xml.XML

/**
 * AnalyzerFactory creates a list of
 * analyzers from a external configuration file
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object AnalyzerFactory {
    /**
     * @since  1.0
     * @access private
     * @var    String
     */
    private val ConfigFile = "/analyzers.config"

    /**
     * Creates a list of analyzers from an XML file
     *
     * @since  1.0
     * @access public
     * @return List[Analyzer]
     */
    def createFromXml: List[Analyzer] = {
        val root = XML.load(getClass.getResourceAsStream(ConfigFile))
        
        val buffer = new ListBuffer[Analyzer]
        
        (root \ "analyzer").foreach{ analyzer =>
            val category = (analyzer \ "@category").text
            val regex = (analyzer \ "@regex").text
            val output = analyzer.text.trim
            
            buffer.append(new SimpleAnalyzer(category, regex.r, output))
        }
        
        buffer.toList
    }
}
