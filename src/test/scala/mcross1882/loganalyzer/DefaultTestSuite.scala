/**
 * Log analyzer and summary builder written in Scala built for JVM projects
 *
 * @package   LogAnalyzer
 * @copyright Apache V2 License (see LICENSE)
 * @url       https://github.com/mcross1882/LogAnalyzer
 */
package mcross1882.loganalyzer.test

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.mock.MockitoSugar
import org.scalamock.scalatest.MockFactory

/**
 * A empty trait that combines our test specs and mock factory
 *
 * @since  1.0
 * @author Matthew Cross <blacklightgfx@gmail.com>
 */
trait DefaultTestSuite extends FlatSpec
    with Matchers 
    with MockFactory
    with MockitoSugar
    
