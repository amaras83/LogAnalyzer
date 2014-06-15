/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.export

import mcross1882.loganalyzer.export.EmailExport
import mcross1882.loganalyzer.test.DefaultTestSuite

class EmailExportSpec extends DefaultTestSuite {

    "send" should "write a message to the transport pipeline" in {
    
    }
    
    protected def buildExport = new FileExport(new FileOutputStream(FixtureFile))
}