/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

import java.io.OutputStream

/**
 * File export sends services to results to a local file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class FileExport(stream: OutputStream) extends Export {
    /**
     * {@inheritdoc}
     */
    def send(message: String): Unit = {
        stream.write(message.getBytes)
        stream.flush
    }
}
