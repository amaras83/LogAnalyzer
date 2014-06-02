/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.analyzer

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import scala.xml.{NodeSeq, XML}

/**
 * AnalyzerFactory creates a list of
 * analyzers from a external configuration file
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
object AnalyzerFactory {
    /**
     * A list buffer for appending analyzers to
     *
     * @since 1.0
     */
    private val _analyzerBuffer = new ListBuffer[Analyzer]
    
    /**
     * Create an analyzer from a string name
     *
     * @since  1.0
     * @param  anType the analyzer type
     * @param  category the analyzer category
     * @param  pattern the pattern to match against the log line
     * @param  message the output message
     * @return Analyzer
     */
    def createFromName(anType: String, category: String, pattern: Regex, message: String): Analyzer = {
        anType match {
            case _ => new SimpleAnalyzer(category, pattern, message)
        }
    }

    /**
     * Creates a list of analyzers from an XML file
     *
     * @since  1.0
     * @param  filename the xml file to load
     * @return List[Analyzer]
     */
    def createFromXml(filename: String): List[Analyzer] = 
        readAnalyzersFromXml(XML.loadFile(filename))
    
    /**
     * Reads all analyzer elements into the list buffer
     *
     * @since  1.0
     * @param  root the xml root containing the analyzer elements
     * @return a list of analyzer from the xml document
     */
    protected def readAnalyzersFromXml(root: NodeSeq): List[Analyzer] = {
        _analyzerBuffer.clear
        (root \ "analyzer").foreach{ analyzer =>
            val meta = readXmlMetaData(analyzer)
            appendAnalyzerToBuffer(meta)
        }
        _analyzerBuffer.toList
    }
    
    /**
     * Generates a AnalyzerXmlMeta definition from the xml document
     *
     * @since  1.0
     * @param  leaf the xml node containing the analyzer attributes
     * @return meta set containing attrbutes values from the xml document
     */
    protected def readXmlMetaData(leaf: NodeSeq): AnalyzerXmlMeta = {
        val analyzerType = (leaf \ "@type").text
        
        new AnalyzerXmlMeta(
            extractAnalyzerType(leaf)
            , (leaf \ "@category").text
            , (leaf \ "@regex").text
            , leaf.text.trim
            , (leaf \ "@vars").text.split('|').toList
        )
    }
        
    /**
     * Appends a single analyzer to the list buffer
     *
     * @since 1.0
     * @param meta the AnalyzerXmlMeta to construct the new analyzer with
     */
    protected def appendAnalyzerToBuffer(meta: AnalyzerXmlMeta): Unit = {
        _analyzerBuffer.append(createFromName(
            meta.analyzerType 
            , meta.category
            , new Regex(meta.regexPattern, meta.regexArgs: _*)
            , meta.outputMessage))
    }
    
    /**
     * Extracts the analyzer type from an xml node
     *
     * @since  1.0
     * @param  leaf the xml node to extract an analyzer type from
     * @return the analyzer type as a string
     */
    protected def extractAnalyzerType(leaf: NodeSeq): String = {
        var analyzerType = (leaf \ "@type").text
        if (analyzerType.isEmpty) {
            analyzerType = "SimpleAnalyzer"
        }
        analyzerType
    }
    
    /**
     * Lazy value object to avoid passing large amounts of parameters around
     *
     * @since 1.0
     */
    protected case class AnalyzerXmlMeta(
        analyzerType: String,
        category: String,
        regexPattern: String,
        outputMessage: String,
        regexArgs: List[String])
}
