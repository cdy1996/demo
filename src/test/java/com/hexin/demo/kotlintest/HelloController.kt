//package kotlintest
//
//import org.springframework.stereotype.Controller
//import org.springframework.ui.Model
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.ResponseBody
//
///**
// * Created by viruser on 2018/7/31.
// */
//@Controller
//open class HelloController (val helloService : HelloService){
//
//    @RequestMapping("/")
//    @ResponseBody
//    fun hello(model: Model): String {
//        return helloService.getUser().toString()
//
//    }
//}