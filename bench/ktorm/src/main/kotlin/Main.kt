import ktorm.KtormBench

fun main() {
    val num = 5000

    KtormBench.connect()
    val ktormInsert = KtormBench.insert(num)
    val ktormSearchId = KtormBench.searchById(num)
    val ktormSearchName = KtormBench.searchByName(num)
    val ktormDelete = KtormBench.delete(num)

    println("======= Ktorm =======")
    println("Insert: $ktormInsert")
    println("FindById: $ktormSearchId")
    println("FindByName: $ktormSearchName")
    println("Delete: $ktormDelete")
}