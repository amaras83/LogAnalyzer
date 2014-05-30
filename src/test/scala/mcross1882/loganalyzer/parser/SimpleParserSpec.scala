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

    "parseLine(...)" should "store a matched regex line" in {
        val parser = buildParser   
        parser.parseLine("a red fox jumped over the fence", List.empty[String])
        
        assert("sample_test\nThe red fox jumped: 1\n\n" equals parser.results)
    }
    
    it should "not store an unmatched regex line" in {
        val parser = buildParser
        parser.parseLine("the red fox ran through the woods", List.empty[String])
        
        assert("sample_test\n\n" equals parser.results)
    }
    
    it should "filter on a timestamp if one is present within the line" in {
        val parser = buildTimestampParser
       
        parser.parseLine("[2014-05-01 12:00:00] Some Debug info...", List("2014-05-01"))
        
        assert("Dates Analyzed\n2014-05-01: 1\n\n" equals parser.results)
    }
    
    it should "ignore lines that do not match the timestamp regex" in {
        val parser = buildTimestampParser
        
        parser.parseLine("[2014-05-01 12:00:00] Some Debug info...", List("2014-05-02"))
        
        assert("Dates Analyzed\n\n" equals parser.results)
    }
    
    "results" should "return the results of any lines that matched an analyzer" in {
        val parser = buildParser
        
        parser.parseLine("a red fox jumped over the fence", List.empty[String])
        parser.parseLine("a red fox ran through the woods", List.empty[String])
        
        assert("sample_test\nThe red fox ran: 1\nThe red fox jumped: 1\n\n" equals parser.results)
    }
    
    protected def buildTimestampParser: Parser = {
        val datePattern = new Regex("""\[(\d+-\d+-\d+) \d+\:\d+\:\d+\]""", "timestamp")
        
        new SimpleParser("timestamp_parser",
            List (
                new SimpleAnalyzer("timestamp", datePattern, "$timestamp"),
                new SimpleAnalyzer("Dates Analyzed", datePattern, "$timestamp")
            )
        )
    }
    
    protected def buildAnalyzers = List(new SimpleAnalyzer("sample_test", new Regex("""a red fox (\w+)""", "action"), "The red fox $action"))
    
    protected def buildParser = new SimpleParser("sample_parser", buildAnalyzers)
}
    