//package kotlintest
//
///**
// * Created by viruser on 2018/7/31.
// */
//fun main(args: Array<String>) {    // 包级可见的函数，接受一个字符串数组作为参数
////    println("Hello World!")         // 分号可以省略
//////    Greeter("World!").greet()
////
////    print("循环输出：")
////    for (i in 1..4) print(i) // 输出“1234”
////    println("\n----------------")
////    print("设置步长：")
////    for (i in 1..4 step 2) print(i) // 输出“13”
////    println("\n----------------")
////    print("使用 downTo：")
////    for (i in 4 downTo 1 step 2) print(i) // 输出“42”
////    println("\n----------------")
////    print("使用 until：")
////    // 使用 until 函数排除结束元素
////    for (i in 1 until 4) {   // i in [1, 4) 排除了 4
////        print(i)
////    }
////    println("\n----------------")
////
////
////    val a: Int = 10000
////    println(a === a) // true，值相等，对象地址相等
////
////    //经过了装箱，创建了两个不同的对象
////    val boxedA: Int? = a
////    val anotherBoxedA: Int? = a
////
////    //虽然经过了装箱，但是值是相等的，都是10000
////    println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
////    println(boxedA == anotherBoxedA) // true，值相等
////
////
////
////    var x = 0
////    when (x) {
////        0, 1 -> println("x == 0 or x == 1")
////        else -> println("otherwise")
////    }
////
////    when (x) {
////        1 -> println("x == 1")
////        2 -> println("x == 2")
////        else -> { // 注意这个块
////            println("x 不是 1 ，也不是 2")
////        }
////    }
////
////    when (x) {
////        in 0..10 -> println("x 在该区间范围内")
////        else -> println("x 不在该区间范围内")
////    }
//
////    val items = setOf("apple", "banana", "kiwi")
////    when {
////        "orange" in items -> println("juicy")
////        "apple" in items -> println("apple is fine too")
////    }
//
//    val items = listOf("apple", "banana", "kiwi")
//    for (item in items) {
//        println(item)
//    }
//
//    for (index in items.indices) {
//        println("item at $index is ${items[index]}")
//    }
//
//
//    loop@ for (i in 1..100) {
//        for (j in 1..100) {
//            if (j==2) break@loop
//        }
//    }
//
//}
//
//class Person {
//
//    var lastName: String = "zhang"
//        get() = field.toUpperCase()   // 将变量赋值后转换为大写
//        set
//
//    var no: Int = 100
//        get() = field                // 后端变量
//        set(value) {
//            if (value < 10) {       // 如果传入的值小于 10 返回该值
//                field = value
//            } else {
//                field = -1         // 如果传入的值大于等于 10 返回 -1
//            }
//        }
//
//    var heiht: Float = 145.4f
//        private set
//}
//
