/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.parser

import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.{Parser, SimpleParser}
import mcross1882.loganalyzer.test.DefaultTestSuite
import scala.util.matching.Regex

class SimpleParserSpec extends DefaultTestSuite {

    "parseFiles" should "iterator through all the files and parse them" in {
        val parser = buildParser
        
        parser.parseFiles(List.empty[String])

        assert("Test Category\nThe red fox ran: 1\nThe red fox jumped: 1\n\n" equals parser.results)
    }
 
    "results" should "return the results of any lines that matched an analyzer" in {
        val parser = buildParser
        
        parser.parseFiles(List.empty[String])
        
        assert("Test Category\nThe red fox ran: 1\nThe red fox jumped: 1\n\n" equals parser.results)
    }
    
    protected def buildTimestampParser: Parser = {
        val datePattern = new Regex("""\[(\d+-\d+-\d+) \d+\:\d+\:\d+\]""", "timestamp")
        
        new SimpleParser("timestamp_parser",
            List("src/test/resources/fixtures/parser-spec.txt"),
            List (
                new SimpleAnalyzer("timestamp", "timestamp", datePattern, "$timestamp"),
                new SimpleAnalyzer("date-analyzer", "Dates Analyzed", datePattern, "$timestamp")
            )
        )
    }
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("test-analyzer", "Test Category", new Regex("""a red fox (\w+)""", "action"), "The red fox $action"))
    
    protected def buildParser = new SimpleParser("sample_parser", List("src/test/resources/fixtures/parser-spec.txt"), buildAnalyzers)
}
    