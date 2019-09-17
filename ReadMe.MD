TPE 1 - Programacion de Objetos Distribuidos 2019

Della Sala - Giorgi - Rodriguez - Santoflaminio

Instrucciones

1. Desde la carpeta principal dirigerse a 'pod-archetype'

2. Ejecutar 'mvn clean install'

3. Dirigirse a 'podtpe/server/target'

4. Extraer 'podtpe-server-1.0-SNAPSHOT-bin.tar.gz'

5. Dirigirse a 'podtpe-server-1.0-SNAPSHOT-bin/podtpe-server-1.0-SNAPSHOT'

6. Ejecutar 'chmod +x run-registry.sh' y 'chmod +x run-server.sh'

7. Luego ejecutar './run-registry.sh'

8. En otra terminal, pero en el mismo directorio ejecutar './run-server.sh'

9. Posicionarse nuevamente en 'podtpe' y dirigirse a 'client/target'

10. Extraer 'podtpe-client-1.0-SNAPSHOT-bin.tar.gz'

11. Dirigirse a 'podtpe-client-1.0-SNAPSHOT-bin/podtpe-client-1.0-SNAPSHOT'

12. Luego ejecutar 'chmod +x CLIENT_SH'

	CLIENT_SH puede ser:  run-administrationclient.sh
						  run-votingclient.sh
						  run-fiscalclient.sh
						  run-queryclient.sh

13. Luego ejecutar './CLIENT_SH JVM_ARGUMENTS'

	Donde CLIENT_SH puede ser alguno de los .sh ya mencionados
	y JVM_ARGUMENTS son los argumentos correspondientes.

A continuacion se detalla la forma de ejecutar cada uno:

Cliente de Administracion:
				
				./run-administrationclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -Daction=actionName

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de votación.
				- actionName​ es el nombre de la acción a realizar: open, state o close.


Cliente de Votacion:
				
				./run-votingclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -DvotesPath=​fileName

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de votación.
				- fileName​ ​es el path del archivo de entrada con los votos de los ciudadanos


Cliente de Fiscalizacion:
				
				./run-fiscalclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -Did=​ pollingPlaceNumber -Dparty=​ partyName​

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de fiscalización.
				- pollingPlaceNumber​ es el número de la mesa de votación a fiscalizar
				- partyName​ es el nombre del partido político del fiscal

Cliente de Consulta:
				
				./run-queryclient.sh -DserverAddress=xx.xx.xx.xx:yyyy -Dstate=​ stateName​ | -Did=​ pollingPlaceNumber​ ] -DoutPath=​ fileName​

				- xx.xx.xx.xx:yyyy es la dirección IP y el puerto donde está publicado el servicio de consulta.
				Si no se indica ​ -Dstate​ ni ​ -Did​ se resuelve la consulta 1
				- stateName es el nombre de la provincia elegida para resolver la consulta 2
				- pollingPlaceNumber ​ es el número de la mesa elegida para resolver la consulta 3
				- Si se indican ambos ​ -Dstate y ​ -Did la consulta falla
				- fileName​ es el path del archivo de salida con los resultados de la consulta elegida