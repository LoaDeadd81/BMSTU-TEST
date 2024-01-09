import exposed.ExposedBench


fun main(args: Array<String>) {
    val num = 1000

    ExposedBench.connect()
    val expInsert = ExposedBench.insert(num)
    val expSearchId = ExposedBench.searchById(num)
    val expSearchName = ExposedBench.searchByName(num)
    val expDelete = ExposedBench.delete(num)

    println("======= Exposed =======")
    println("Insert: $expInsert")
    println("FindById: $expSearchId")
    println("FindByName: $expSearchName")
    println("Delete: $expDelete")

}