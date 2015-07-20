#!/bin/sh

if [ -z "$JAVA_HOME" ]; then
	echo "The JAVA_HOME variable is not set!\nThis installation requires the JDK version 1.6 or higher."
exit 1
fi

export GRADLE_HOME=./gradle-1.4-bin
export PATH=$PATH:$GRADLE_HOME/gradle-1.4/bin

if [ "$1" = "buildandrun" ]; then 
	gradle -q build -b DiagnosticsPlugin/plugin/build.gradle 

	if [ $? -eq 0 ]; then
		gradle -I init.gradle $2 $3
	else
		echo "Build faild!"
	fi
else
	gradle -I init.gradle $1
fi


