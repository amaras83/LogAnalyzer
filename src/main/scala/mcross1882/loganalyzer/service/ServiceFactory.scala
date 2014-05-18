/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.service

import mcross1882.loganalyzer.parser.Parser
import scala.collection.mutable.ListBuffer
import scala.xml.XML


/**
 * ServiceFactory constructs a list of services
 * from an external configuration file
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ServiceFactory {
    /**
     * @since  1.0
     * @access private
     * @var    String
     */
    private val ConfigFile = "/services.config"

    /**
     * Creates a list of parsers from an XML file
     *
     * @since  1.0
     * @access public
     * @param  List[Parsers] the predefined parsers to use when building a service
     * @return List[Service]
     */
    def createFromXml(parsers: List[Parser]): List[Service] = {
        val root = XML.load(getClass.getResourceAsStream(ConfigFile))
        
        val buffer = new ListBuffer[Service]
        val nameBuffer = new ListBuffer[String]
        val fileBuffer = new ListBuffer[String]
        
        var name: String = ""
        var title: String = ""
        
        (root \ "service").foreach{ service =>
            nameBuffer.clear
            
            name = (service \ "@name").text
            title = (service\ "@title").text
            
            (service \ "logfiles" \ "file").foreach{ file =>
                fileBuffer.append((file \ "@src").text)
            }
            
            (service \ "parsers" \ "parser").foreach{ parser =>
                nameBuffer.append((parser \ "@name").text)
            }
            
            buffer.append(new Service(
                name, 
                title, 
                fileBuffer.toList, 
                buildParserList(parsers, nameBuffer.toList)))
        }
        
        buffer.toList
    }
    
    /**
     * Build a list of parsers from a whitelist of parser names
     *
     * @since  1.0
     * @access protected
     * @param  List[Parser] the available parsers
     * @param  List[String] whitelisted parser names
     * @return List[Parser] filtered list of parsers
     */
    protected def buildParserList(parsers: List[Parser], names: List[String]): List[Parser] = 
        parsers.filter(x => names.contains(x.name))
}
