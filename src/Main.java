import java.io.File;

public class Main {

    public static void main(String[] args) {
        File file = new File("song/");

        if (!file.exists())
            if (file.mkdir())
                System.out.println("Song directory has been created !");

        Window window = new Window();
    }
}
