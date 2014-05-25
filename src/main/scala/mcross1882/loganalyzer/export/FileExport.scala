/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

import java.io.{File, FileOutputStream}

/**
 * File export sends services to results to a local file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class FileExport(filename: String) extends Export {
    /**
     * Output stream pointing to a local file that
     * the program can write to
     *
     * @since  1.0
     */
    protected val _outputStream = new FileOutputStream(new File(filename))
    
    /**
     * {@inheritdoc}
     */
    def send(message: String): Unit = {
        _outputStream.write(message.getBytes)
        _outputStream.flush
    }
}
