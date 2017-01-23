trait NearestAirports {
  def fromAirport(airport: String, limit: Int = 5): List[String]
}
