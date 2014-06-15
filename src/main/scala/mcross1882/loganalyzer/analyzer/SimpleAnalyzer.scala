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
import scala.util.matching.Regex.Match

/**
 * SimpleAnalyzer uses a regex pattern and a formatted
 * message to produce a condensed log result
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  n the analyzer name
 * @param  cat The category name
 * @param  pt The regex pattern
 * @param  msg The output message
 */
class SimpleAnalyzer(n: String, cat: String, pt: Regex, msg: String) extends Analyzer {
    /**
     * Key value map for storing message and occurrences
     *
     * @since  1.0
     */
    private val _messages = new HashMap[String,Int]
    
    /**
     * {@inheritdoc}
     */
    def isMatch(line: String): Boolean = {
        pt findFirstMatchIn line match {
            case Some(text) => {
                val formattedLine = replaceMessageVariables(text)
                _messages.put(formattedLine, _messages.getOrElse(formattedLine, 0) + 1)
                true
            }
            case None => false
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def name: String = n
    
    /**
     * {@inheritdoc}
     */
    def category: String = cat
    
    /**
     * {@inheritdoc}
     */
    def message: String = {
        val builder = new StringBuilder
        for  ((message, count) <- _messages) {
            builder.append(f"$message%s: $count%d\n")
        }
        builder.toString
    }
    
    /**
     * {@inheritdoc}
     */
    def hits: Int = _messages.foldLeft(0)((res,x) => res + x._2)
        
    /**
     * Replaces all the variables in a given message with their
     * respective values. (e.g. timestamp to 2014-05-01)
     *
     * @since  1.0
     * @param  text a regex match containing groups of variable names
     * @return a formatted string with values instead of variables
     */
    protected def replaceMessageVariables(text: Match): String = {
        var formattedLine = msg
        for (name <- text.groupNames if !name.isEmpty && formattedLine.contains("$" + name)) {
            formattedLine = formattedLine.replace("$" + name, text.group(name))
        }
        formattedLine
    }
}
