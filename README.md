## Named annotation
Inserts `def name: String` that returns class name into your class

## Futurify & FuturifyBlocking 
Annotations. Make sure all methods return a future. `Futurify` sends execution onto `ExecutionContext.global` and `FuturifyBlocking` essentially wraps method into Future.successful

Can be thought of as a compile time statically typed decorator.
