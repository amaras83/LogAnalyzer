/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

import scala.collection.mutable.ListBuffer
import scala.xml.NodeSeq

/**
 * Factory for constructing export entities
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ExportFactory {
    /**
     * Create a export from a XML node sequence
     *
     * @since  1.0
     * @param  nodes a subset of XML nodes containing export elements
     * @return a list of newly create Export entity
     */
    def createFromNodeSeq(nodes: NodeSeq): List[Export] = {
        val buffer = new ListBuffer[Export]
        
        (nodes \ "exports" \ "file").foreach{ file =>
            buffer.append(new FileExport((file \ "@src").text))
        }
            
        (nodes \ "exports" \ "email").foreach{ email =>
            buffer.append(new EmailExport(
                (email \ "@to").text,
                (email \ "@from").text,
                (email \ "@subject").text))
        }
        
        buffer.toList
    }
}
