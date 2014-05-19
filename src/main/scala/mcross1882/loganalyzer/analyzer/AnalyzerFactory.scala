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
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object AnalyzerFactory {
    /**
     * Create an analyzer from a string name
     *
     * @since  1.0
     * @param  anType the analyzer type
     * @param  category the analyzer category
     * @param  pattern the pattern to match against the log line
     * @param  message the output message
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
     * @param  filename the xml file to load
     * @return List[Analyzer]
     */
    def createFromXml(filename: String): List[Analyzer] = {
        val root = XML.loadFile(filename)
        
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
