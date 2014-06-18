/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.parser

import mcross1882.loganalyzer.analyzer.{Analyzer, SimpleAnalyzer}
import mcross1882.loganalyzer.parser.ParserFactory
import mcross1882.loganalyzer.test.FactoryTestSuite
import scala.util.matching.Regex

class ParserFactorySpec extends FactoryTestSuite("src/test/resources/conf/parsers") {
    
    "createFromName" should "should return a SimpleParser if a invalid type is passed" in {
        val parser = ParserFactory.createFromName("badType", "sample_test", List("src/test/resources/fixtures/parser-spec.txt"), buildAnalyzers)
        parser.name should be("sample_test")
    }
    
    it should "throw an exception if a null parserType argument is passed in" in {
        intercept[IllegalArgumentException] {
            val parser = ParserFactory.createFromName(null, "sample_test", List("src/test/resources/fixtures/parser-spec.txt"), buildAnalyzers)
        }
    }
    
    it should "fail if a null name argument is passed in" in {
        intercept[IllegalArgumentException] {
            val parser = ParserFactory.createFromName("badType", null, List.empty[String], buildAnalyzers)
        }
    }
    
    "createFromXML" should "return a list of parsers from an external XML file" in {
        val parser = ParserFactory.createFromXml(loadTestFile("default"), buildAnalyzers)
        parser(0).name should be("DemoParser")
    }
    
    it should "return an empty list of parsers if none are defined in the XML file" in {
        val parser = ParserFactory.createFromXml(loadTestFile("empty"), buildAnalyzers)
        assert(parser.isEmpty)
    }
    
    it should "throw an exception if the XML is malformed or corrupted" in {
        intercept[org.xml.sax.SAXParseException] {
            val parser = ParserFactory.createFromXml(loadTestFile("broken"), buildAnalyzers)
        }
    }
    
    it should "throw an exception if no analyzers are passed in" in {
        intercept[IllegalArgumentException] {
            val parser = ParserFactory.createFromXml(loadTestFile("default"), List.empty[Analyzer])
        }
    }
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("test-analyzer", "Test Category", new Regex("""a red fox (\w+)""", "action"), "The red fox $action")
    )
}
