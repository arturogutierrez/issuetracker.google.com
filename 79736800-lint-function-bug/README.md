# Unexpected failure during lint analysis when invoking a referenced Kotlin function without using invoke

View on [Issue Tracker](https://issuetracker.google.com/issues/79736800).

```
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
```

Getting a lint crash when running `./gradlew lintDebug`:
```
> Task :app:lintDebug FAILED
79736800-lint-function-bug/app: Error: Unexpected failure during lint analysis (this is a bug in lint or one of the libraries it depends on)

Message: Unresolved class: class org.jetbrains.kotlin.descriptors.SourceElement$1
Stack: KotlinReflectionInternalError:KClassImpl.reportUnresolvedClass(KClassImpl.kt:301)←KClassImpl.access$reportUnresolvedClass(KClassImpl.kt:42)←KClassImpl$Data$descripto

r$2.invoke(KClassImpl.kt:52)←KClassImpl$Data$descriptor$2.invoke(KClassImpl.kt:43)←ReflectProperties$LazySoftVal.invoke(ReflectProperties.java:93)←ReflectProperties$Val.getValue(ReflectProperties.java:32)←KClassImpl$Data.getDescriptor(KClassImpl.kt:-1)←KClassImpl.getDescriptor(KClassImpl.kt:172)
``` 

The critical line is: 
```
val itemId = transformer.transform(input)
```

But it works if the function is invoked using `invoke()`:
```
val itemId = transformer.transform.invoke(input)
```

### Extra

Only happens if the severity of all errors is changed in the `lint.xml` file:

```
 <issue id="all" severity="error"/>
```
