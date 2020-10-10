import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Charset w1251 = Charset.forName("Windows-1251");

    public static byte[] readFile(Path path) throws IOException {
        byte[] fileBytes = Files.readAllBytes(path);
        return fileBytes;
    }

    public static String toBits(byte[] bt) {
        String hiddenBits = "";
        for (byte b : bt) {
            hiddenBits = hiddenBits + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        System.out.println("\nСкрытая информация в битовом представлении: " + hiddenBits + "\n");
        return hiddenBits;
    }

    public static String addHiddenToText(String textString, String hiddenBits) {
        Map<Character, Character> states = new HashMap<>();
        states.put('К', 'K');
        states.put('Е', 'E');
        states.put('Н', 'H');
        states.put('Х', 'X');
        states.put('В', 'B');
        states.put('А', 'A');
        states.put('Р', 'P');
        states.put('О', 'O');
        states.put('С', 'C');
        states.put('М', 'M');
        states.put('Т', 'T');
        states.put('е', 'e');
        states.put('о', 'o');
        states.put('х', 'x');
        states.put('с', 'c');
        states.put('р', 'p');
        states.put('а', 'a');
        int count = 0;
        char[] chars = hiddenBits.toCharArray();
        char[] chText = textString.toCharArray();
        for (int n = 0; n < chText.length; n++) {
            if (states.containsKey(chText[n])) {
                if (count < chars.length) {
                    if (chars[count] == '1') {
                        chText[n] = states.get(chText[n]);
                    }
                    count++;
                }
            }
        }
        String res = new String(chText);
        return res;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        // C:\Projects\project_2_steganorrhaphy\files\hidden_phrase.txt
        System.out.println("\nВведите путь к файлу, в котором находится скрытая информация: ");
        Path pathHidden = Path.of(bf.readLine());
        byte[] hiddenString = readFile(pathHidden);
        String hiddenBits = toBits(hiddenString);

        // C:\Projects\project_2_steganorrhaphy\files\text.txt
        System.out.println("Введите путь к файлу, в котором находится основной текст: ");
        Path pathText = Path.of(bf.readLine());
        String textString = Files.readString(pathText, w1251);

        String result = addHiddenToText(textString, hiddenBits);

        // C:\Projects\project_2_steganorrhaphy\files\result2.txt
        System.out.println("\nВведите путь к результирующему файлу, в котором будет находится текст со скрытой информацией: ");
        String pathResult = bf.readLine();
        File fileOutput = new File(pathResult);
        FileOutputStream outStream = new FileOutputStream(fileOutput);
        outStream.write(result.getBytes(w1251));
    }
}
