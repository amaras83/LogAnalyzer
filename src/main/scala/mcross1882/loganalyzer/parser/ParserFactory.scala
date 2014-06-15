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
import scala.xml.{NodeSeq, XML}

/**
 * ParserFactory constructs a list of parsers
 * from an external configuration file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ParserFactory {
    /**
     * A list buffer for appending parser
     *
     * @since 1.0
     */
    private val _parserBuffer = new ListBuffer[Parser]
    
    /**
     * A list of analyzer names to store bind with a given parser
     *
     * @since 1.0
     */
    private val _analyzerNames = new ListBuffer[String]
    
    /**
     * Create a parser from a parser type
     *
     * @since  1.0
     * @param  parserType the parser type
     * @param  name the parser name
     * @param  analyzers to use bind
     * @return Parser
     */
    def createFromName(parserType: String, name: String, analyzers: List[Analyzer]): Parser = {
        if (isIllegalString(parserType) || isIllegalString(name)) {
            throw new IllegalArgumentException("Parser type and name cannot be null or empty");
        }
        
        parserType match {
            case _ => new SimpleParser(name, analyzers)
        }
    }

    /**
     * Creates a list of parsers from an XML file
     *
     * @since  1.0
     * @param  filename the xml file to load
     * @param  analyzers the predefined analyzers to use when building a parser
     * @return List[Parser]
     */
    def createFromXml(filename: String, analyzers: List[Analyzer]): List[Parser] = {
        if (analyzers.isEmpty) {
            throw new IllegalArgumentException("Analyzers must be present when constructing a parser.")
        }
        
        val root = XML.loadFile(filename)
        readParsersIntoBuffer(root, analyzers)
    }
    
    /**
     * Read all parsers in the configuration file into the parser list buffer
     *
     * @since  1.0
     * @param  root the xml root node containing the parsers
     * @param  analyzers a list of analyzers to be filtered then bound to the parsers
     * @return a list of imported parsers
     */
    protected def readParsersIntoBuffer(root: NodeSeq, analyzers: List[Analyzer]): List[Parser] = {   
        _parserBuffer.clear
        (root \ "parser").foreach{ parser =>
            readAnalyzersIntoBuffer(parser)
            
            _parserBuffer.append(createFromName(
                (parser \ "@type").text, 
                (parser \ "@name").text, 
                buildAnalyzerList(analyzers)))
        }
        _parserBuffer.toList
    }
    
    /**
     * Reads an xml node sequence of analyzer elements
     * storing their respective category attributes to load later
     *
     * @since  1.0
     * @param  root the xml root contains the analyzer element list
     * @return a list of analyzer categories
     */
    protected def readAnalyzersIntoBuffer(root: NodeSeq): List[String] = {
        _analyzerNames.clear
        (root \ "analyzer").foreach{ analyzer =>
            _analyzerNames.append((analyzer \ "@name").text)
        }
        _analyzerNames.toList
    }
    
    /**
     * Build a list of analyzers matched to a list of names
     *
     * @since  1.0
     * @param  analyzers
     * @param  categories
     * @return List[Analyzer]
     */
    protected def buildAnalyzerList(analyzers: List[Analyzer]): List[Analyzer] =
        analyzers.filter(x => _analyzerNames.contains(x.name))
        
    /**
     * Checks if a string is null or empty
     *
     * @since  1.0
     * @param  text the string to check
     * @return true if the string is not null and not empty; false otherwise
     */
    protected def isIllegalString(text: String): Boolean = 
        null == text || text.isEmpty
}
