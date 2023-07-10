import uy.edu.um.prog2.adt.Hash.MyHashTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CargaCSV {

    /**Funciones para formato correcto de la data leída del CSV*/

    /**Favourites a int: hay casos que son numeros con .0 (String -> double -> int)*/
    public int favouritesToInt(String favouritesString) throws NumberFormatException {
        double favourites= Double.parseDouble(favouritesString);
        return (int) favourites;
    }

    /** datetime en string a LocalDate*/
    public LocalDate dateToLocalDateFormat(String fechaString) throws ParseException {
        // Define the format of the input string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Parse the input string into a LocalDateTime object
        return LocalDate.parse(fechaString, formatter);
    }

    /**Funcion para pasar userVerified y isRetweet a boolean (están en "True" y "False")*/
    public boolean stringToBoolean(String booleanString) throws Exception{
        if (!booleanString.equals("True") && !booleanString.equals("False")){
            throw new Exception();
        } else {
            return Boolean.parseBoolean(booleanString);
        }
    }

    /**Funcion para los hashtags que vienen en un array pero string*/
    public String[] extractHashtags(String hashtagsString){
        // Remove square brackets and single quotes from the string
        String cleanedString = hashtagsString.replaceAll("\\[|'|\\]", "");
        // Split the cleaned string by commas to get individual hashtags
        String[] hashtags = cleanedString.split(", ");
        // Convert each hashtag to lowercase
        for (int i = 0; i < hashtags.length; i++) {
            hashtags[i] = hashtags[i].toLowerCase();
        }
        return hashtags;
    }

    /**Funcion que lee el archivo csv y carga los datos en los HashTables*/
    public void dataLoad(MyHashTable<Long, Tweet> tweets, MyHashTable<String, User> users, MyHashTable<String, HashTag> hashtags, String[] userNamesArray) throws IOException {
        Reader reader = new FileReader("src/f1_dataset.csv");
        Iterable<CSVRecord> csvParser = CSVFormat.EXCEL.parse(reader);

        String userName;
        int userFavourites;
        boolean userVerified;
        LocalDate date;
        String content;
        String[] hashtagsOfTweet;

        for (CSVRecord fila : csvParser) {
            if (fila.isConsistent()) {
                try {
                    userName = fila.get(1);
                    userFavourites = favouritesToInt(fila.get(7));
                    userVerified = stringToBoolean(fila.get(8));
                    date = dateToLocalDateFormat(fila.get(9));
                    content = fila.get(10);
                    hashtagsOfTweet = extractHashtags(fila.get(11));

                } catch (Exception e) {
                    continue;
                }
            } else {
                continue;
            }

            // Obtener usuario si existe y si no crearlo
            User userToAdd;
            if (users.contains(userName)) {
                userToAdd = users.get(userName);
                userToAdd.incrementTweetCount();                        // Si vuelve a aparecer el usuario, incrementa su cantidad de tweets
                if(userFavourites > userToAdd.getFavourites()){
                    userToAdd.setFavourites(userFavourites);            // Si vuelve a aparecer el usuario, actualiza su cantidad de favoritos asumiendo que aumentan con el tiempo
                }
            } else {
                userToAdd = new User(userName, userFavourites, userVerified);
                users.put(userName, userToAdd);
                userNamesArray[users.size() - 1] = userName;            //llevar un registro de los nombres de usuario sin repetidos
            }

            // Crear Tweet y crear Hashtag si no existía todavía
            Tweet tweetToAdd = new Tweet(date, content);
            tweets.put(tweetToAdd.getId(), tweetToAdd);

            for (String s : hashtagsOfTweet) {
                if (!hashtags.contains(s)) {
                    HashTag hashTagToAdd = new HashTag(s);
                    hashtags.put(hashTagToAdd.getText(), hashTagToAdd);
                    tweetToAdd.addHashTag(hashTagToAdd);
                } else {
                    HashTag existingHashTag = hashtags.get(s);
                    tweetToAdd.addHashTag(existingHashTag);
                }
            }
        }
    }
}