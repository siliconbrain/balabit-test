object Program {
  def main(args: Array[String]): Unit = {
    val calcByDistance = new NearestAirportsByDistance("2008.csv")
    val calcByAvgFlightTime = new NearestAirportsByAvgFlightTime("2008.csv")

    println("ATL TOP5 nearest airports")
    println(calcByDistance.fromAirport("ATL"))
    println(calcByAvgFlightTime.fromAirport("ATL"))

    println("EWR TOP10 nearest airports")
    println(calcByDistance.fromAirport("EWR", 10))
    println(calcByAvgFlightTime.fromAirport("EWR", 10))
  }
}
