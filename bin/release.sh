#!/bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"
ARTIFACT_ID=$(cat $DIR/../pom.xml | sed -n 's|.*<artifactId>\(.*\)</artifactId>.*|\1|p' | head -n1)
VERSION=$(cat $DIR/../pom.xml | sed -n 's|.*<version>\(.*\)</version>.*|\1|p' | head -n1)

rm -rf $DIR/../release
mkdir $DIR/../release

cp $DIR/../target/$ARTIFACT_ID-$VERSION-jar-with-dependencies.jar $DIR/../release/extractor-$VERSION.jar
cp $DIR/../src/main/resources/extractor.yml $DIR/../release

cat <<EOT >> $DIR/../release/run.bat
chcp 65001
title Wiki Extractor v$VERSION
color 71
cls
java -jar extractor-$VERSION.jar
pause
EOT

cd $DIR/../release
zip -r extractor-$VERSION.zip *