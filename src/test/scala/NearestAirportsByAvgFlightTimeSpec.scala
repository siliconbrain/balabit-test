import java.io.{File, PrintWriter}
import org.scalatest._

class NearestAirportsByAvgFlightTimeSpec extends FlatSpec with Matchers {
  import NearestAirportsByAvgFlightTimeSpec._

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
    writeRecord(pw)("AP1", "AP2", Some(60))
    writeRecord(pw)("AP2", "AP3", Some(60))
    writeRecord(pw)("AP2", "AP4", Some(70))
    writeRecord(pw)("AP2", "AP5", Some(80))
    writeRecord(pw)("AP2", "AP6", Some(90))
    writeRecord(pw)("AP2", "AP7", Some(100))
    writeRecord(pw)("AP2", "AP8", Some(110))
    writeRecord(pw)("AP2", "AP9", Some(120))
    writeRecord(pw)("AP2", "AP3", Some(62))
    writeRecord(pw)("AP2", "AP4", Some(72))
    writeRecord(pw)("AP2", "AP5", Some(82))
    writeRecord(pw)("AP2", "AP6", Some(92))
    writeRecord(pw)("AP2", "AP7", Some(102))
    writeRecord(pw)("AP2", "AP8", Some(112))
    writeRecord(pw)("AP2", "AP9", Some(122))
    writeRecord(pw)("AP2", "AP3", Some(67))
    writeRecord(pw)("AP2", "AP4", Some(77))
    writeRecord(pw)("AP2", "AP5", Some(87))
    writeRecord(pw)("AP2", "AP6", Some(97))
    writeRecord(pw)("AP2", "AP7", Some(107))
    writeRecord(pw)("AP2", "AP8", Some(117))
    writeRecord(pw)("AP2", "AP9", Some(127))
    writeRecord(pw)("AP3", "AP4", Some(10))
    writeRecord(pw)("AP3", "AP4", None)
    writeRecord(pw)("AP3", "AP4", Some(30))
    pw.flush()
    file.setReadOnly()
    file
  }

  val emptyNearestAirportsByAvgFlightTimeCalc = new NearestAirportsByAvgFlightTime(emptyFile.getAbsolutePath)
  val testNearestAirportsByAvgFlightTimeCalc = new NearestAirportsByAvgFlightTime(testDataFile.getAbsolutePath)

  "The NearestAirportsByDistance" should "return empty list when there's no data" in {
    emptyNearestAirportsByAvgFlightTimeCalc.fromAirport("UNK") should be (List.empty)
  }

  it should "return empty list when there's no matching airport" in {
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("UNK") should be (List.empty)
  }

  it should "return only the specified number of destinations when there are more" in {
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("AP2", 5).length should be (5)
  }

  it should "return only as many destinations as there are" in {
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("AP1", 100).length should be (1)
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("AP2", 100).length should be (7)
  }

  it should "return destinations in ascending order of distance" in {
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("AP2") should be (List("AP3", "AP4", "AP5", "AP6", "AP7"))
  }

  it should "not fail when some flights' actual flight time is NA" in {
    testNearestAirportsByAvgFlightTimeCalc.fromAirport("AP3") should be (List("AP4"))
  }
}

object NearestAirportsByAvgFlightTimeSpec {
  val testFilePrefix = "balabit-test-NearestAirportsByAvgFlightTime"

  def writeRecord(printWriter: PrintWriter)(origin: String, destination: String, actualFlightTime: Option[Int] = None) =
    printWriter.println(s",,,,,,,,,,,${actualFlightTime.map(_.toString).getOrElse("NA")},,,,,${origin},${destination},,,,,,,,,,,")
}
