/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

/**
 * Export provides the methods for sending/writing
 * string data to the appropriate output format
 * specified in a Service entity
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
trait Export {
    /**
     * Sends a message to the export entity for writing
     *
     * @since  1.0
     * @param  message the data to write
     */
    def send(message: String): Unit
}
