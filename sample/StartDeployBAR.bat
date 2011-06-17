@echo off
setlocal

set INSTALLPATH=%~dp0..\..

rem Add the MQ Classes for Java to the CLASSPATH...
set CLASSPATH=%INSTALLPATH%\classes\com.ibm.mq.jar

rem Add the Configuration Manager Proxy to the CLASSPATH...
set CLASSPATH=%CLASSPATH%;%INSTALLPATH%\classes\ConfigManagerProxy.jar

rem Add the Samples directory to the CLASSPATH...
set CLASSPATH=%CLASSPATH%;%INSTALLPATH%\sample\ConfigManagerProxy\ConfigManagerProxySamples.jar

rem Add the bin directory to the PATH
rem This will allow the Configuration Manager Proxy classes to load LogonInfo.dll which 
rem will enable domain support.  Failure to have this dll in the path will mean that 
rem domain users will be identified as machine\user, rather than domain\user
set PATH=%PATH%;%INSTALLPATH%\bin

rem Start the sample
"%INSTALLPATH%\jre\bin\java" cmp.DeployBAR %1 %2 %3 %4 %5

endlocal
