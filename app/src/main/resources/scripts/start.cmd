@echo off
setlocal enabledelayedexpansion

if not "%EIP_PROFILE%" == "" (
    set "ENV=%EIP_PROFILE%"
) else (
    if "%~1" == "" (
        set "ENV=dev"
    ) else (
        set "ENV=%~1"
    )
)

REM Ensure that the provided environment is valid
if "%ENV%"=="dev" (
    goto :continue
) else if "%ENV%"=="prod" (
    goto :continue
) else if "%ENV%"=="test" (
    goto :continue
) else if "%ENV%"=="staging" (
    goto :continue
) else (
    echo Error: Invalid environment provided.
    goto :eof
)

:continue
set "SPRING_PROFILES_ACTIVE=%ENV%"

REM Check if routes directory exists and has contents
if exist "routes\" (
    dir /b "routes\" | findstr /v /b /i /c:"Directory of" | findstr /v /i /c:"File Not Found" >nul 2>&1
    if errorlevel 1 (
        if "%ENV%"=="dev" (
            echo Routes directory contents:
            for /f "tokens=*" %%F in ('dir /b "routes\" ^| findstr /v /b /i /c:"Directory of" ^| findstr /v /i /c:"File Not Found"') do (
                echo - routes\%%F
            )
        )
    ) else (
        mkdir "routes\" >nul 2>&1
        echo No routes found in routes directory.
    )
) else (
    mkdir "routes\" >nul 2>&1
    echo No routes found in routes directory.
)

echo Starting Ozone EIP Client Application in %ENV% environment.
java -cp "eip-client.jar" "-Dloader.path=routes/" org.springframework.boot.loader.PropertiesLauncher
