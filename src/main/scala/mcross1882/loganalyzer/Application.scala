/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer

import java.io.File
import java.util.Scanner
import mcross1882.loganalyzer.analyzer.{Analyzer, AnalyzerFactory}
import mcross1882.loganalyzer.parser.{Parser, ParserFactory}

/**
 * Main Entry point
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object Application {
    /**
     * Analyzers that will be used on the log input
     *
     * @since  1.0
     * @access protected
     * @var    List[Analyzer]
     */
    protected val _analyzers = AnalyzerFactory.createFromXml
    
    /**
     * Parsers that will be used on the log output
     * and stdout
     *
     * @since  1.0
     * @access protected
     * @var    List[Parser]
     */
    protected val _parsers = ParserFactory.createFromXml(_analyzers)
    
    /**
     * Process a log line
     *
     * @since  1.0
     * @access protected
     * @param  String line the line to be parsed
     * @return Unit
     */
    protected def processLine(line: String): Unit =
        for (parser <- _parsers) parser.parseLine(line)
    
    /**
     * Print the results of all the parsers
     *
     * @since  1.0
     * @access protected
     * @return Unit
     */
    protected def printAllParsers: Unit =
        for (parser <- _parsers) parser.printResults
    
    /**
     * Program start
     *
     * @since  1.0
     * @access public
     * @param  Array[String] args the program arguments
     * @return Unit
     */
    def main(args: Array[String]) {
        val reader = new Scanner(new File("sample.log"))
        try {
            while (reader.hasNext) {
                processLine(reader.nextLine)
            }
        } finally {
            reader.close
        }
        printAllParsers
    }
}
