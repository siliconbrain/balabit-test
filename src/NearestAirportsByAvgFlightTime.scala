import scala.collection.mutable.{HashMap, SortedSet, TreeSet}

class NearestAirportsByAvgFlightTime(csvFile: String) extends NearestAirports {
  private val origins: Map[String, List[String]] = {
    val ordering = Ordering.by[(Double, String), Double](_._1)

    io.Source.fromFile(csvFile)
      .getLines()
      .drop(1) // ignore header
      .map(line => new Record(line.split(',')))
      .foldLeft(new HashMap[(String, String), (Int, Int)])(
        (routes, record) => {
          val route = (record.origin, record.destination)
          val times = routes.getOrElse(route, (0, 0))
          routes.update(route, record.actualElapsedTime match {
            case Some(actualElapsedTime) => (times._1 + actualElapsedTime, times._2 + 1)
            case None => times
          })
          routes
        }
      )
      .foldLeft(new HashMap[String, SortedSet[(Double, String)]])(
        (origins, route) => {
          val destinations = origins.getOrElseUpdate(route._1._1, new TreeSet()(ordering))
          destinations += ((route._2._1.toDouble / route._2._2, route._1._2))
          origins
        })
      .map { case (key, set) => key -> set.map(_._2).toList }(collection.breakOut)
  }

  def fromAirport(airport: String, limit: Int): List[String] = origins.getOrElse(airport, List.empty).take(limit)
}
