/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

import javax.mail.Transport
import javax.mail.internet.MimeMessage

/**
 * Email exports store meta data about a given recipient and sender.
 * They also send messages through the email service session
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
class EmailExport(transport: Transport, emailMessage: MimeMessage) extends Export {        
    /**
     * {@inheritdoc}
     */
    def send(message: String): Unit = {
        emailMessage.setText(message)
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients)
    }
}
