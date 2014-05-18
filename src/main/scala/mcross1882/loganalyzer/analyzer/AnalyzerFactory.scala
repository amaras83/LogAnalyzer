/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
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
     * Create an analyzer from a string name
     *
     * @since  1.0
     * @access public
     * @param  String anType the analyzer type
     * @param  String category the analyzer category
     * @param  Regex pattern the pattern to match against the log line
     * @param  String message the output message
     * @return Analyzer
     */
    def createFromName(anType: String, category: String, pattern: Regex, message: String): Analyzer = {
        anType match {
            case _ => new SimpleAnalyzer(category, pattern, message)
        }
    }

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
            var anType   = (analyzer \ "@type").text
            val category = (analyzer \ "@category").text
            val regex    = (analyzer \ "@regex").text
            val output   = analyzer.text.trim
            val args     = (analyzer \ "@vars").text.split('|')
            
            if (anType.isEmpty) {
                anType = "SimpleAnalyzer"
            }
            
            buffer.append(createFromName(anType, category, new Regex(regex, args: _*), output))
        }
        
        buffer.toList
    }
}
