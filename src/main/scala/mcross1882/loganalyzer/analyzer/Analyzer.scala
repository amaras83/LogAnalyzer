/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

/**
 * Analyzer trait matches predefined tokens against
 * the log line. Analyzer also provide output information
 * for the tokens they match against
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
trait Analyzer {
    /**
     * Matches all predefined regex patterns against
     * the input line. If any of the regex patterns match
     * the method will return true
     *
     * @since  1.0
     * @param  String line
     * @return Boolean
     */
    def isMatch(line: String): Boolean
    
    /**
     * Returns the analyzer name used to match against parser imports
     *
     * @since  1.0
     * @return String
     */
    def name: String
    
    /**
     * Returns the category name for this analyzer
     *
     * @since  1.0
     * @return String
     */
    def category: String
    
    /**
     * Returns a "pretty message" for this analyzer
     *
     * @since  1.0
     * @return String
     */
    def message: String
    
    /**
     * Returns the number of hits this analyzer received
     *
     * @since  1.0
     * @return Int
     */
    def hits: Int
}
