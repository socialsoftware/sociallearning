import sbt.Keys.scalacOptions

lazy val buildSettings = Seq(
  name := "saslearning",
  organization := "pt.ulisboa.tecnico",
  version := "2.0.0-SNAPSHOT",
  scalaVersion := "2.12.7",
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/socialsoftware/saslearning"),
      "scm:git:git@github.com:socialsoftware/saslearning.git"
    )
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("fix", "all compile:scalafixCli test:scalafixCli")

addCompilerPlugin(scalafixSemanticdb)

lazy val finchVersion = "0.24.0"
lazy val circeVersion = "0.10.0"
lazy val jwtVersion = "0.18.0"
lazy val refinedVersion = "0.9.2"

val testDependencies = Seq(
  "org.mockito"    % "mockito-all"         % "1.10.19",
  "org.scalacheck" %% "scalacheck"         % "1.14.0",
  "org.scalatest"  %% "scalatest"          % "3.0.5",
  "eu.timepit"     %% "refined-scalacheck" % refinedVersion,
)

val dependenciesSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel"           %% "cats-core" % "1.4.0",
    "com.github.finagle"      %% "finch-core" % finchVersion,
    "com.github.finagle"      %% "finch-circe" % finchVersion,
    "io.circe"                %% "circe-generic" % circeVersion,
    "io.circe"                %% "circe-generic-extras" % circeVersion,
    "io.circe"                %% "circe-parser" % circeVersion,
    "io.circe"                %% "circe-refined" % circeVersion,
    "eu.timepit"              %% "refined" % refinedVersion,
    "com.sun.mail"            % "mailapi" % "1.6.2",
    "com.pauldijou"           %% "jwt-core" % jwtVersion,
    "com.pauldijou"           %% "jwt-circe" % jwtVersion,
    "org.scalaj"              %% "scalaj-http" % "2.4.1",
    "com.twitter"             %% "twitter-server" % "18.9.1",
    "com.github.pureconfig"   %% "pureconfig" % "0.9.2",
  ) ++ testDependencies.map(_ % "test"),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
)

val compilerSettings = Seq(
  javacOptions ++= Seq("-Xlint", "-encoding", "UTF-8", "-Dfile.encoding=utf-8"),
  scalacOptions ++= Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:implicitConversions", // Explicitly enables the implicit conversions feature
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xfuture", // Turn on future language features.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match", // Pattern match may not be typesafe.
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification", // Enable partial unification in type constructor inference
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-unused:locals", // Warn if a local definition is unused.
    "-Ywarn-unused:params", // Warn if a value parameter is unused.
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
    "-Ywarn-unused:privates", // Warn if a private member is unused.
    //"-Ywarn-value-discard",              // Warn when non-Unit expression results are unused.
    "-Yrangepos" // For semanticdb
  ),
  scalacOptions in (Compile, console) ~= (_ filterNot { option =>
    option.startsWith("-Ywarn") || option == "-Xfatal-warnings"
  }),
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
)

lazy val allSettings = buildSettings ++ compilerSettings ++ dependenciesSettings

lazy val root = project
  .in(file("."))
  .settings(allSettings)
