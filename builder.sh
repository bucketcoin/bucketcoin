#!/usr/bin/env bash

# UBER-JAR BUILDER SCRIPT & ENVIRONMENT SETTER // BUCKETCOIN BLOCKCHAIN

# check dependencies

# command-line dependencies
echo -n "Checking dependencies... "
for name in git javac gradle awk which
do
  [[ $(which $name 2>/dev/null) ]] || { echo -en "\n[build/ERROR] Dependency $name is not present."; exit 1; }
done

# jdk version
if type -p java; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "[build/ERROR] Dependency java is not present."
    exit 1;
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}');
    # shellcheck disable=SC2071
    if [[ "$version" > "16" ]]; then
        :
    else
        echo "[build/ERROR] JDK version is below JDK 16.";
    fi
fi

# start process
echo "[build/INFO] All command-line dependencies are fulfilled.";
mkdir temp && cd temp || echo "[build/ERROR] Failed to change directory."; exit 1;
echo "[build/INFO] Directory /temp has been created.";

# start cloning dependency KotlinP2P and build using gradle
echo "[build/INFO] Cloning dependency KotlinP2P from https://github.com/Cr4shdev/KotlinP2P.git..."
git clone https://github.com/Cr4shdev/KotlinP2P.git;
cd KotlinP2P || echo "[build/ERROR] Failed to change directory."; exit 1;
echo "[build/INFO] Building dependency KotlinP2P using Gradle..."
if gradle build > /dev/null 2>&1; then
  echo "[build/INFO] Dependency KotlinP2P is successfully built."
else
  echo "[build/ERROR] Build failed."
fi

cd build/libs || echo "[build/ERROR] Failed to change directory."; exit 1;
mv KotlinP2P-0.1.jar ../../lib;
cd ../../lib || echo "[build/ERROR] Failed to change directory."; exit 1;
mkdir kotlinP2P && mv KotlinP2P-0.1.jar kotlinP2P && cd kotlinP2P || echo "[build/ERROR] Failed to change directory."; exit 1;
file=$(touch pom.xml)

# write to pom.xml
# shellcheck disable=SC2086
cat > $file <<- END-OF-POM.XML
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>net.bucketcoin</groupId>
        <artifactId>bucket-coin</artifactId>
        <version>1.0</version>
    </parent>

    <artifactId>kotlinP2P</artifactId>
    <packaging>non-maven-jar</packaging>

    <build>
            <plugins>
                <plugin>
                    <groupId>com.github.stephenc.nonmavenjar</groupId>
                    <artifactId>non-maven-jar-maven-plugin</artifactId>
                    <configuration>
                        <jarFile>${basedir}/KotlinP2P-0.1.jar</jarFile>
                    </configuration>
                </plugin>
            </plugins>
    </build>

</project>
END-OF-POM.XML

