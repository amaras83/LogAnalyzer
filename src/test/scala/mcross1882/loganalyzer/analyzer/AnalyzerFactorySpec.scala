/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.analyzer

import org.scalatest._
import mcross1882.loganalyzer.analyzer.AnalyzerFactory

class AnalyzerFactorySpec extends FlatSpec with Matchers {
    
    "createFromName(...)" should "should return a SimpleAnalyzer if a invalid type is passed" in {
        val analyzer = AnalyzerFactory.createFromName("badType", "sample_test", """some regex""".r, "the output message")
        
        analyzer.category should be("sample_test")
        analyzer.hits should be(0)
    }
    
    "createFromXML(...)" should "return a list of analyzers from an external XML file" in {
        val analyzers = AnalyzerFactory.createFromXml("conf/dist/analyzers/demo.config")
        
        analyzers.length should be(3)
        analyzers(0).category should be("Emergency")
        analyzers(1).category should be("FailedLogin")
        analyzers(2).category should be("DebuggingNoise")
    }
}
