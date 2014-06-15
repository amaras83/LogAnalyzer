/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.export

import javax.mail.Transport
import javax.mail.internet.MimeMessage
import mcross1882.loganalyzer.export.EmailExport
import mcross1882.loganalyzer.test.DefaultTestSuite
import org.mockito.Mockito.verify

class EmailExportSpec extends DefaultTestSuite {

    private var _transportMock: Transport = mock[Transport]
    
    private var _messageMock: MimeMessage = mock[MimeMessage]
    
    "send" should "write a message to the transport pipeline" in {
        val export = buildExport
        val content = "The Expected Content"
        
        export.send(content)
        
        verify(_messageMock).setText(content)
        verify(_transportMock).sendMessage(_messageMock, _messageMock.getAllRecipients)
    }
    
    protected def buildExport = new EmailExport(_transportMock, _messageMock)
}