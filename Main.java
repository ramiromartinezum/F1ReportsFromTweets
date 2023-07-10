import uy.edu.um.prog2.adt.Hash.*;
import uy.edu.um.prog2.adt.Heap.*;
import uy.edu.um.prog2.adt.LinkedList.MyLinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    /**Lector del txt usado para funcion de los pilotos*/
    public static String[] readDriversTxt(String filePath) throws IOException {
        String[] lines = new String[20];

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < lines.length; i++) {
                lines[i] = reader.readLine();
            }
        }
        return lines;
    }

    private static boolean isValidDate(String input) {
        try {
            LocalDate.parse(input);
            return input.matches("\\d{4}-\\d{2}-\\d{2}");
        } catch (DateTimeParseException e) {
            System.out.println("Fecha ingresada no valida");
            return false;
        }
    }

    private static boolean isWithinRange(LocalDate date) {
        LocalDate startDate = LocalDate.of(2021, 7, 1);
        LocalDate endDate = LocalDate.of(2022, 8, 31);
        return date.isAfter(startDate) && date.isBefore(endDate);
    }


///////////////////////////////MAIN///////////////////////////////////
    public static void main(String[] args) {

        /**Creacion de los HashTables usernamesArray (para recorrer uy.edu.um.prog2.adt.Hash de users facilmente)*/
        MyHashTable<String,User> users = new HashTableImpl<>(500000);
        MyHashTable<Long,Tweet> tweets = new HashTableImpl<>(700000);
        MyHashTable<String,HashTag> hashtags = new HashTableImpl<>(100000);
        String[] userNamesArray = new String[500000];

        Scanner scanner = new Scanner(System.in);
        boolean datosCargados = false;
        int option;

        while (true) {
            double tiempoInicio = 0;
            double tiempoFin = 0;

            System.out.println("------- Twitter based F1 Reports (Jul 21 - Aug 22)-------");
            System.out.println("***  Si no cargó los datos, por favor ingrese la opción 0  ***");
            System.out.println("0 - Cargar datos al sistema (solo funciona la primera vez)");
            System.out.println("1 - Listar los 10 pilotos activos en la temporada 2023 más mencionados en los tweets");
            System.out.println("2 - Top 15 usuarios con más tweets");
            System.out.println("3 - Cantidad de hashtags distintos para un día dado");
            System.out.println("4 - Hashtag más usado para un día dado");
            System.out.println("5 - Top 7 cuentas con más favoritos");
            System.out.println("6 - Cantidad de tweets con una palabra o frase específicos");
            System.out.println("7 - Cerrar programa");
            System.out.print("Ingrese la opción deseada: ");

            option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (option == 0 && !datosCargados) {
                tiempoInicio = System.currentTimeMillis();
                try {
                        CargaCSV cargaCSV = new CargaCSV();
                        cargaCSV.dataLoad(tweets, users, hashtags, userNamesArray);
                        datosCargados = true;
                        System.out.println("Datos cargados correctamente");
                        System.out.println(hashtags.size());

                        tiempoFin = System.currentTimeMillis();
                } catch (IOException e) {
                    System.out.println("Error al cargar los datos: " + e.getMessage());
                }
            } else if (option == 1 && datosCargados) {
                tiempoInicio = System.currentTimeMillis();

                String[] driversArray = new String[20];
                try {
                    driversArray = readDriversTxt("src/drivers.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.print("Ingrese el mes (MM): ");
                int mesIngresado = scanner.nextInt();
                System.out.print("Ingrese el año (YYYY): ");
                int anioIngresado = scanner.nextInt();
                tiempoInicio = System.currentTimeMillis();

                if ((anioIngresado == 2021 && mesIngresado>=7 && mesIngresado<=12) || (anioIngresado == 2022 && mesIngresado>=1 && mesIngresado<=8)){
                    MyHeapImpl<Integer, String> heap = new MyHeapImpl<>(20);

                    for (String driver : driversArray) {
                        String driverName = driver.toLowerCase();
                        int counter = 0;

                        for (long i = 0; i < tweets.size(); i++) {
                            if (tweets.get(i).getDate().getMonthValue() == mesIngresado && tweets.get(i).getDate().getYear() == anioIngresado && tweets.get(i).getContent().toLowerCase().contains(driverName)) {
                                counter++;
                            }
                        }
                        heap.insert(counter, driver);
                    }
                    System.out.println("N° menciones   Nombre del piloto");
                    Pair<Integer, String> removedCountDriver;
                    for (int i = 0; i < 10; i++) {
                        removedCountDriver = heap.removeMax();
                        System.out.println(removedCountDriver.getKey() + "           " + removedCountDriver.getValue());
                    }
                } else {
                    System.out.println("Fecha fuera de rango");
                }
                tiempoFin = System.currentTimeMillis();

            } else if (option == 2 && datosCargados) {
                tiempoInicio = System.currentTimeMillis();

                MyHeapImpl<Integer, User> heap = new MyHeapImpl<>(users.size());

                for (String userName : userNamesArray) {
                    if (userName == null) {
                        break;
                    } else {
                        heap.insert(users.get(userName).getTweetCount(), users.get(userName));
                    }
                }

                Pair<Integer, User> tweetCountAndUser;
                System.out.println("N° de tweets   Verificado     Username");
                for (int i = 0; i < 15; i++) {
                    tweetCountAndUser = heap.removeMax();
                    System.out.println(tweetCountAndUser.getKey() +"         "+ tweetCountAndUser.getValue().isVerified() +"          "+ tweetCountAndUser.getValue().getName() );
                }
                tiempoFin = System.currentTimeMillis();

            } else if (option == 3 && datosCargados) {
                System.out.println("Ingrese una fecha con el formato YYYY-MM-DD: ");
                String fechaIngresada = scanner.nextLine();
                if (isValidDate(fechaIngresada)) {
                    LocalDate date = LocalDate.parse(fechaIngresada);
                    if (isWithinRange(date)) {
                        int dia = date.getDayOfMonth();
                        int mes = date.getMonthValue();
                        int anio = date.getYear();

                        tiempoInicio = System.currentTimeMillis();

                        int hashtagsDistintos = 0;

                        MyHashTable<String, Integer> usedHashtagsHash = new HashTableImpl<>(hashtags.size());

                        for (long i = 0; i < tweets.size(); i++) {
                            Tweet tweetTemp = tweets.get(i);
                            if (tweetTemp.getDate().getDayOfMonth() == dia && tweetTemp.getDate().getMonthValue() == mes && tweetTemp.getDate().getYear() == anio && tweetTemp.getTweetHashtags().size() != 0) {
                                for (int j = 0; j < tweetTemp.getTweetHashtags().size(); j++) {
                                    if (!(hashtagsDistintos == 0)) {
                                        if (!usedHashtagsHash.contains(tweetTemp.getTweetHashtags().get(j).getText())) {
                                            usedHashtagsHash.put(tweetTemp.getTweetHashtags().get(j).getText(), hashtagsDistintos);
                                            hashtagsDistintos++;
                                        }
                                    } else {
                                        usedHashtagsHash.put(tweetTemp.getTweetHashtags().get(j).getText(), hashtagsDistintos);
                                        hashtagsDistintos++;
                                    }
                                }
                            }
                        }
                        System.out.println("Cantidad de hashtags distintos para el dia " + dia + "/" + mes + "/" + anio + ": " + hashtagsDistintos);
                        tiempoFin = System.currentTimeMillis();
                    } else {
                        System.out.println("Fecha fuera de rango");
                    }
                }
            } else if (option == 4 && datosCargados) {
                System.out.println("Ingrese una fecha con el formato YYYY-MM-DD: ");
                String fechaIngresada = scanner.nextLine();
                if (isValidDate(fechaIngresada)) {
                    LocalDate date = LocalDate.parse(fechaIngresada);
                    if (isWithinRange(date)) {
                        int dia = date.getDayOfMonth();
                        int mes = date.getMonthValue();
                        int anio = date.getYear();

                        tiempoInicio = System.currentTimeMillis();

                        //filtramos hashtag por dia ingresado
                        MyHashTable<Integer, Tweet> dayFilteredTweets = new HashTableImpl<>(tweets.size());

                        int fakeID = 0;
                        for (long i = 0; i < tweets.size(); i++) {
                            Tweet tweetTemp = tweets.get(i);
                            if (tweetTemp.getDate().getDayOfMonth() == dia && tweetTemp.getDate().getMonthValue() == mes && tweetTemp.getDate().getYear() == anio) {
                                dayFilteredTweets.put(fakeID, tweetTemp);
                                fakeID++;
                            }
                        }

                        //con los tweets filtrados por dia, guardamos los tweets en un hash para lograr una lista de hashtags unicos
                        MyHashTable<String, Pair<Integer, String>> dayFilteredHashtags = new HashTableImpl<>(hashtags.size());

                        for (int i = 0; i < dayFilteredTweets.size(); i++) {
                            Tweet tweetTemp = dayFilteredTweets.get(i);
                            for (int j = 0; j < tweetTemp.getTweetHashtags().size(); j++) {
                                String hashtagInTweet = tweetTemp.getTweetHashtags().get(j).getText().toLowerCase();
                                if (dayFilteredHashtags.contains(hashtagInTweet)) {
                                    dayFilteredHashtags.get(hashtagInTweet).setKey(dayFilteredHashtags.get(hashtagInTweet).getKey() + 1);
                                } else {
                                    dayFilteredHashtags.put(hashtagInTweet, new Pair<>(1, hashtagInTweet));
                                }
                            }
                        }

                        //creo mi heap para ordenar los hashtags por cantidad de apariciones
                        MyHeap<Integer, String> hashtagCountHeap = new MyHeapImpl<>(50000);

                        //Puedo obtener las keys porque está super filtrados por dia y por unicidad de hashtag (hash garantiza unicidad)
                        MyLinkedList<String> uniqueDayFilteredHashtags = dayFilteredHashtags.keys();

                        //inserto en heap para ordenar
                        for (int i = 0; i < uniqueDayFilteredHashtags.size(); i++) {
                            String hashtagTemp = uniqueDayFilteredHashtags.get(i);
                            if (!hashtagTemp.equalsIgnoreCase("f1")) {
                                hashtagCountHeap.insert(dayFilteredHashtags.get(hashtagTemp).getKey(), hashtagTemp);
                            }
                        }

                        //obtengo el maximooooooooo e imprimo resultado (el PAIR es CLAVE)
                        Pair<Integer, String> maxHashtag;
                        maxHashtag = hashtagCountHeap.removeMax();
                        System.out.println("El hashtag mas usado el dia " + dia + "/" + mes + "/" + anio + " fue: " + maxHashtag.getValue() + " con " + maxHashtag.getKey() + " apariciones");
                        tiempoFin = System.currentTimeMillis();
                    } else {
                        System.out.println("Fecha fuera de rango");
                    }
                }
            } else if (option == 5 && datosCargados) {
                tiempoInicio = System.currentTimeMillis();
                MyHeapImpl<Integer, String> heap = new MyHeapImpl<>(users.size());

                for (String userName : userNamesArray) {
                    if (userName == null) {
                        break;
                    }  else {
                        heap.insert(users.get(userName).getFavourites(), userName);
                    }
                }

                Pair<Integer, String> favourtiesAndUsername;
                System.out.println("N° de favoritos   Username");
                for (int i = 0; i < 7; i++) {
                    favourtiesAndUsername = heap.removeMax();
                    System.out.println(favourtiesAndUsername.getKey() + "       " + favourtiesAndUsername.getValue());
                }
                tiempoFin = System.currentTimeMillis();

            } else if (option == 6 && datosCargados) {
                System.out.print("Ingrese la frase: ");
                String frase = scanner.nextLine().toLowerCase();

                tiempoInicio = System.currentTimeMillis();

                int counter = 0;
                int tweetsSize = tweets.size();
                for (long i = 0; i < tweetsSize; i++) {
                    Tweet tweetTemp = tweets.get(i);
                    if (tweetTemp.getContent().toLowerCase().contains(frase)) {
                        counter++;
                    }
                }
                System.out.println("Cantidad de tweets con la palabra o frase '" + frase + "': " + counter);
                tiempoFin = System.currentTimeMillis();

            } else if (option == 7) {
                System.out.println();
                System.out.println("Programa finalizado");
                break;
            }
            System.out.println("Duracion: " + (tiempoFin - tiempoInicio) + " milisegundos");
            System.out.println();
        }

    }
}