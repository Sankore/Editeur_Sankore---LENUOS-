#LENUOS : Installation
## Prérequis:

- JAVA 6
- Un serveur d'application servlet 2.5. Testé avec Tomcat 6
- Xuggler [http://www.xuggle.com/xuggler/](http://www.xuggle.com/xuggler/downloads)
- ImageMagick [http://www.imagemagick.org/script/index.php](http://www.imagemagick.org/script/index.php) (Testé avec la version 6.6.3-1)

Attention à l'architecture de votre JVM. Si vous avez une JVM 32bits prenez les librairies adéquats pour xuggler et ImageMagick

## Installation

### Xuggler [http://www.xuggle.com/xuggler/downloads/](http://www.xuggle.com/xuggler/downloads)

Installer selon les indications officielles puis ajouter les jar de xuggler au classpath du serveur

	export CLASSPATH=chemin_vers_xuggler/share/java/jars:$CLASSPATH
	
Attention Tomcat ignore le classpath du système. Il faut ajouter dans conf/catalina.properties la valeur à la propriété shared.loader ou commons.loader

	shared.loader=chemin_vers_xuggler/share/java/jars/xuggle-xuggler.jar
	
#### Notes
xuggler ne propose plus de binaires. Si l'installation par le code source échoue, les binaires sont disponibles à ces adresses 

1. [3.4 en version Linux 64 bits](http://com.xuggle.s3.amazonaws.com/xuggler/xuggler-3.4.FINAL/xuggle-xuggler.3.4.1012-x86_64-unknown-linux-gnu.sh)
2. [3.4 en version Linux 32 bits](http://com.xuggle.s3.amazonaws.com/xuggler/xuggler-3.4.FINAL/xuggle-xuggler.3.4.1012-i686-pc-linux-gnu.sh)
	
		chmod +x xuggle-xuggler.3.4.1012-i686-pc-linux-gnu.sh
		./xuggle-xuggler.3.4.1012-i686-pc-linux-gnu.sh


### ImageMagick [http://www.imagemagick.org/script/binary-releases.php](http://www.imagemagick.org/script/binary-releases.php)

Ajouter une variable d'environnement IM4JAVA_TOOLPATH pointant vers l'installation de ImageMagick ou mieux vers le PATH

	export IM4JAVA_TOOLPATH=$PATH

Voilà un exemple de profile sous MacOS X

	export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
	export XUGGLE_HOME=/usr/local/xuggler
	export MAGICK_HOME=/usr/local/ImageMagick
	export DYLD_LIBRARY_PATH=$XUGGLE_HOME/lib:$MAGICK_HOME/lib:$DYLD_LIBRARY_PATH
	export PATH=$XUGGLE_HOME/bin:$MAGICK_HOME/bin:$PATH
	export IM4JAVA_TOOLPATH=$PATH

### Notes

1. Sous MacOS X créer un fichier ~/.MacOSX/environment.plist pour signifier à Eclipse où se trouvent les librairies dynamiques utilisées par 
xuggler et im4java (ffmpeg et ImageMagick) et aussi les binaires des outils utilisés par im4java.

		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
		 "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
		<plist version="1.0">
			<dict>
		    	<key>DYLD_LIBRARY_PATH</key>
		    	<string>/usr/local/xuggler/lib:/usr/local/ImageMagick/lib</string>
				<key>IM4JAVA_TOOLPATH</key>
				<string>/usr/local/ImageMagick/bin</string>
		  	</dict>
		</plist>
		
2. Sous une debian, j'ai rencontré un problème. L'application n'arrive pas à exécuter le bin d'imagemagick à cause d'un problème de mémoire.

		org.im4java.core.CommandException: java.io.IOException: Cannot run program "/usr/local/bin/convert": java.io.IOException: error=12, Cannot allocate memory
		at org.im4java.core.ImageCommand.run(ImageCommand.java:215)

	La solution (Source: [http://stackoverflow.com/questions/1124771/how-to-solve-java-io-ioexception-error12-cannot-allocate-memory-calling-runt](http://stackoverflow.com/questions/1124771/how-to-solve-java-io-ioexception-error12-cannot-allocate-memory-calling-runt))
		
		echo 1 > /proc/sys/vm/overcommit_memory	

3. Selon la charge du serveur, il peut être nécessaire d'augmenter le nombre de fichiers pouvant être ouverts simultannément [http://serverfault.com/questions/20387/debian-too-many-open-files](http://serverfault.com/questions/20387/debian-too-many-open-files)
4. Pour avoir la dernière version de ImageMagick, même avec la debian 5.0.8, il faut la compiler soi-même.
 
	Préalablement au configure de ImageMagick, il faut installer les delegates au moins pour les JPEG et les PNG, aussi en les compilant :
	Télécharge les sources ici : http://www.imagemagick.org/download/delegates/
	http://www.imagemagick.org/download/delegates/jpegsrc.v8b.tar.bz2
	http://www.imagemagick.org/download/delegates/libpng-1.5.2.tar.bz2
	 
	 
	Et pour pouvoir compiler ça, il faut préalablement installer par apt le soft zlib1g-dev, par exemple avec la commande :
	aptitude install zlib1g-dev  
	 
	En récapitulant :
	
	1. Installation de zlib1g-dev par apt : aptitude install zlib1g-dev
	2. Compilation et installation du delegate jpeg pour ImageMagick
	3. Compilation et installation du delegate png pour ImageMagick
	4. Compilation et installation de ImageMagick
	
## Configuration

### Dossier d'application
Le LENUOS stocke les données d'utilisateurs sur le système de fichier. Il faut donc créé un dossier reservé à l'application et accessible en droit. Ex:
	
	/var/lib/lenuos/

### -Deditor.logs
Le dossier dans lequel se trouveront les logs de l'application. Ex:

	-Deditor.logs=/var/lib/lenuos/logs/

### -Deditor.loglevel
Le niveau de log, les valeurs possibles sont celles de log4j Ex:

	-Deditor.loglevel=WARN

### -Deditor.config
Le path vers un fichier de config pour overrider les propriétés de configuration par défaut. Ex:

	-Deditor.config=/var/lib/lenuos/lenuos.properties

### Exemple de fichier de config

	# Users directory.
	users.directory = /var/lib/lenuos/

	# Set whenever users can retrieve their login information
	# by email if security is activated.
	security.retrieve.login = false
	
	# xwiki open sankoré
	opensankore.sso.enable = true
	opensankore.sso.cookie.domain = planete.sankore.org
	opensankore.sso.cookie.username = username
	opensankore.sso.verificationurl = http://planete.sankore.org/xwiki/bin/view/Main/Authentication?xpage=plain
	opensankore.sso.loginurl = http://planete.sankore.org/xwiki/bin/login/XWiki/XWikiLogin?xredirect=%s
	opensankore.sso.redirecturl = http://lenuos.sankore.org/
	opensankore.sso.timeout = 1000
	opensankore.sso.proxyurl =
	opensankore.sso.proxyport = 8888

	#octets (default 10Mo). -1 mean unlimitted
	upload.max.size = 10240000
	#octets (default 1Mo). -1 mean unlimitted
	icon.upload.max.size = 1024000
	#octets (default -1). -1 mean unlimitted
	model.upload.max.size = -1

	
## Développement

Le projet utilise Eclipse comme IDE

L'Editeur est divisé en 3 projets:

1. editor
2. editor-api
3. editor-modules

### Build et déploiement

Le projet utilise ant.

Il se construit à partir du build maitre qui est à la racine, en dehors des sous projets. Par défaut le résultat est placé dans ~/editor. L'option -Dprojetc.bin permet de changer le repertoire de destination.

La variable XUGGLE_HOME est nécessaire. Ajouter là à votre environnement si vous buildez en ligne de commande ou comme propriété ant dans les préférences du projet editor.