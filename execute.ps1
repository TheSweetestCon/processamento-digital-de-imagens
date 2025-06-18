$classpath = "lib\jfreechart-1.5.4.jar;lib\jcommon-1.0.24.jar"
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -cp $classpath -d bin $files

Write-Host "Executando Main..."
java -cp "bin;lib\jfreechart-1.5.4.jar;lib\jcommon-1.0.24.jar" src.ui.Main