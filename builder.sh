#!/usr/bin/env bash

# check dependencies

# command-line dependencies
echo -n "Checking dependencies... "
for name in git kotlinc javac gradle
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
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    # shellcheck disable=SC2071
    if [[ "$version" > "16" ]]; then
        :
    else
        echo "[build/ERROR] JDK version is below JDK 16."
    fi
fi

# start process
echo "[build/INFO] All command-line dependencies are fulfilled.";
mkdir temp && cd temp || exit 1;
echo "[build/INFO] Directory /temp has been created.";

echo "[build/INFO] Cloning KotlinP2P from https://github.com/Cr4shdev/KotlinP2P.git..."
git clone https://github.com/Cr4shdev/KotlinP2P.git;
cd KotlinP2P || exit 1;
gradle


