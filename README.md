# F1ReportsFromTweets
Utilización de ADTs para la realización de reportes solicitados utilizando como fuente un csv de tweets. Trabajo práctico de la materia Programación 2 (Java).

FUNDAMENTAL: Agregar a la estructura de proyecto la library de Java: commons-csv-1.102 (File > Project Structure > New Project Library (Alt + Insertar) > Java > Buscar en Program Files )

1. Subir el archivo f1_dataset.csv a la carpeta src. El código ya viene predeterminado para leer ese archivo.

2. Subir archivo drivers.txt para poder ser leído para la primer función.

3. Correr Main. Este archivo corre la parte principal del programa y abre la terminal con el siguiente menú:

   ------- Twitter based F1 Reports (Jul 21 - Aug 22)-------
   ***  Si no cargó los datos, por favor ingrese la opción 0  ***
   0 - Cargar datos al sistema (solo funciona la primera vez)
   1 - Listar los 10 pilotos activos en la temporada 2023 más mencionados en los tweets
   2 - Top 15 usuarios con más tweets
   3 - Cantidad de hashtags distintos para un día dado
   4 - Hashtag más usado para un día dado
   5 - Top 7 cuentas con más favoritos
   6 - Cantidad de tweets con una palabra o frase específicos
   7 - Cerrar programa
   Ingrese la opción deseada: 

   i. Cargar datos al sistema presionando 0 (de lo contrario los reportes no funcionarán)

   ii. Seleccionar la opción para correr el reporte deseado.
	a. El reporte 1 pedira que se ingrese mes (MM) y año (YYYY) por separado.
	b. Los reportes 3 y 4 solicitarán fecha en formatio (YYYY-MM-DD)
	c. El reporte 6 solictará una palabra o frase (tener en cuenta que es sensible a characteres especiales pero no a mínusculas/mayúsculas)


  Útil: cada reporte imprime el tiempo que demora en milisegundos una vez finalizado.
