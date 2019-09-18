TPE 1 - Programacion de Objetos Distribuidos 2019

Integrantes: Della Sala - Giorgi - Rodriguez - Santoflaminio

Instrucciones

1. Desde la carpeta principal dirigirse a 'pod-archetype'

2. Abrir la terminal en dicho directorio 

3. Ejecutar ./start.sh

4. Ejecutar ./startServer.sh

5. Dirigirse a 'client/target/podtpe-client-1.0-SNAPSHOT'

6. Por ultimo ejecutar './CLIENT_SH JVM_ARGUMENTS'

	Donde CLIENT_SH puede ser alguno de los .sh a continuacion: run-managementclient.sh, run-votingclient.sh, run-fiscalclient.sh o run-queryclient.sh
	JVM_ARGUMENTS son los argumentos correspondientes.

A continuacion se detalla la forma de ejecutar cada uno:

Cliente de Administración:
				
				./run-managementclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de votación.
				- actionName​ es el nombre de la acción a realizar: open, state o close.


Cliente de Votación:
				
				./run-votingclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -DvotesPath=​fileName

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de votación.
				- fileName​ ​es el path del archivo de entrada con los votos de los ciudadanos


Cliente de Fiscalización:
				
				./run-fiscalclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -Did=​ pollingPlaceNumber -Dparty=​ partyName​

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de fiscalización.
				- pollingPlaceNumber​ es el número de la mesa de votación a fiscalizar
				- partyName​ es el nombre del partido político del fiscal

Cliente de Consulta:
				
				./run-queryclient.sh -DserverAddress=xx.xx.xx.xx:yyyy [ -Dstate=​ stateName​ | -Did=​ pollingPlaceNumber​ ] -DoutPath=​ fileName​

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de consulta.
				Si no se indica ​ -Dstate​ ni ​ -Did​ se resuelve la consulta 1
				- stateName es el nombre de la provincia elegida para resolver la consulta 2
				- pollingPlaceNumber ​ es el número de la mesa elegida para resolver la consulta 3
				- Si se indican ambos ​ -Dstate y ​ -Did la consulta falla
				- fileName​ es el path del archivo de salida con los resultados de la consulta elegida


Tests

Para ejecutar los tests utilizar un entorno de desarrollo como Eclipse.

1. Dirigirse a pod-archetype > podtpe > server > src > main > test > ar > edu > itba > test donde se econtrarán las clases con casos de prueba

2. Agregar al build path la carpeta test mencionada. Para ello, usando Eclipse, ir a Project > Properties. Seleccionar la opción Java Build Path, y luego la pestaña Source. Hacer click en Add Folder... y seleccionar la carpeta test

3. Ejecutar cada clase con JUnit5. Para ello seleccionar en Run > Run Configurations > JUnit, JUnit5 como Test runner
