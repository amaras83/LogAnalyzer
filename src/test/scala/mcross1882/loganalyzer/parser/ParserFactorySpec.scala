/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test.parser

import mcross1882.loganalyzer.analyzer.SimpleAnalyzer
import mcross1882.loganalyzer.parser.ParserFactory
import mcross1882.loganalyzer.test.DefaultTestSuite

class ParserFactorySpec extends DefaultTestSuite {
    
    "createFromName(...)" should "should return a SimpleParser if a invalid type is passed" in {
        val parser = ParserFactory.createFromName("badType", "sample_test", buildAnalyzers)
        parser.name should be("sample_test")
    }
    
    "createFromXML(...)" should "return a list of parsers from an external XML file" in {
        val parser = ParserFactory.createFromXml("src/test/resources/parsers.config", buildAnalyzers)
        parser(0).name should be("DemoParser")
    }
    
    protected def buildAnalyzers = List(
        new SimpleAnalyzer("sample_test", """some regex""".r, "the output message")
    )
}
