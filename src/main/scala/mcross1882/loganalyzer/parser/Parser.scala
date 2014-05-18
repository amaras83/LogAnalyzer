/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.parser

/**
 * Parsers parse lines and store meta data related
 * to the log records. After the logs have been
 * consumed printResults is called which should write
 * any output and stats to stdout
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
trait Parser {
    /**
     * Parse a given log line
     *
     * @since  1.0
     * @access public
     * @param  String line
     * @return Unit
     */
    def parseLine(line: String): Unit
    
    /**
     * Print the aggregated log results
     *
     * @since  1.0
     * @access public
     * @return Unit
     */
    def printResults: Unit
}
