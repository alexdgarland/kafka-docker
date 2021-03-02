
data class MyInnerData(
    var innerId: Int? = null,
    var innerName: String = ""
) {
    constructor(i: Int): this(i, "My inner data with ID $i")
}

data class MyData(
    var id: Int? = null,
    var name: String = "",
    var inners: List<MyInnerData> = emptyList()
) {
    constructor(i: Int): this(i, "My data with ID $i", listOf(MyInnerData(i + 1), MyInnerData(i + 2)))
}
