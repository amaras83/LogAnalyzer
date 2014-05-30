/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.analyzer

import scala.util.matching.Regex
import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.test.DefaultTestSuite

class SimpleAnalyzerSpec extends DefaultTestSuite {
    
    "isMatch(...)" should "return true if the regex matches against the line" in {
        val analyzer = buildAnalyzer
        analyzer.isMatch("a red fox jumped") should be(true)
    }
    
    it should "return false if the regex does not match the line" in {
        val analyzer = buildAnalyzer
        analyzer.isMatch("the big brown bear") should be(false)
    }
    
    "category" should "equal the value that was passed into the constructor" in {
        val analyzer = buildAnalyzer
        analyzer.category should be("sample_test")
    }
    
    "message" should "display a formatted output message" in {
        val analyzer = buildAnalyzer
        
        analyzer.isMatch("a red fox jumped over the fence")
        analyzer.isMatch("a red fox ran across the field")
        analyzer.message should be (
            "The red fox ran: 1\n"
          + "The red fox jumped: 1\n"
        )
    }
    
    "hits" should "display the number of matches that happened" in {
        val analyzer = buildAnalyzer
        
        analyzer.isMatch("a red fox jumped over the fence")
        analyzer.isMatch("a red fox ran across the field")
        analyzer.hits should be(2)
    }
    
    protected def buildAnalyzer: SimpleAnalyzer = 
        new SimpleAnalyzer("sample_test", new Regex("""a red fox (\w+)""", "action"), "The red fox $action")
}
