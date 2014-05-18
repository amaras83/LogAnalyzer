/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

import scala.collection.mutable.HashMap
import scala.util.matching.Regex

/**
 * Analyzer trait matches predefined tokens against
 * the log line. Analyzer also provide output information
 * for the tokens they match against
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  String cat The category name
 * @param  Regex pt The regex pattern
 * @param  String msg The output message
 */
class SimpleAnalyzer(cat: String, pt: Regex, msg: String) extends Analyzer {
    /**
     * Key value map for storing message and occurrences
     *
     * @since  1.0
     * @access public
     * @var    HashMap[String,Int]
     */
    private val _messages = new HashMap[String,Int]
    
    /**
     * {@inheritdoc}
     */
    def isMatch(line: String): Boolean = pt findFirstIn line match {
        case Some(pt(text)) => {
            _messages.put(text, _messages.getOrElse(text, 0) + 1)
            true
        }
        case None => false
    }
    
    /**
     * {@inheritdoc}
     */
    def category: String = cat
    
    /**
     * {@inheritdoc}
     */
    def message: String = {
        var result: String = ""
        _messages.foreach{ message =>
            val extracted = message._1
            val count = message._2
            
            result += msg.format(extracted, count)
        }
        result
    }
    
    /**
     * {@inheritdoc}
     */
    def hits: Int = _messages.foldLeft(0)((res,x) => res + x._2)
}
