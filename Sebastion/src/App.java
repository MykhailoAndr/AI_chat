import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
     public static void main(String[] args) {
        System.out.println("");
        System.out.println("------- Sebastion_1_1 -------");
        System.out.println("To end dialog enter: By");
        System.out.println("");
        System.out.println("");
        System.out.println("-Glad to see you, Mr! My name Sebastion, how can I help you?");
        

        String fifoFile = "/tmp/sebastion_fifo";
        String fifoFileForPython = "/tmp/sebastion_fifo2";

        try (BufferedWriter pythonWriter = new BufferedWriter(new FileWriter(fifoFileForPython))) {

        while(true) {
            
        try (BufferedReader reader = new BufferedReader(new FileReader(fifoFile))) {
            String input;
            String question = "";
            while (((input = reader.readLine()) != null)) {
                // Process the input
                //System.out.println("");
                //System.out.println("Received input nomer2: " + input);
                
                


                    //вводимо запит
                    
                    //String question = scan.next();
                    //next - не можна використовувати пробіли в якості ключа запиту(можна вписати тільке одне слово)
                    
                    //вихід з програми, якщо використав стоп-слово
                    if (input.equalsIgnoreCase("By") || input.equalsIgnoreCase("by")) {
                        //System.out.println("Thanks for giving me life for a few seconds, wount see you soon!");
                        //Чомусь програма не запускається з виводженням повідомлення
                        // Можливо добавлення затримки перед виконанням break допоможе 
                        break;
                    }
                    if(input != question) {
                        question = input;
                        // відповідь чата 
                       // System.out.println("-" + chatGPT(question));
                        String response = chatGPT(question);
                        System.out.println("");
                        System.out.println("-" + response);

                        pythonWriter.write(response);
                        pythonWriter.newLine();
                        pythonWriter.flush(); 
                    }         
                
                // Perform your desired actions with the input
            }
        } catch (IOException e) {
            System.err.println("Error reading from FIFO file: " + e.getMessage());
            }
        
        //Scanner scan = new Scanner(System.in);

        
    }
    }  catch (IOException e) {
        System.err.println("Error writing to Python FIFO file: " + e.getMessage());
        }  //Зациклюємо запити та відповіді
        
        //scan.close();
    }


    // Настройка запита до API ChatGPT з налаштуваннями та надсилання на їх сервер
    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";

        // ключ розробника, потрібен для авторизації, можна отримати в акануті openai.
        // кількість запитів обмежанна, треба буде 5$, як мінімум  раз, можливо двічі, чи тричі   
        String apiKey = "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"; // API key goes here
        
        // Модель можна вибрати будь-яку
        String model = "gpt-4o";

        try {
            // налаштування підключення до їхнього сервера
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // body нашого запиту, включаючи модель, запитання, тип запиту та інші настройки
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Налаштування типу відповіді
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Сам текст відповіді
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method extracts the response expected from chatgpt and returns it.
    public static String extractContentFromResponse(String response) {
        // Позначаємо, де починається контент
        int maxWords = 80;
        int startMarker = response.indexOf("content") + 11;
        // І де закінчується 
        int endMarker = response.indexOf("\"", startMarker);
        // Видаляємо зайві символи та отримуємо текст відповіді
        String content = response.substring(startMarker, endMarker).trim();
    
        // Розбиваємо текст на речення
        String[] sentences = content.split("\\. "); // Розділяємо за крапкою
    
        StringBuilder limitedContent = new StringBuilder();
        int wordCount = 0;
    
        for (String sentence : sentences) {
            // Розбиваємо речення на слова
            String[] words = sentence.split("\\s+");
            // Перевіряємо, чи додавання цього речення не перевищить maxWords
            if (wordCount + words.length > maxWords) {
                break; // Виходимо, якщо додавання призведе до перевищення
            }
            limitedContent.append(sentence).append(". "); // Додаємо речення до обмеженого контенту
            wordCount += words.length; // Оновлюємо кількість слів
        }
    
        return limitedContent.toString().trim(); // Повертаємо обмежений текст
    }
    




    /* public static void main(String[] args) {
        String fifoFile = "/tmp/sebastion_fifo";
        System.out.println("Received input: nothing");

        try (BufferedReader reader = new BufferedReader(new FileReader(fifoFile))) {
            String input;
            while ((input = reader.readLine()) != null) {
                // Process the input
                System.out.println("Received input nomer2: " + input);
                // Perform your desired actions with the input
            }
        } catch (IOException e) {
            System.err.println("Error reading from FIFO file: " + e.getMessage());
        }
    }*/
}

//Проблеми

// Програма завжди запускається з другого разу 


//  періодично перестає відповідати на половинні речення, в кінці появляється знак "/"

// Можна попробувати інші його версії, можливо баг тільки в новій 