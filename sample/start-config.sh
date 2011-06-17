#!/bin/sh
export CLASSPATH=./classes/com.ibm.mq.jar:./classes/ConfigManagerProxy.jar:./ConfigManagerProxySamples.jar
/usr/lib/jvm/j2sdk1.4.2_17/jre/bin/java cmp.exerciser.CMPAPIExerciser
