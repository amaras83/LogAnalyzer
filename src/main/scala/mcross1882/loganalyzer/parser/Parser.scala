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
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
trait Parser {
    /**
     * Parses any related logfiles
     *
     * @since  1.0
     * @param  dates the dates to filter on (if they exist in the line)
     * @return Unit
     */
    def parseFiles(dates: List[String]): Unit
    
    /**
     * Return the aggregated log results as a single string
     *
     * @since  1.0
     * @return String
     */
    def results: String
    
    /**
     * Returns the name of the parser
     *
     * @since  1.0
     * @return String
     */
    def name: String
}
