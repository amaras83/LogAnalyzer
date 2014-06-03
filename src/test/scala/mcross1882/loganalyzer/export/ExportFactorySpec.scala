/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.export

import java.io.FileOutputStream
import mcross1882.loganalyzer.export.ExportFactory
import mcross1882.loganalyzer.test.DefaultTestSuite
import scala.io.Source
import scala.xml.XML

class ExportFactorySpec extends DefaultTestSuite {

    val toEmail = ""
    
    val xmlFile = "src/test/resources/fixtures/export_factory_node_seq.xml"
    
    "createFromNodeSeq" should "return a list of export defined from an XML file" in {
        val root = XML.loadFile(xmlFile)
        
        val exports = ExportFactory.createFromNodeSeq(root)
        
        assert(1 == exports.length)
        assert("FileExport" equals exports.head.getClass.getSimpleName)
    }
    
    "createEmailExport" should "return a EmailExport class ready to an email" in {
        val root = XML.loadFile(xmlFile)
        
        if (!toEmail.isEmpty) {
            val export = ExportFactory.createEmailExport("gmail", toEmail, toEmail, "Test Subject")
            export.send("This is some test content")
        }
    }
}