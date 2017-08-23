::
:: Win32 build script for ApacheJMeter.jar
::
:: @author Stefano Mazzocchi <stefano@apache.org>
:: @version $Revision: 322547 $ $Date: 1998-12-13 16:52:04 -0800 (Sun, 13 Dec 1998) $
::

@echo off

echo  * Compiling Java source...
md .\temp
javac -O -d .\temp .\org\apache\jmeter\*.java .\org\apache\jmeter\timers\*.java .\org\apache\jmeter\visualisers\*.java

echo  * Copying image files...
md temp\org\apache\jmeter\images
copy .\org\apache\jmeter\images\*.* .\temp\org\apache\jmeter\images\*.* >nul

echo  * Copying property files...
copy .\org\apache\jmeter\*.properties .\temp\org\apache\jmeter\*.properties >nul

echo  * Creating ApacheJMeter.jar file...
cd .\temp
jar cmf0 ..\MANIFEST ..\..\bin\ApacheJMeter.jar *.* >nul
cd ..

echo  * Removing temp directory...
deltree /y temp >nul

echo Done.