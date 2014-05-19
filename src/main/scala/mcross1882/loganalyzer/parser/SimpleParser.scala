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
     */
    protected val _records = new HashMap[String,Int]
    
    /**
     * The base timestamp format to parse out of the log line
     *
     * @since  1.0
     */
    protected val _timestamps = analyzers.filter(x => "timestamp".equals(x.category))

    /**
     * {@inheritdoc}
     */
    def parseLine(line: String, dates: List[String]): Unit = {
        if (_timestamps.length > 0) {
            val currentDate = _timestamps.head.message.split(':').head
            if (_timestamps.head.isMatch(line) && !dates.exists(_.equals(currentDate))) {
                return
            }
        }
        
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
        for ((category, subset) <- analyzers.groupBy(_.category) if !category.equals("timestamp")) {
            println(category)
            for (analyzer <- subset if _records.contains(analyzer.category)) {
                print("%s".format(analyzer.message))
            }
            println
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def name: String = n
}
