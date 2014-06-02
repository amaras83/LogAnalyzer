/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.service

import mcross1882.loganalyzer.export.ExportFactory
import mcross1882.loganalyzer.parser.Parser
import scala.collection.mutable.ListBuffer
import scala.xml.XML
import scala.xml.NodeSeq

/**
 * ServiceFactory constructs a list of services
 * from an external configuration file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ServiceFactory {
    /**
     * List buffer for storing and generating
     * immutable service lists
     *
     * @since 1.0
     */
    private val _serviceBuffer = new ListBuffer[Service]
        
    /**
     * List buffer for storing log file names
     *
     * @since 1.0
     */
    private val _logFileBuffer = new ListBuffer[String]
       
    /**
     * List buffer for storing parser names
     * 
     * @since 1.0
     */
    private val _parserNameBuffer = new ListBuffer[String]
   
    /**
     * Creates a list of parsers from an XML file
     *
     * @since  1.0
     * @param  String filename the xml file to load
     * @param  List[Parsers] the predefined parsers to use when building a service
     * @return List[Service]
     */
    def createFromXml(filename: String, parsers: List[Parser]): List[Service] = {
        parserListIsNotEmptyOrThrow(parsers)
        _serviceBuffer.clear
        
        val root = XML.loadFile(filename)
        var logFiles = List.empty[String]
        var parserNames = List.empty[String]
        
        (root \ "service").foreach{ service =>
            logFiles = readLogFiles(service)
            parserNames = readParsers(service)

            _serviceBuffer.append(new Service(
                (service \ "@name").text, 
                (service\ "@title").text, 
                logFiles, 
                buildParserList(parsers, parserNames),
                ExportFactory.createFromNodeSeq(service)))
        }
        
        _serviceBuffer.toList
    }
    
    /**
     * Checks to see if the parser list is not empty. If it is then
     * an exception will be thrown
     *
     * @since 1.0
     */
    protected def parserListIsNotEmptyOrThrow(parsers: List[Parser]): Unit = {
        if (parsers.isEmpty) {
            throw new IllegalArgumentException("Parser list cannot be empty when constructing a Service")
        }
    }
    
    /**
     * Reads all the log files defined in the xml file into a string list
     *
     * @since  1.0
     * @param serviceRoot the XML nodeseq to parse
     * @return immutable list of log file names
     */
    protected def readLogFiles(serviceRoot: NodeSeq): List[String] = {
        _logFileBuffer.clear
        (serviceRoot \ "logfiles" \ "file").foreach{ file =>
            _logFileBuffer.append(((file \ "@src").text))
        }
        _logFileBuffer.toList
    }
    
    /**
     * Reads all the parsers defined in the xml file into a string list
     *
     * @since  1.0
     * @param  serviceRoot the xml nodeseq to use for parsing
     * @return immutable list of parser names
     */
    protected def readParsers(serviceRoot: NodeSeq): List[String] = {
        _parserNameBuffer.clear
        (serviceRoot \ "parsers" \ "parser").foreach{ parser =>
            _parserNameBuffer.append(((parser \ "@name").text))
        }
        _parserNameBuffer.toList
    }
    
    /**
     * Build a list of parsers from a whitelist of parser names
     *
     * @since  1.0
     * @param  List[Parser] the available parsers
     * @param  List[String] whitelisted parser names
     * @return List[Parser] filtered list of parsers
     */
    protected def buildParserList(parsers: List[Parser], names: List[String]): List[Parser] = 
        parsers.filter(x => names.contains(x.name))
}
