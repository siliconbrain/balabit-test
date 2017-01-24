import scala.collection.mutable.{HashMap, SortedSet, TreeSet}

class NearestAirportsByDistance(csvFile: String) extends NearestAirports {
  private val origins: Map[String, List[String]] = {
    val ordering = Ordering.by[(Int, String), Int](_._1)

    io.Source.fromFile(csvFile)
      .getLines()
      .drop(1) // ignore header
      .map(line => new Record(line.split(',')))
      .foldLeft(new HashMap[String, SortedSet[(Int, String)]])(
        (origins, record) => {
          val destinations = origins.getOrElseUpdate(record.origin, new TreeSet()(ordering))
          destinations += ((record.distance, record.destination))
          origins
        })
      .map { case (key, set) => key -> set.map(_._2).toList }(collection.breakOut)
  }

  def fromAirport(airport: String, limit: Int): List[String] = origins.getOrElse(airport, List.empty).take(limit)
}
