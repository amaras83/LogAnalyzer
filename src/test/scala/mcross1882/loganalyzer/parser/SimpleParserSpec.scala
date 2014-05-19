/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.parser

import org.scalatest._
import scala.util.matching.Regex
import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.SimpleParser

class SimpleParserSpec extends FlatSpec with Matchers {

    "parseLine(...)" should "store a matched regex line" in {
        val parser = buildParser   
        parser.parseLine("a red fox jumped over the fence")
    }
    
    it should "not store an unmatched regex line" in {
        val parser = buildParser
        parser.parseLine("the red fox ran through the woods")
    }
    
    "printResults" should "print the results of any lines that matched an analyzer" in {
        val parser = buildParser
        
        parser.parseLine("a red fox jumped over the fence")
        parser.parseLine("a red fox ran through the woods")
        
        parser.printResults
    }
    
    protected def buildAnalyzers = List(new SimpleAnalyzer("sample_test", new Regex("""a red fox (\w+)""", "action"), "The red fox $action"))
    
    protected def buildParser = new SimpleParser("sample_parser", buildAnalyzers)
}