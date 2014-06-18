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
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.xml.XML

/**
 * SimpleParser allows you to define a parser
 * programatically by providing helper methods
 * for setting it up
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 * @param  n the name to reference this parser by
 * @param  files to parse
 * @param  analyzersto use on the specified files
 */
class SimpleParser(n: String, files: List[String], analyzers: List[Analyzer]) extends Parser {
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
    protected val _timestamps = analyzers.filter(x => "timestamp".equals(x.name))

    /**
     * {@inheritdoc}
     */
    def parseFiles(dates: List[String]) {
        for (file <- files) {
            parseLinesFromFiles(file, dates)
        }
    }
    
    /**
     * {@inheritdoc}
     */
    def results: String = {
        val builder = new StringBuilder
        var category = ""
        val groups = groupAnalyzersByCategory
        
        for (analyzer <- analyzers) {
            category = analyzer.category
            if (groups.contains(category)) {
                appendAnalyzerSubset(builder, category, groups(category))
            }
        }
        builder.toString
    }
    
    /**
     * {@inheritdoc}
     */
    def name: String = n
            
    /**
     * Opens up a source files and parses it line-by-line
     *
     * @since 1.0
     * @param dates to filter on
     */
    protected def parseLinesFromFiles(filename: String, dates: List[String]) {
        try {
            Source.fromFile(filename).getLines.foreach{ line =>
                parseLine(line, dates)
            }
        } catch {
            case e: Exception => println("An error occurred while reading %s. (%s)".format(filename, e.getMessage))
        }
    }
    
    /**
     * Parses a single line from the files
     *
     * @since 1.0
     * @param line to parse
     * @param dates to filter on
     */
    protected def parseLine(line: String, dates: List[String]): Unit = {
        if (dates.isEmpty || isTimestampInRange(line, dates)) {
            storeAnalyzerMatches(line)
        }
    }
    
    /**
     * Determines if the line contains a valid timestamp. If the line
     * does contain a timestamp it will be validated against the dates array
     *
     * @since  1.0
     * @param  line the line to be parsed
     * @param  dates valid dates in the range
     * @return true if the timestamp is in range; false otherwise
     */
    protected def isTimestampInRange(line: String, dates: List[String]): Boolean = {
        var currentDate = ""
        for (timestamp <- _timestamps if timestamp.isMatch(line)) {
            currentDate = extractTextFromMessage(timestamp)
            if (!dates.exists(_.equals(currentDate))) {
                return false
            }
        }
        true
    }
    
    /**
     * Parses the line argument and increments the count for any
     * analyzer category contained within the line
     * 
     * @since  1.0
     * @param  line the line to be parsed
     */
    protected def storeAnalyzerMatches(line: String): Unit = {
        for (analyzer <- analyzers if analyzer.isMatch(line)) {
            _records.put(analyzer.category, _records.getOrElse(analyzer.category, 0) + 1)
        }
    }
    
    /**
     * Extracts the text of a analyzer message
     *
     * @since  1.0
     * @param  analyzer the analyzer with a message to split
     * @return the extracted text trimmed
     */
    protected def extractTextFromMessage(analyzer: Analyzer): String = {
        val fields = analyzer.message.split(":")
        
        if (!fields.isEmpty) fields.head.trim else ""
    }
        
    /**
     * Groups analyzers by their category producing while ignoring
     * any timestamp analyzers
     *
     * @since  1.0
     * @return a list of analyzers grouped by category
     */
    protected def groupAnalyzersByCategory: Map[String, List[Analyzer]] =
        analyzers.filter(!_.category.equals("timestamp")).groupBy(_.category)
    
    /**
     * Appends all messages from analyzer subset into builder
     *
     * @since 1.0
     * @param builder the string builder to append text too
     * @param category the analyzer category
     * @param subset of filtered analyzers
     */
    protected def appendAnalyzerSubset(builder: StringBuilder, category: String, subset: List[Analyzer]): Unit = {
        builder.append("%s\n".format(category))
        for (analyzer <- subset if _records.contains(analyzer.category)) {
            builder.append(analyzer.message)
        }
        builder.append("\n")
    }
}
