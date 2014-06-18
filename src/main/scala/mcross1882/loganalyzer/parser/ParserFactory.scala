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
     * Create a parser from a parser type
     *
     * @since  1.0
     * @param  parserType the parser type
     * @param  name the parser name
     * @param  files to parse
     * @param  analyzers to use bind
     * @return Parser
     */
    def createFromName(parserType: String, name: String, files: List[String], analyzers: List[Analyzer]): Parser = {
        if (isIllegalString(parserType) || isIllegalString(name)) {
            throw new IllegalArgumentException("Parser type and name cannot be null or empty");
        }
        
        parserType match {
            case _ => new SimpleParser(name, files, analyzers)
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
        var analyzerNames = List.empty[String]
        var filenames = List.empty[String]
        val parserBuffer = new ListBuffer[Parser]
        
        (root \ "parser").foreach{ parser =>
            filenames = readXMLByAttribute("logfile", "@src", (parser \ "logfiles"))
            analyzerNames = readXMLByAttribute("analyzer", "@name", (parser \ "analyzers"))
       
            if (!filenames.isEmpty && !analyzerNames.isEmpty) {
                parserBuffer.append(createFromName(
                    (parser \ "@type").text, 
                    (parser \ "@name").text, 
                    filenames,
                    filterAnalyzersByName(analyzers, analyzerNames)))
            }
        }
        parserBuffer.toList
    }
    
    /**
     * Reads the related log files into the string buffer
     *
     * @since 1.0
     * @param leafName the leaf node to traverse
     * @param NodeSeq to traverse
     */
    protected def readXMLByAttribute(leafName: String, attribute: String, root: NodeSeq): List[String] = {
        val buffer = new ListBuffer[String]
        (root \ leafName).foreach{ item => 
            buffer.append((item \ attribute).text)
        }
        buffer.toList
    }
    
    /**
     * Build a list of analyzers matched to a list of names
     *
     * @since  1.0
     * @param  analyzers
     * @param  categories
     * @return List[Analyzer]
     */
    protected def filterAnalyzersByName(analyzers: List[Analyzer], names: List[String]): List[Analyzer] =
        analyzers.filter(x => names.contains(x.name))
        
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
