/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.parser

import mcross1882.loganalyzer.analyzer.Analyzer
import scala.collection.mutable.HashMap

/**
 * SimpleParser allows you to define a parser
 * programatically by providing helper methods
 * for setting it up
 *
 * @since  1.0
 * @access public
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  String n the name to reference this parser by
 * @param  List[Analyzer] analyzers
 */
class SimpleParser(n: String, analyzers: List[Analyzer]) extends Parser {
    /**
     * Temporary storage maintaining the number of occurrences
     * a given log pattern appears
     *
     * @since  1.0
     * @access protected
     * @var    HashMap[String,Int]
     */
    protected val _records = new HashMap[String,Int]

    /**
     * {@inheritdoc}
     */
    def parseLine(line: String): Unit = {
        for (analyzer <- analyzers) {
            if (analyzer.isMatch(line)) {
                _records.put(analyzer.category, _records.getOrElse(analyzer.category, 0) + 1)
            }
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def printResults: Unit = {
        for (analyzer <- analyzers) {
            if (_records.contains(analyzer.category)) {
                println("%s\n%s".format(analyzer.category, analyzer.message, analyzer.hits))
            }
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def name: String = n
}
