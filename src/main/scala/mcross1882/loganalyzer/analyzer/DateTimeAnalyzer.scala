/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

import org.joda.time.format.DateTimeFormat

/**
 * DateTimeAnalyzer uses the Joda time parser
 * instead of a standard regex parser for easier
 * date parsing. This allows for date normalization
 * and makes it possible to filter on a different
 * date format than what was in the file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  pattern the Joda-time datetime pattern
 */
class DateTimeAnalyzer(pattern: String) extends Analyzer {
    /**
     * Key value map for storing message and occurrences
     *
     * @since  1.0
     */
    private var _hitCount: Int = 0
    
    /**
     * DateTimeFormatter for matching dates and times from a log line
     *
     * @since  1.0
     */
    private val _formatter = DateTimeFormat.forPattern(pattern)
    
    /**
     * {@inheritdoc}
     */
    def isMatch(line: String): Boolean = {
        try {
            _formatter.parseDateTime(line)
            _hitCount += 1
            true
        } catch {
            case e: Exception => false
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def category: String = "timestamp"
    
    /**
     * {@inheritdoc}
     */
    def message: String = ""
    
    /**
     * {@inheritdoc}
     */
    def hits: Int = _hitCount
}
