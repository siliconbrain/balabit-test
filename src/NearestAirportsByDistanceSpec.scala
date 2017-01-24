import java.io.{File, PrintWriter}
import org.scalatest._

class NearestAirportsByDistanceSpec extends FlatSpec with Matchers {
  import NearestAirportsByDistanceSpec._

  val emptyFile = {
    val file = File.createTempFile(testFilePrefix, "empty")
    file.deleteOnExit()
    file.setReadOnly()
    file
  }

  val testDataFile = {
    val file = File.createTempFile(testFilePrefix, "test-data")
    file.deleteOnExit()
    val pw = new PrintWriter(file)
    pw.println("HEADER LINE")
    writeRecord(pw)("AP1", "AP2", 300)
    writeRecord(pw)("AP2", "AP3", 400)
    writeRecord(pw)("AP2", "AP4", 500)
    writeRecord(pw)("AP2", "AP5", 600)
    writeRecord(pw)("AP2", "AP6", 700)
    writeRecord(pw)("AP2", "AP7", 800)
    writeRecord(pw)("AP2", "AP8", 900)
    writeRecord(pw)("AP2", "AP9", 1000)
    pw.flush()
    file.setReadOnly()
    file
  }

  val emptyNearestAirportsByDistanceCalc = new NearestAirportsByDistance(emptyFile.getAbsolutePath)
  val testNearestAirportsByDistanceCalc = new NearestAirportsByDistance(testDataFile.getAbsolutePath)

  "The NearestAirportsByDistance" should "return empty list when there's no data" in {
    emptyNearestAirportsByDistanceCalc.fromAirport("UNK") should be (List.empty)
  }

  it should "return empty list when there's no matching airport" in {
    testNearestAirportsByDistanceCalc.fromAirport("UNK") should be (List.empty)
  }

  it should "return only the specified number of destinations when there are more" in {
    testNearestAirportsByDistanceCalc.fromAirport("AP2", 5).length should be (5)
  }

  it should "return only as many destinations as there are" in {
    testNearestAirportsByDistanceCalc.fromAirport("AP1", 100).length should be (1)
    testNearestAirportsByDistanceCalc.fromAirport("AP2", 100).length should be (7)
  }

  it should "return destinations in ascending order of distance" in {
    testNearestAirportsByDistanceCalc.fromAirport("AP2") should be (List("AP3", "AP4", "AP5", "AP6", "AP7"))
  }
}

object NearestAirportsByDistanceSpec {
  val testFilePrefix = "balabit-test-NearestAirportsByDistance"

  def writeRecord(printWriter: PrintWriter)(origin: String, destination: String, distance: Int = 0) =
    printWriter.println(s",,,,,,,,,,,,,,,,${origin},${destination},${distance.toString},,,,,,,,,,")
}
