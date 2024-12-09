plugins {
    id("java-library")
}

val main = "de.jjohannes.robot.R2D2"

val nxj = layout.buildDirectory.file("${main.substring(main.lastIndexOf(".") + 1)}.nxj")
val nxtClassesJar = layout.projectDirectory.file("lib/nxt/classes.jar").asFile
val pcClasspath = files(
        layout.projectDirectory.dir("lib/pc").asFileTree.filter { it.extension == "jar" },
            layout.projectDirectory.dir("lib/pc/native/windows")
)

dependencies {
    implementation(files(nxtClassesJar))
}

tasks.compileJava {
    options.compilerArgs = listOf("-bootclasspath", nxtClassesJar.absolutePath)
}

val linkNxj = tasks.register<JavaExec>("linkNxj") {
    group = "robot"

    inputs.files(tasks.compileJava.map { it.destinationDirectory })
    outputs.file(nxj)

    classpath = pcClasspath
    mainClass = "lejos.pc.tools.NXJLink"
    args("--writeorder", "LE", "--bootclasspath", nxtClassesJar.absolutePath, "--classpath", inputs.files.asPath, "-o", nxj.get().asFile, main)
}

val uploadNxj = tasks.register<JavaExec>("uploadNxj") {
    group = "robot"

    inputs.files(linkNxj)

    classpath = pcClasspath
    mainClass = "lejos.pc.tools.NXJUpload"
    args("-r", inputs.files.filter { it.extension == "nxj" }.singleFile)
}

tasks.assemble {
    dependsOn(uploadNxj)
}