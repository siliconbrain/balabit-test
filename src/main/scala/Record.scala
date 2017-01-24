import scala.util.Try

class Record(data: Array[String]) {
  def actualElapsedTime = Try(data(11).toInt).toOption
  def origin = data(16)
  def destination = data(17)
  def distance = data(18).toInt
}
