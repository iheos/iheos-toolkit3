@echo off

rem system specific 
rem system specific 

set basedir=C:\e\Packages\mygradlescripts\env-validation


rem set GRADLE_HOME=C:\e\Packages\gradle-1.4-bin
rem set PATH=%PATH%;%GRADLE_HOME%\gradle-1.4\bin

set GRADLE_HOME=C:\e\Packages\gradle-2.3-all
set PATH=%PATH%;%GRADLE_HOME%\gradle-2.3\bin

rem system specific 
rem system specific 



@call gradle -q build -b DiagnosticsPlugin\plugin\build.gradle 

if %ERRORLEVEL% GEQ 1 goto STOP


@call gradle -q -I init.gradle %1
goto END


:STOP
echo Build Error!

:END
