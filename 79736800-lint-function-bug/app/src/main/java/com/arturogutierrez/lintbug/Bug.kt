package com.arturogutierrez.lintbug

interface Transformer<Input, Output> {

    val transform: (Input) -> Output
}

class StringTransformer : Transformer<String, Int> {

    override val transform: (String) -> Int
        get() = { it.hashCode() }
}

class Example<Input, Output>(
        private val transformer: Transformer<Input, Output>
) {

    fun run(input: Input) {
        val itemId = transformer.transform(input)
        println(itemId)
    }
}