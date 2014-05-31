/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.export

import java.io.FileOutputStream
import mcross1882.loganalyzer.export.FileExport
import mcross1882.loganalyzer.test.DefaultTestSuite
import scala.io.Source

class FileExportSpec extends DefaultTestSuite {

    val FixtureFile = "src/test/resources/fixtures/file_export.txt"
    
    "send" should "write a message to the output stream and flush it" in {
        println(getClass.getResource(FixtureFile))
        val export = buildExport
        val message = "The message to write"
        
        export.send(message)
        
        val lines = Source.fromFile(FixtureFile).getLines
        
        assert(message equals lines.next)
    }
    
    it should "not write a empty message" in {
        val export = buildExport
        val message = ""
        
        export.send(message)
        
        val lines = Source.fromFile(FixtureFile).getLines
        
        assert(lines.isEmpty)
    }
    
    protected def buildExport = new FileExport(new FileOutputStream(FixtureFile))
}