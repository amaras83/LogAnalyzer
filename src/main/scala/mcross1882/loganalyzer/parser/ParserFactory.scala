/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.parser

import mcross1882.loganalyzer.analyzer.Analyzer
import scala.collection.mutable.ListBuffer
import scala.xml.XML

/**
 * ParserFactory constructs a list of parsers
 * from an external configuration file
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ParserFactory {
    /**
     * @since  1.0
     * @access private
     * @var    String
     */
    private val ConfigFile = "/parsers.config"

    /**
     * Creates a list of parsers from an XML file
     *
     * @since  1.0
     * @access public
     * @param  List[Analyzer] the predefined analyzers to use when building a parser
     * @return List[Parser]
     */
    def createFromXml(analyzers: List[Analyzer]): List[Parser] = {
        val root = XML.load(getClass.getResourceAsStream(ConfigFile))
        
        val buffer = new ListBuffer[Parser]
        val categoryBuffer = new ListBuffer[String]
        
        (root \ "parser").foreach{ parser =>
            buffer.clear
            (parser \ "analyzer").foreach{ analyzer =>
                categoryBuffer.append((analyzer \ "@category").text)
            }
            
            buffer.append(new SimpleParser(buildAnalyzerList(analyzers, categoryBuffer.toList)))
        }
        
        buffer.toList
    }
    
    /**
     * Build a list of analyzers given a list of categories
     *
     * @since  1.0
     * @access protected
     * @param  List[Analyzer] analyzers
     * @param  List[String] categories
     * @return List[Analyzer]
     */
    protected def buildAnalyzerList(analyzers: List[Analyzer], categories: List[String]): List[Analyzer] = {
        val buffer = new ListBuffer[Analyzer]
        
        for (category <- categories) {
            analyzers.foreach{ analyzer =>
                if (category equals analyzer.category) {
                    buffer append analyzer
                }
            }
        }
        
        buffer.toList
    }
}
