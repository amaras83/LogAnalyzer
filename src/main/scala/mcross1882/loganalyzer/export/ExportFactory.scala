/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.export

import java.io.{File, FileOutputStream}
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, Session, Transport}
import mcross1882.loganalyzer.AutoLoader
import scala.collection.mutable.ListBuffer
import scala.xml.{NodeSeq, XML}

/**
 * Factory for constructing export entities
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object ExportFactory {
    /**
     * Create a export from a XML node sequence
     *
     * @since  1.0
     * @param  nodes a subset of XML nodes containing export elements
     * @return a list of newly create Export entity
     */
    def createFromNodeSeq(nodes: NodeSeq): List[Export] = {
        val buffer = new ListBuffer[Export]
        
        (nodes \ "exports" \ "file").foreach{ file =>
            buffer.append(createFileExport((file \ "@src").text))
        }
        
        (nodes \"exports" \ "email").foreach{ email =>
            buffer.append(createEmailExport(
                (email \ "@name").text
                , (email \ "@to").text
                , (email \ "@from").text
                , (email \ "@subject").text))
        }

        buffer.toList
    }
    
    /**
     * Create a FileExport with an associated FileOutputStream
     *
     * @since  1.0
     * @param  filename the path of the file to open for writing
     * @return a newly constructed FileExport
     */
    def createFileExport(filename: String) = 
        new FileExport(new FileOutputStream(new File(filename)))
     
    /**
     * Creates a EmailExport class ready to transport messages to an SMTPS server
     *
     * @since  1.0
     * @param  config the email configuration name
     * @param  to the recipient
     * @param  from the sender of the email
     * @param  subject the email subject
     * @return a newly constructed EmailExport
     */
    def createEmailExport(config: String, to: String, from: String, subject: String): EmailExport = {
        val session = Session.getDefaultInstance(System.getProperties())
        val transport = loadEmailTransportFromXml(session, config) match {
            case Some(value) => value
            case None => throw new Exception("Failed to load email configuration settings for %s".format(config))
        }
        
        val message = new MimeMessage(session)
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
        message.setFrom(new InternetAddress(from))
        message.setSubject(subject)
        
        new EmailExport(transport, message)
    }
    
    /**
     * Loads the email tranport pipe given an xml definition
     *
     * @since  1.0
     * @param  session the java mail session
     * @param  expectedName the configuration name to load
     * @return Some(transport) if the configuration is found; None otherwise
     */
    protected def loadEmailTransportFromXml(session: Session, expectedName: String): Option[Transport] = {
        var currentName: String = ""
        val root = XML.loadFile(buildEmailConfigFilename(expectedName))
        val transport = session.getTransport("smtps")
        
        (root \ "config").foreach{ config =>
            currentName = (config \ "@name").text
            
            if (currentName equals expectedName) {
                transport.connect(
                    attribute(config, "host")
                    , attribute(config, "port").toInt
                    , attribute(config, "user")
                    , attribute(config, "pass"))
                return Some(transport)
            }
        }
        None
    }
    
    /**
     * Builds an relative path to the email configuration file
     *
     * @since  1.0
     * @param  name the configuration name
     * @return the relative path to the configuration file
     */
    protected def buildEmailConfigFilename(name: String): String = 
        "%s%semail%s%s.config".format(AutoLoader.HomeDirectory, AutoLoader.Separator, AutoLoader.Separator, name)
    
    /**
     * Extracts an node attribute from a given node sequence
     *
     * @since  1.0
     * @param  root the xml root
     * @param  attr the attribute name
     * @return the attribute value as a string
     */
    protected def attribute(root: NodeSeq, attr: String): String = 
        (root \ "@%s".format(attr)).text
}
