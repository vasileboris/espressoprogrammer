package com.espressoprogrammer.kotlin

data class Book2(val title: String,
                 val authors: Array<String>) {

}

fun createBook2(title: String, author: String): Book2 {
    return Book2(title, Array(1, { i -> author}));
}

/*
fun createBook2(title: String, author1: String, author2: String): Book2 {
    return Book2(title, arrayOf(author1, author2));
}
*/

/*
Error:(9, 25) Kotlin: [Internal Error] org.jetbrains.kotlin.codegen.CompilationException: Back-end (JVM) Internal error: Couldn't inline method call 'arrayOf' into
fun createBook2(title: String, author1: String, author2: String): Book2 {
    return Book2(title, arrayOf(author1, author2));
}
cause: Not generated
Cause: Couldn't find declaration file for kotlin/KotlinPackage
File being compiled and position: (9,25) in /media/fastdata/work/espressoprogrammer-code/kotlin-examples/src/main/java/com/espressoprogrammer/kotlin/Book2.kt
PsiElement: arrayOf(author1, author2)
The root cause was thrown at: InlineCodegen.java:212
	at org.jetbrains.kotlin.codegen.inline.InlineCodegen.genCallInner(InlineCodegen.java:158)
	at org.jetbrains.kotlin.codegen.CallGenerator.genCall(CallGenerator.kt:105)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.invokeMethodWithArguments(ExpressionCodegen.java:2429)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.invokeMethodWithArguments(ExpressionCodegen.java:2387)
	at org.jetbrains.kotlin.codegen.Callable$invokeMethodWithArguments$1.invoke(Callable.kt:44)
	at org.jetbrains.kotlin.codegen.Callable$invokeMethodWithArguments$1.invoke(Callable.kt:23)
	at org.jetbrains.kotlin.codegen.OperationStackValue.putSelector(StackValue.kt:65)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:109)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:102)
	at org.jetbrains.kotlin.codegen.CallGenerator$DefaultCallGenerator.genValueAndPut(CallGenerator.kt:63)
	at org.jetbrains.kotlin.codegen.CallBasedArgumentGenerator.generateExpression(CallBasedArgumentGenerator.java:73)
	at org.jetbrains.kotlin.codegen.ArgumentGenerator.generate(ArgumentGenerator.kt:59)
	at org.jetbrains.kotlin.codegen.CallBasedArgumentGenerator.generate(CallBasedArgumentGenerator.java:60)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.invokeMethodWithArguments(ExpressionCodegen.java:2409)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.invokeMethodWithArguments(ExpressionCodegen.java:2387)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen$16.invoke(ExpressionCodegen.java:3416)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen$16.invoke(ExpressionCodegen.java:3393)
	at org.jetbrains.kotlin.codegen.OperationStackValue.putSelector(StackValue.kt:65)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:109)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:102)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.gen(ExpressionCodegen.java:323)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen$9.invoke(ExpressionCodegen.java:1823)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen$9.invoke(ExpressionCodegen.java:1807)
	at org.jetbrains.kotlin.codegen.OperationStackValue.putSelector(StackValue.kt:65)
	at org.jetbrains.kotlin.codegen.StackValueWithLeaveTask.putSelector(StackValue.kt:57)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:109)
	at org.jetbrains.kotlin.codegen.StackValue.put(StackValue.java:102)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.gen(ExpressionCodegen.java:323)
	at org.jetbrains.kotlin.codegen.ExpressionCodegen.returnExpression(ExpressionCodegen.java:1887)
	at org.jetbrains.kotlin.codegen.FunctionGenerationStrategy$FunctionDefault.doGenerateBody(FunctionGenerationStrategy.java:50)
	at org.jetbrains.kotlin.codegen.FunctionGenerationStrategy$CodegenBased.generateBody(FunctionGenerationStrategy.java:72)
	at org.jetbrains.kotlin.codegen.FunctionCodegen.generateMethodBody(FunctionCodegen.java:365)
	at org.jetbrains.kotlin.codegen.FunctionCodegen.generateMethod(FunctionCodegen.java:204)
	at org.jetbrains.kotlin.codegen.FunctionCodegen.generateMethod(FunctionCodegen.java:139)
	at org.jetbrains.kotlin.codegen.FunctionCodegen.gen(FunctionCodegen.java:114)
	at org.jetbrains.kotlin.codegen.MemberCodegen.genFunctionOrProperty(MemberCodegen.java:180)
	at org.jetbrains.kotlin.codegen.PackagePartCodegen.generateBody(PackagePartCodegen.java:94)
	at org.jetbrains.kotlin.codegen.MemberCodegen.generate(MemberCodegen.java:117)
	at org.jetbrains.kotlin.codegen.PackageCodegen.generateFile(PackageCodegen.java:117)
	at org.jetbrains.kotlin.codegen.PackageCodegen.generate(PackageCodegen.java:61)
	at org.jetbrains.kotlin.codegen.KotlinCodegenFacade.generatePackage(KotlinCodegenFacade.java:99)
	at org.jetbrains.kotlin.codegen.KotlinCodegenFacade.doGenerateFiles(KotlinCodegenFacade.java:77)
	at org.jetbrains.kotlin.codegen.KotlinCodegenFacade.compileCorrectFiles(KotlinCodegenFacade.java:44)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.generate(KotlinToJVMBytecodeCompiler.kt:376)
	at org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.compileModules(KotlinToJVMBytecodeCompiler.kt:121)
	at org.jetbrains.kotlin.cli.jvm.K2JVMCompiler.doExecute(K2JVMCompiler.kt:173)
	at org.jetbrains.kotlin.cli.jvm.K2JVMCompiler.doExecute(K2JVMCompiler.kt:49)
	at org.jetbrains.kotlin.cli.common.CLICompiler.exec(CLICompiler.java:181)
	at org.jetbrains.kotlin.cli.common.CLICompiler.exec(CLICompiler.java:138)
	at org.jetbrains.kotlin.cli.common.CLICompiler.execAndOutputXml(CLICompiler.java:63)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$remoteIncrementalCompile$1.invoke(CompileServiceImpl.kt:246)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$remoteIncrementalCompile$1.invoke(CompileServiceImpl.kt:69)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$1$1.invoke(CompileServiceImpl.kt:420)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$1$1.invoke(CompileServiceImpl.kt:69)
	at org.jetbrains.kotlin.daemon.common.DummyProfiler.withMeasure(PerfUtils.kt:137)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.checkedCompile(CompileServiceImpl.kt:453)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.access$checkedCompile(CompileServiceImpl.kt:69)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$1.invoke(CompileServiceImpl.kt:419)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl$doCompile$1.invoke(CompileServiceImpl.kt:69)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.ifAlive(CompileServiceImpl.kt:559)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.ifAlive$default(CompileServiceImpl.kt:524)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.doCompile(CompileServiceImpl.kt:410)
	at org.jetbrains.kotlin.daemon.CompileServiceImpl.remoteIncrementalCompile(CompileServiceImpl.kt:243)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.rmi.server.UnicastServerRef.dispatch(UnicastServerRef.java:324)
	at sun.rmi.transport.Transport$1.run(Transport.java:200)
	at sun.rmi.transport.Transport$1.run(Transport.java:197)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.Transport.serviceCall(Transport.java:196)
	at sun.rmi.transport.tcp.TCPTransport.handleMessages(TCPTransport.java:568)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run0(TCPTransport.java:826)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.lambda$run$0(TCPTransport.java:683)
	at java.security.AccessController.doPrivileged(Native Method)
	at sun.rmi.transport.tcp.TCPTransport$ConnectionHandler.run(TCPTransport.java:682)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.IllegalStateException: Couldn't find declaration file for kotlin/KotlinPackage
	at org.jetbrains.kotlin.codegen.inline.InlineCodegen.createMethodNode(InlineCodegen.java:212)
	at org.jetbrains.kotlin.codegen.inline.InlineCodegen.genCallInner(InlineCodegen.java:149)
	... 79 more
 */