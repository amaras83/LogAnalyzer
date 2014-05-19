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
     * Create a parser from a parser type
     *
     * @since  1.0
     * @access public
     * @param  String parserType the parser type
     * @param  String name the parser name
     * @param  List[Analyzer] the analyzers to use bind
     * @return Parser
     */
    def createFromName(parserType: String, name: String, analyzers: List[Analyzer]): Parser = parserType match {
        case _ => new SimpleParser(name, analyzers)
    }

    /**
     * Creates a list of parsers from an XML file
     *
     * @since  1.0
     * @access public
     * @param  String filename the xml file to load
     * @param  List[Analyzer] the predefined analyzers to use when building a parser
     * @return List[Parser]
     */
    def createFromXml(filename: String, analyzers: List[Analyzer]): List[Parser] = {
        val root = XML.loadFile(filename)
        
        val buffer = new ListBuffer[Parser]
        val categoryBuffer = new ListBuffer[String]
        var name: String = ""
        var parserType: String = ""
        
        (root \ "parser").foreach{ parser =>
            categoryBuffer.clear
            
            name = (parser \ "@name").text
            parserType = (parser \ "@type").text
            
            (parser \ "analyzer").foreach{ analyzer =>
                categoryBuffer.append((analyzer \ "@category").text)
            }
            
            buffer.append(createFromName(parserType, name, buildAnalyzerList(analyzers, categoryBuffer.toList)))
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
    protected def buildAnalyzerList(analyzers: List[Analyzer], categories: List[String]): List[Analyzer] =
        analyzers.filter(x => categories.contains(x.category))
}
