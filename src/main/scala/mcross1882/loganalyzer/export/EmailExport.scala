/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

/**
 * Email export sends an email with the service results as
 * the message body
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class EmailExport(to: String, from: String, subject: String) extends Export {    
    /**
     * {@inheritdoc}
     */
    def send(message: String): Unit = {
        // xxx unimplemented
    }
}
